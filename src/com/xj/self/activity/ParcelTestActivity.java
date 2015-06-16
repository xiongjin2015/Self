package com.xj.self.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.haha.self.R;
import com.xj.self.entity.Entity;

public class ParcelTestActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parecal_test);
	      Entity entity = new Entity(1,"xj");
	      entity.setName("zhangwu");
	}
	
	
	public void onClick(View v){
		Intent intent = new Intent(this,ParcelResultActivity.class);
		Entity entity = new Entity(2,"xj");
		entity.setName("lishi");
		intent.putExtra("Parcel", entity);
		startActivity(intent);
	}

}
