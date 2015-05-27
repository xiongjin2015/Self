package com.xj.self.activity;

import com.xj.self.manager.Single;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

public class TestActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText("Hello World!");
		setContentView(tv);
		Single.getInstance(this);
	}
	
	@Override
	protected void finalize() throws Throwable {
        Log.i("XJ", "---gc---");
		super.finalize();
	}

}
