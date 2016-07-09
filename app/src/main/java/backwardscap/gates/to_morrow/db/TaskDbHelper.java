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

import java.text.DateFormat;
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

        Log.d("Add Task",t.getTaskText()+" | " + t.getDate());

        SQLiteDatabase db= this.getWritableDatabase();

        db.insert(tableName,null,v);
    }


    public boolean updateTask (Task task, Task newTask, String table)
    {
        if(isLaterDate(newTask,task)){
            Log.d("Date","Task is newer");
        }
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+  table+
                " SET "+    TaskContract.TaskEntry.COL_TASK_TITLE+"=\""+newTask.getTaskText()+"\","+
                TaskContract.TaskEntry.COL_TASK_DATE+"=\""+newTask.getDate()+
                "\" WHERE "+  TaskContract.TaskEntry.COL_TASK_TITLE+"=\""+task.getTaskText()+"\" AND "+
                TaskContract.TaskEntry.COL_TASK_DATE+"=\""+task.getDate()+"\"";
        ContentValues contentValues = new ContentValues();
        db.execSQL(query);

        Log.d("SQL",query);
        return true;
    }

    public boolean isLaterDate(Task t, Task compareTo){
        boolean isLater = false;

        char[] t1 = t.getDate().toCharArray();
        char[] t2 = compareTo.getDate().toCharArray();

//      ("MM-dd-YYYY kk:mm:ss")
//      ("0123456789012345678")


        int t1Year = Integer.parseInt(""+t1[6]+t1[7]+t1[8]+t1[9]);
        int t2Year = Integer.parseInt(""+t1[6]+t1[7]+t1[8]+t1[9]);

        int t1Month = Integer.parseInt(""+t1[0]+t1[1]);
        int t2Month = Integer.parseInt(""+t2[0]+t2[1]);

        int t1Day = Integer.parseInt(""+t1[3]+t1[4]);
        int t2Day = Integer.parseInt(""+t2[3]+t2[4]);

        int t1Hr = Integer.parseInt(""+t1[11]+t1[12]);
        int t2Hr = Integer.parseInt(""+t2[11]+t2[12]);

        int t1Min = Integer.parseInt(""+t1[14]+t1[15]);
        int t2Min = Integer.parseInt(""+t2[14]+t2[15]);

        int t1Sec = Integer.parseInt(""+t1[17]+t1[18]);
        int t2Sec = Integer.parseInt(""+t2[17]+t2[18]);

        if(t1Year == t2Year) {
            Log.d("Date","Year same");
            //years are the same,compare months
            if (t1Month == t2Month) {
                Log.d("Date","Month same");
                //Months are the same, compare days
                if(t1Day==t2Day){
                    Log.d("Date","Day same");
                    //Days are the same, compare hours
                    if(t1Hr==t2Hr){
                        Log.d("Date","Hr same");
                        //hrs are the same, compare min
                        if(t1Min==t2Min){
                            Log.d("Date","Min same");
                            //mins are same, finally compare seconds
                            if(t1Sec>t2Sec) {
                                Log.d("Date","Seconds is newer");
                                isLater = true;
                            }
                            else if(t1Sec<t2Sec) {
                                Log.d("Date","Seconds is older");
                                isLater = false;
                            }
                        }
                        else if(t1Min>t2Min)
                            isLater = true;
                    }
                    else if(t1Hr > t2Hr)
                        isLater=true;
                }
                else if(t1Day > t2Day)
                    isLater = true;
            }
        }
        else if(t1Year>t2Year)
                isLater=true;



        return isLater;
    }

    public boolean deleteTask(String tableName,Task task){
        boolean r = false;

        String query = "Delete from " + tableName + " where "+TaskContract.TaskEntry.COL_TASK_TITLE+" = \"" + task.getTaskText() + "\""
                +" AND "+TaskContract.TaskEntry.COL_TASK_DATE+" = \""+task.getDate()+"\"";

        Log.d("SQLite",query);

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(query);

        /*Task t = new Task();

        //TODO: match task based on date and time that it was created
        if(c.moveToFirst()){

            //Log.d("Delete",c.getString(0) + " | " + c.getString(1) + " | " + c.getString(2));
            t.setID(Integer.parseInt(c.getString(0)));
            t.setTaskText(task.getTaskText());



            //db.delete(tableName, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
             //       new String[] {String.valueOf(t.getTaskText())});
            r = true;
        }
        c.close();
        */
        db.close();

        return true;

    }


    //Populate arraylist with all tasks
    public ArrayList<Task> getAllTasks(String table)
    {
        ArrayList<Task> array_list = new ArrayList<>();



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+table, null );
        if(cursor.moveToFirst()){
            Task t = new Task();

            String newTask =cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE));

            t.setTaskText(newTask);

            String newDate = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DATE));

            t.setDate(newDate);

            //Log.d("Task",newTask + " | " + newDate);

            array_list.add(t);

            //Walk through the array of tasks, adding each to the taskList
            while(cursor.moveToNext()){
                t=new Task();

                String newTask2 =cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE));

                t.setTaskText(newTask2);

                String newDate2 = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DATE));

                t.setDate(newDate2);


                array_list.add(t);
                //Log.d("Task2",newTask2 + " | " + newDate2);
            }
        }


        cursor.close();
        db.close();

        return array_list;
    }
}
