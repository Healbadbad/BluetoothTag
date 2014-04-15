package com.example.bluetoothtag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameChooserActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamechooser);
	}
	public void startGame(View view) {
		Intent intent = new Intent(this, GameHostActivity.class);
		startActivity(intent);
	}

}
