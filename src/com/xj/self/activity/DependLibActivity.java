package com.xj.self.activity;

import android.os.Bundle;
import android.util.Log;

import com.haha.common.ativity.TestUIActivity;

public class DependLibActivity extends TestUIActivity {
    
    private final static String TAG = "DependLibActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "create view sucess");;
    }

    
}
