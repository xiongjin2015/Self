package com.xj.self.activity.lmj;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.haha.self.R;
import com.xj.self.view.lmj623565791.MetroImageView;

public class MetroImageActivity extends Activity {
    
    private MetroImageView joke;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_win8_metro);
        joke = (MetroImageView)findViewById(R.id.ic_joke);
        joke.setOnClickListener(new MetroImageView.OnViewClickListener() {
            
            @Override
            public void onViewClick(MetroImageView view) {
                Toast.makeText(MetroImageActivity.this, "joke", Toast.LENGTH_LONG).show();
                
            }
        });
    }

}
