package com.example.bluetoothtag;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class GameHostActivity extends Activity {
	static HashMap<String, HashMap<String, String>> Firedata = new HashMap<String, HashMap<String, String>>();
	ArrayAdapter<String> adapter;
	private BluetoothAdapter bluetoothAdapter;
	ArrayList<String> players;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.gamechooser);
		players = new ArrayList<String>();
		Log.d("bluetoothtag", "Host: initialized everything but server");
		final String selfname = bluetoothAdapter.getDefaultAdapter().getName();
		Firebase ref = new Firebase(
				"https://blistering-fire-1807.firebaseio.com/");
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot arg0) {
				Log.d("bluetoothtag", "" + players);
				Firedata = (HashMap<String, HashMap<String, String>>) arg0
						.getValue();
				String[] temp;
				try{
					temp =Firedata.get(selfname).get("Lobby").split(":");
					temp = Firedata.get(selfname).get("Lobby")
							.split(":");
					for (String s : temp) {
						players.add(s);
					}
					Log.d("bluetoothtag", "datachange!");
					adapter.addAll(players);
					adapter.notifyDataSetChanged();
				}catch (NullPointerException e){
					Log.w("bluetoothtag","Firebase data was null");
				}
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				Log.d("bluetoothtag", "cancelled listener");

			}

		});
		Log.d("bluetoothtag", "Host: initialized server");

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Lobby", selfname);
		data.put("IT", "");
		Firedata.put(selfname, data);
		ref.setValue(Firedata);
		Log.d("bluetoothtag", "Host: added self to server");

		String[] temp = Firedata.get(selfname).get("Lobby").split(":");
		for (String s : temp) {
			players.add(s);
		}
		adapter = new ArrayAdapter<String>(this, R.layout.activity_list_item,
				players);

		Log.d("bluetoothtag", "Host: update adapter");

		ListView lv = new ListView(this);

		lv.setAdapter(adapter);// maybe want to add a click handler to kick
								// people here?

		this.setContentView(lv);// Want it to display lobby, so need to start
								// lobby with current host, blank lobby, blank
								// it, need startgame button
		Log.d("bluetoothtag", "Host: Set Listview");

	}

	private void startGame() {
		Intent intent = new Intent(this, GamePlayingActivity.class);
		// put rest of info into intent such as setting the names of people in the lobby, gametype, and other misc info 
		startActivity(intent);
	}
	
}
