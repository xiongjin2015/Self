package com.xj.self.activity.gl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xj.self.R;
import com.xj.self.service.FloatWindowService;

public class FloatWindow360Activity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_360floatwindow_demo);
    }
    
    public void startFloatWindow(View view){
        Intent intent = new Intent(this,FloatWindowService.class);
        startService(intent);
        finish();
    }

}
