package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

public class popup_mission_complete extends DialogFragment implements View.OnClickListener {

    public static final String TAG="dialog_event";
    public popup_mission_complete() {}
    public static popup_mission_complete getInstance() {
        popup_mission_complete p=new popup_mission_complete();
        return p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_popup_mission_complete, container);
        Button confirmBtn=(Button)v.findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(this);
        setCancelable(false);
        return v;
    }
    @Override
    public void onClick(View view) {
        Intent i=new Intent();
        i.setClass(view.getContext(), TabActivity.class);
        startActivity(i);
    }
}