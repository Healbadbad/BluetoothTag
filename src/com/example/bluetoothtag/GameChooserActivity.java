package com.example.bluetoothtag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GameChooserActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamechooser);
	}
	
	// Throw the Bluetooth host information here.
	// From here, I'd imagine the data needs to be sent to the server about the game.
	// Then when others search online for the info about a player and they find the similar bluetooth address,
	// That address is searched for on the server.
	// From there, a game is created. 
	
}