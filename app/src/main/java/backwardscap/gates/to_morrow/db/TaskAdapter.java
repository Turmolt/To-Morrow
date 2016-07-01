package backwardscap.gates.to_morrow.db;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import backwardscap.gates.to_morrow.R;

/**
 * Created by Sam on 6/29/2016.
 */

public class TaskAdapter extends ArrayAdapter<Task>{

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        // Get the data item for this position
        Task t = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        // Lookup view for data population
        TextView task = (TextView) convertView.findViewById(R.id.task_title);
        TextView date = (TextView) convertView.findViewById(R.id.task_date);

        Log.d("TaskAdapter",""+position);
        ImageView IV = (ImageView)convertView.findViewById(R.id.startDivider);
        //if position is 0 then turn start divider alpha to 1
        if(position == 0){

            IV.setAlpha(1.0f);
        }else{

            IV.setAlpha(0.0f);
        }


        // Populate the data into the template view using the data object
        task.setText(t.getTaskText());
        date.setText(t.getDate());
        // Return the completed view to render on screen
        return convertView;
    }
}
