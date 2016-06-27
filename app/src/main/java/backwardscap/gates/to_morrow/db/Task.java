package backwardscap.gates.to_morrow.db;

/**
 * Created by Sam on 6/27/2016.
 */

//Task data structure for use in SQLite DB
public class Task {

    private int _id;
    private String _taskText;
    private String _date;

    public Task(){

    }

    //Create fully filled task
    private Task(int id, String taskText, String date){
        this._id = id;
        this._taskText = taskText;
        this._date = date;
    }

    public Task(String taskText, String date){
        this._taskText = taskText;
        this._date = date;
    }

    public void setID(int id){
        this._id=id;
    }

    public int getID(){
        return this._id;
    }

    public void setTaskText(String taskText){
        this._taskText = taskText;
    }

    public String getTaskText(){

        return this._taskText;
    }

    public void setDate(String date){
        this._date = date;
    }

    public String getDate(){
        return this._date;
    }

}
