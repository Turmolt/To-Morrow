package backwardscap.gates.to_morrow;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.DialogInterface;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import backwardscap.gates.to_morrow.db.Task;
import backwardscap.gates.to_morrow.db.TaskContract;
import backwardscap.gates.to_morrow.db.TaskDbHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TaskDbHelper dbHelper;
    private ListView mTaskListView;

    private Date date;
    private String myDate;
    private SimpleDateFormat dateForm;



    private ListView TmrListView;

    private ArrayAdapter<String> dateAdapter;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        TmrListView = (ListView) findViewById(R.id.list_tmr);
        dateForm = new SimpleDateFormat("MM-dd");
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
                                date = new Date();
                                myDate = dateForm.format(date);
                                Task t = new Task();
                                String task = String.valueOf(taskEditText.getText());
                                t.setTaskText(task);
                                t.setDate(""+myDate);
                                dbHelper.AddTask(TaskContract.TaskEntry.TDY_TABLE, t);

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

    public void editTextTDY(View view){
        View parent = (View) view.getParent();

        final TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        taskTextView.setSelected(true);

        final String oldTask = taskTextView.getText().toString();

        //Log.d(TAG,"Add a new task");
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Editing task")
                .setView(taskEditText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        date = new Date();
                        myDate = dateForm.format(date);
                        Task t = new Task();
                        String task = String.valueOf(taskEditText.getText());
                        t.setTaskText(task);
                        t.setDate(""+myDate);
                        dbHelper.updateTask(oldTask, t, TaskContract.TaskEntry.TDY_TABLE);

                        UpdateUI();

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void deleteTask(View view){
        //Get View Parent
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);

        //Identify task
        Task task = new Task();
        task.setTaskText(String.valueOf(taskTextView.getText()));

        //send command to dbHelper to delete task
        dbHelper.deleteTask(TaskContract.TaskEntry.TDY_TABLE,task);
        UpdateUI();
    }

    @SuppressWarnings("unchecked")
    private void UpdateUI(){
        ArrayList taskDate = dbHelper.getAllTasks(TaskContract.TaskEntry.TDY_TABLE);
        ArrayList taskList = (ArrayList)taskDate.get(0);
        ArrayList dateList = (ArrayList)taskDate.get(1);

        //If the ArrayAdapter is not there, create one
        if(mAdapter==null){

            mAdapter = new ArrayAdapter<>(this, R.layout.item_todo, R.id.task_title, taskList);
            dateAdapter = new ArrayAdapter<>(this,R.layout.item_todo,R.id.task_date,dateList);
            mTaskListView.setAdapter(mAdapter);
            mTaskListView.setAdapter(dateAdapter);
        }
        //Otherwise we clear it and then add all tasks
        else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();

            dateAdapter.clear();
            dateAdapter.addAll(dateList);
            dateAdapter.notifyDataSetChanged();
        }
    }
}
