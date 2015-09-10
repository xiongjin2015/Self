package com.xj.self.activity.lmj.viewgroup;

import com.xj.self.R;

import android.app.Activity;
import android.os.Bundle;

public class CustomImgContainerActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //ViewGroup宽和高设置为固定值
        setContentView(R.layout.activity_custom_image_view_group);
        
        //ViewGroup的宽和高设置为wrap_content
        //setContentView(R.layout.activity_custom_image_view_group);
        
        //ViewGroup的宽和高设置为match_parent
        //setContentView(R.layout.activity_custom_image_view_group);
    }

}
