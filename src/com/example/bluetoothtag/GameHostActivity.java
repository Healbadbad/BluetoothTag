package com.example.bluetoothtag;

import java.util.HashMap;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class GameHostActivity extends Activity{
	static HashMap<String,HashMap<String,String>> Firedata = new HashMap<String,HashMap<String,String>>();
	ArrayAdapter adapter;
	private BluetoothAdapter bluetoothAdapter;
	String[] players;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String selfname = bluetoothAdapter.getDefaultAdapter().getName();
		Firebase ref = new Firebase("https://blistering-fire-1807.firebaseio.com/");
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot arg0) {
				Firedata = (HashMap<String, HashMap<String, String>>) arg0.getValue();
				System.out.println(Firedata.get("one"));
				players =  Firedata.get(selfname).get("Lobby").split(":");
				adapter.notifyDataSetChanged();
			}
			@Override
			public void onCancelled(FirebaseError arg0) {
			}
		});
		HashMap<String,String> data = new HashMap<String,String>();
		data.put("Lobby",selfname);
		data.put("IT","");
		Firedata.put(selfname, data);
		players = Firedata.get(selfname).get("Lobby").split(":");
		adapter = new ArrayAdapter(this, R.layout.activity_list_item, players);
		ListView lv = new ListView(this);
		lv.setAdapter(adapter);// maybe want to add a click handler to kick people here?
		
		this.setContentView(lv);// Want it to display lobby, so need to start lobby with current host, blank lobby, blank it, need startgame button
		
	}
	private void startGame(){
		Intent intent = new Intent(this, GamePlayingActivity.class);
		startActivity(intent);
	}
}
