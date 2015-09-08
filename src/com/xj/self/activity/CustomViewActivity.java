package com.xj.self.activity;

import android.app.Activity;
import android.os.Bundle;

import com.xj.self.R;

public class CustomViewActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.custom_view_title);
        setContentView(R.layout.custom_view_image);
    }

}
