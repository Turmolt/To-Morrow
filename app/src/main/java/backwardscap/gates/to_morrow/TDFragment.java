package backwardscap.gates.to_morrow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import backwardscap.gates.to_morrow.R;

/**
 * Created by Sam on 6/27/2016.
 */


//Fragment class handling the To Do Lists
public class TDFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main,container,false);
    }



}
