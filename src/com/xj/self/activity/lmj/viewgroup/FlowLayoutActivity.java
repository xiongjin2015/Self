package com.xj.self.activity.lmj.viewgroup;

import com.xj.self.R;

import android.app.Activity;
import android.os.Bundle;

public class FlowLayoutActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //ViewGroup的宽和高设置为match_parent
        //setContentView(R.layout.activity_flow_layout);
        
        //ViewGroup的宽固定
        setContentView(R.layout.activity_flow_layout1);
        
        
    }

}
