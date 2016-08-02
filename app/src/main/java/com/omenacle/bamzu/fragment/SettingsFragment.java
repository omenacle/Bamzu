package com.omenacle.bamzu.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omenacle.bamzu.R;

public class SettingsFragment extends Fragment{

    public SettingsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        return layoutInflater.inflate(R.layout.fragment_settings,container,false);
    }
}