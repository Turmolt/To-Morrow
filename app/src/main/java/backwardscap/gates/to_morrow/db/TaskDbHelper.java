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
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TDY_TABLE + " ( "
                + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, "+
                TaskContract.TaskEntry.COL_TASK_DATE +" TEXT NOT NULL);";
        db.execSQL(createTable);


        String createTmrTable = "CREATE TABLE " + TaskContract.TaskEntry.TMR_TABLE + " ( "
                + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL," +
                TaskContract.TaskEntry.COL_TASK_DATE +" TEXT NOT NULL);";
        db.execSQL(createTmrTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TDY_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TMR_TABLE);

        onCreate(db);

    }

    public Task getThisTask(String tableName, String taskName){
        Task t = new Task();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select * from "+ tableName + " where " + TaskContract.TaskEntry.COL_TASK_TITLE +
                " = \"" + taskName + "\"",null);

        if(c.moveToFirst()) {
            c.moveToFirst();
            Log.d("Cursor", c.getString(0) + " | " + c.getString(1) + " | " + c.getString(2));
            t.setID(Integer.parseInt(c.getString(0)));
            t.setTaskText(c.getString(1));
            t.setDate(c.getString(2));
            c.close();
        } else {

            Log.d("Cursor","No task");
            t=null;

        }
        db.close();
        Log.d("TG","New Lookup Working");
        return t;
    }

    public void AddTask(String tableName, Task t){
        ContentValues v = new ContentValues();

        v.put(TaskContract.TaskEntry.COL_TASK_TITLE,t.getTaskText());
        v.put(TaskContract.TaskEntry.COL_TASK_DATE,t.getDate());

        SQLiteDatabase db= this.getWritableDatabase();

        db.insert(tableName,null,v);
    }


    public boolean updateTask (String task, String newTask, String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COL_TASK_TITLE, newTask);
        contentValues.put(TaskContract.TaskEntry.COL_TASK_DATE, Calendar.DATE);
        db.update(table, contentValues, TaskContract.TaskEntry.COL_TASK_TITLE+" = ? ", new String[] { task } );
        return true;
    }

    public boolean deleteTask(String tableName,String taskName){
        boolean r = false;

        String query = "Select * from " + tableName + " where "+TaskContract.TaskEntry.COL_TASK_TITLE+" = \"" + taskName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(query,null);

        Task t = new Task();


        if(c.moveToFirst()){
            Log.d("Delete",c.getString(0) + " | " + c.getString(1) + " | " + c.getString(2));
            t.setID(Integer.parseInt(c.getString(0)));
            t.setTaskText(taskName);
            db.delete(tableName, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                    new String[] {String.valueOf(t.getTaskText())});
            c.close();
            r = true;
        }

        db.close();
        return r;
    }

    public ArrayList<String> getAllTasksTMR()
    {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+TaskContract.TaskEntry.TMR_TABLE, null);
        if(cursor.moveToFirst()){
            array_list.add(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE)));

            //Walk through the array of tasks, adding each to the taskList
            while(cursor.moveToNext()){
                int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
                array_list.add(cursor.getString(idx));
            }
        }

        cursor.close();
        db.close();

        return array_list;
    }

    //Populate arraylist with all tasks
    public ArrayList<String> getAllTasksTDY()
    {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+TaskContract.TaskEntry.TDY_TABLE, null );
        if(cursor.moveToFirst()){
            array_list.add(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE)));

            //Walk through the array of tasks, adding each to the taskList
            while(cursor.moveToNext()){
                int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
                array_list.add(cursor.getString(idx));
            }
        }

        cursor.close();
        db.close();

        return array_list;
    }
}
