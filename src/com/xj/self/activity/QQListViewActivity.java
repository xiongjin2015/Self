package com.xj.self.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.haha.self.R;
import com.xj.self.view.lmj623565791.QQListView;
import com.xj.self.view.lmj623565791.QQListView.DelButtonClickListener;

public class QQListViewActivity extends Activity {
    
    private QQListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> mDatas;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_qq_list);
        
        mListView = (QQListView)findViewById(R.id.listview);
        /**不要直接Arrays.asList*/
        mDatas = new ArrayList<String> (Arrays.asList("HelloWorld","Welcome","Java","Servlet","Struts","Hibernate","Spring","HTML5","JavaScripte","Lucene"));
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDatas);
        mListView.setAdapter(mAdapter);
        
        mListView.setDelButtonClickListener(new DelButtonClickListener() {
            
            @Override
            public void onClick(int position) {
                Toast.makeText(QQListViewActivity.this, position + " : " + mAdapter.getItem(position), Toast.LENGTH_LONG).show();  
                mAdapter.remove(mAdapter.getItem(position));
            }
        });
        
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(QQListViewActivity.this, position + " : " + mAdapter.getItem(position), Toast.LENGTH_LONG).show();
            }
  
        });
    }

}
