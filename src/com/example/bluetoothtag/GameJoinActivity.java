package com.example.bluetoothtag;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GameJoinActivity extends Activity {

	ListView joinList;
	
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamejoiner);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		
		joinList = (ListView) findViewById(R.id.joinlist);
		joinList.setAdapter(adapter);
		
	}

}
