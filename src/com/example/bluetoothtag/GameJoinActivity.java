package com.example.bluetoothtag;

import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class GameJoinActivity extends Activity {
	BluetoothTask loader;
	HashMap<String,Short> devices = new HashMap<String,Short>();
	HashMap<String,HashMap<String,String>> Firedata = new HashMap<String,HashMap<String,String>>();
	ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamechooser);
		Log.d("bluetoothtag","set the def layout gamechooser");
//		View peopleJoined = findViewById(R.id.PeopleCount);
		
		
		Log.d("bluetoothtag","Started the asyncTask");
		
		adapter = new ArrayAdapter<String>(this, R.layout.activity_list_item);
		ListView lv = new ListView(this);
		lv.setAdapter(adapter);// maybe want to add a click handler to kick people here?
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String label = (String) ((TextView) view).getText();
				startLobby(label);
			}
		});
		this.setContentView(lv);// Want it to display lobby, so need to start lobby with current host, blank lobby, blank it, need startgame button
//		loader.execute(this);
//		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,players);
	}
	@SuppressLint("NewApi")
	public void setdevices(HashMap<String, Short> newlist){
		this.devices = newlist;
		adapter.addAll(findhosts());
		adapter.notifyDataSetChanged();
	}
	private ArrayList<String> findhosts(){
		Firebase ref = new Firebase("https://blistering-fire-1807.firebaseio.com/");
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot arg0) {
				Firedata = (HashMap<String, HashMap<String, String>>) arg0.getValue();
//				System.out.println(Firedata.get("one"));
//				players =  Firedata.get(selfname).get("Lobby").split(":");
//				adapter.notifyDataSetChanged();
			}
			@Override
			public void onCancelled(FirebaseError arg0) {
			}
		});
		ArrayList<String> hosts = new ArrayList<String>();
		for(String s: devices.keySet()){
			for(String k :Firedata.keySet()){
				if(s.equals(k)){
					hosts.add(k);
				}
			}
		}
		return hosts;
	}
	
	public void startLobby(String host) {
		Intent intent = new Intent(this, GameLobbyActivity.class);
		String message = host;
		intent.putExtra("Host", message);
		startActivity(intent);
	}
	
	
	
	// Throw the Bluetooth host information here.
	// From here, I'd imagine the data needs to be sent to the server about the game.
	// Then when others search online for the info about a player and they find the similar bluetooth address,
	// That address is searched for on the server.
	// From there, a game is created. 
	protected void onPause(){
		super.onPause();
		loader.cancel(true);
	}
	protected void onResume(){
		super.onResume();
		loader = new BluetoothTask();
		loader.parent = this;
		loader.execute(this);
	}
	protected void onDestroy(){
		super.onDestroy();
		
	}
	
}
