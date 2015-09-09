package com.xj.self.activity.lmj;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.xj.self.R;
import com.xj.self.view.lmj623565791.GestureLockViewGroup;
import com.xj.self.view.lmj623565791.GestureLockViewGroup.OnGestureLockViewListener;

public class GestureLockViewActivity extends Activity {
    
    private GestureLockViewGroup mGestureLockViewGroup;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_gesture_lock_view);
        
        mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.gestureLockViewGroup);
        mGestureLockViewGroup.setAnswer(new int[]{1,2,3,4,5});
        mGestureLockViewGroup.setOnGestureLockViewListener(new OnGestureLockViewListener(){
            
            @Override
            public void onUnMatchedExceedBoundary() {
                Toast.makeText(GestureLockViewActivity.this, "错误5次...", Toast.LENGTH_SHORT).show();
                mGestureLockViewGroup.setUnMatchExceedBoundary(5);
                
            }
            
            @Override
            public void onGestureEvent(boolean matched) {
                Toast.makeText(GestureLockViewActivity.this, matched+"",  
                        Toast.LENGTH_SHORT).show();
                
            }
            
            @Override
            public void onBlockSelected(int position) {
                // TODO Auto-generated method stub
                
            }
        });
    }

}
