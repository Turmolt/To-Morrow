package backwardscap.gates.to_morrow.db;

/**
 * Created by Sam on 6/20/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskDbHelper extends SQLiteOpenHelper {



    public TaskDbHelper(Context context){
        super(context,TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( "
                + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }

    public void getThisTask( int ID){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select task from " + TaskContract.TaskEntry.TABLE + " where ID = "+ ID,null);
        c.moveToFirst();

        Log.d("TAG",c.getString(0));

    }

    public boolean updateTask (Integer id, String task, String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("date", Calendar.getInstance().DAY_OF_YEAR +","+Calendar.getInstance().YEAR);
        db.update(table, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }


    //Populate arraylist with all tasks
    public ArrayList<String> getAllTasks()
    {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from tasks", null );
        cursor.moveToFirst();

        //Walk through the array of tasks, adding each to the taskList
        while(cursor.moveToNext()){
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            array_list.add(cursor.getString(idx));
        }

        cursor.close();
        db.close();


        return array_list;
    }
}
