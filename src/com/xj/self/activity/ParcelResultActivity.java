package com.xj.self.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.xj.self.entity.Entity;
import com.xj.self.manager.Single;

public class ParcelResultActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Entity entity = (Entity)intent.getParcelableExtra("Parcel");
		TextView tv = new TextView(this);
		tv.setText("entity [id="+entity.getId()+",name="+entity.getName()+"]");
		setContentView(tv);
		//Single.getInstance(this);
	}
	
	@Override
	protected void finalize() throws Throwable {
        Log.i("XJ", "---gc---");
		super.finalize();
	}
	

}
