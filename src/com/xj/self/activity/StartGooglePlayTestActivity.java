package com.xj.self.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.haha.self.R;

public class StartGooglePlayTestActivity extends ActionBarActivity {
	
	static final String PLAY_PKG_NAME = "com.android.vending";
	
	private EditText mPkgNameEditTxt;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_play_test);
		mPkgNameEditTxt= (EditText) findViewById(R.id.pkg);
	}
	
	
	public void onClick(View v){
/*		if(TextUtils.isEmpty(mPkgNameEditTxt.getText())){
			Toast.makeText(this, "please input app package name,pkg name is not null", Toast.LENGTH_LONG).show();
			return;
		}*/	
		//market://details?id=com.teamlava.restaurantstory&referrer=
		String url ="market://details?id="+mPkgNameEditTxt.getText();
		Log.i("XJ", "url:"+url);
        Intent playIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        playIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        playIntent.setPackage(PLAY_PKG_NAME);
        try {
            startActivity(playIntent);
        } catch (Exception e) {
           e.printStackTrace();
            // no play crash
            //startBrowser(data, url);
        }
		
	}
	
	

}
