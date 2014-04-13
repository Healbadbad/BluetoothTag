package com.example.bluetoothtag;

import android.app.Activity;
import android.os.Bundle;

import com.firebase.client.Firebase;

public class GamePlayingActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Firebase ref = new Firebase("https://blistering-fire-1807.firebaseio.com/");
		
	}
}
