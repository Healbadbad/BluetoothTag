package com.example.bluetoothtag;

import java.util.ArrayList;
import java.util.HashMap;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GameLobbyActivity extends Activity {

	ListView joinList;

	ArrayAdapter<String> adapter;
	static HashMap<String, HashMap<String, String>> Firedata = new HashMap<String, HashMap<String, String>>();
	ArrayAdapter adapter2;
	private BluetoothAdapter bluetoothAdapter;
	ArrayList<String> players;
	int attempt = 0;

	// String hostname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		Intent prefs = this.getIntent();
		final String host = prefs.getExtras().getString("Host");
		players = new ArrayList<String>();
		// setContentView(R.layout.gamejoiner);
		final String selfname = bluetoothAdapter.getDefaultAdapter().getName();
		final Firebase ref = new Firebase(
				"https://blistering-fire-1807.firebaseio.com/");
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot arg0) {
				Firedata = (HashMap<String, HashMap<String, String>>) arg0
						.getValue();
				if (attempt == 0) {
					attempt = 1;
					HashMap<String, String> data = new HashMap<String, String>();
					data = Firedata.get(host);
					Log.d("bluetoothtag","data: "+data);
					String lobby = data.get("Lobby");
					if(lobby.contains(selfname)){ // don't add self to lobby if already in lobby
						
					}else{
					lobby = lobby.concat(":" + selfname);
					data.put("Lobby", lobby);
					Log.d("bluetoothtag","data: "+data);
					Firedata.put(host, data);
					ref.setValue(Firedata);
					Log.d("bluetoothtag", "Host: added self to server");
					}
					String[] temp = { "" };
//					try {
//						temp = Firedata.get(host).get("Lobby").split(":");
//					} catch (NullPointerException e) {
//						Log.w("bluetoothtag", "Firebase data was null");
//					}
					temp = lobby.split(":");
					players = new ArrayList<String>();
					for (String s : temp) {
						players.add(s);
					}
					adapter.clear();
					adapter.addAll(players);
					return;
				}
				String[] temp;
				try {
					temp = Firedata.get(host).get("Lobby").split(":");
					players = new ArrayList<String>();
					for (String s : temp) {
						players.add(s);
					}
					Log.d("bluetoothtag", "datachange!");
					adapter.clear();
					adapter.addAll(players);
				} catch (NullPointerException e) {
					Log.w("bluetoothtag", "Firebase data was null");
				}
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
			}
		});
		HashMap<String, String> data = new HashMap<String, String>();
		// data.put("Lobby",selfname);
		adapter = new ArrayAdapter(this, R.layout.activity_list_item, players);
		ListView lv = new ListView(this);
		lv.setAdapter(adapter);// maybe want to add a click handler to kick
								// people here?
		this.setContentView(lv);// Want it to display lobby, so need to start
								// lobby with current host, blank lobby, blank
								// it, need startgame button
		// adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,players);
		this.setTitle(host+"'s lobby");
	}

}
