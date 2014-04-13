package com.example.bluetoothtag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class GameChooserActivity extends Activity {
	BluetoothTask loader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamechooser);
		
		View peopleJoined = findViewById(R.id.PeopleCount);
		
		loader = new BluetoothTask();
		loader.parent = this;
		loader.execute(this);
		Log.d("bluetoothetag","Started the asyncTask");
	}
	
	public void startGame(View view) {
		Intent intent = new Intent(this, GamePlayingActivity.class);
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
	
}
