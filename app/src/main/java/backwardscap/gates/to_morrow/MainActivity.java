package backwardscap.gates.to_morrow;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import backwardscap.gates.to_morrow.db.TaskContract;
import backwardscap.gates.to_morrow.db.TaskDbHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TaskDbHelper dbHelper;
    private ListView mTaskListView;
    private ListView TmrListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);



        //SQLiteDatabase db = dbHelper.getReadableDatabase();

        UpdateUI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.action_add_task:
                //Log.d(TAG,"Add a new task");
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add new task")
                        .setMessage("What do you have to do?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE,task);
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                UpdateUI();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editText(View view){

        View parent = (View) view.getParent();

        final TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        taskTextView.setSelected(true);

        String task = String.valueOf(taskTextView.getText());

        ArrayList arrayList = dbHelper.getAllTasks();



        Log.d(TAG,"EndiT");
    }

    public void deleteTask(View view){
        //Get View Parent
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);

        //Identify task
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Delete entry
        db.delete(TaskContract.TaskEntry.TABLE,TaskContract.TaskEntry.COL_TASK_TITLE+" = ?",
                new String[]{task});
        db.close();
        UpdateUI();
    }

    @SuppressWarnings("unchecked")
    private void UpdateUI(){

        ArrayList taskList = dbHelper.getAllTasks();

        //If the ArrayAdapter is not there, create one
        if(mAdapter==null){

            mAdapter = new ArrayAdapter<>(this, R.layout.item_todo, R.id.task_title, taskList);
            mTaskListView.setAdapter(mAdapter);
        }
        //Otherwise we clear it and then add all tasks
        else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();

        }
        //cursor.close();
        //db.close();
    }

}
