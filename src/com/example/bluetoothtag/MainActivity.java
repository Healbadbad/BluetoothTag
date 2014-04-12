package com.example.bluetoothtag; 
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
 
public class MainActivity extends Activity {
	
	protected static final String TAG = "com.dagwaging.bluetag.MainActivity";
 
	private static final int REQUEST_DISCOVERABLE = 1;
 
	private BroadcastReceiver receiver;
	
	private BluetoothAdapter bluetoothAdapter;
	
	private Timer timer;
	
	private ListView deviceList;
	
	private HashMapter<String, String> devices;
	
	private HashMap<String, Timer> expirations;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_DISCOVERABLE) {
			if(resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		devices = new HashMapter<String, String>(this, android.R.layout.activity_list_item);
		
		//deviceList = (ListView) findViewById(R.id.device_list);
		//deviceList.setAdapter(devices);
		
		//deviceList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		//deviceList.setOnItemClickListener(this);
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
			
			startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
		}
		
		expirations = new HashMap<String, Timer>();
		
		receiver = new BroadcastReceiver() {
			@SuppressLint("InlinedApi")
			@Override
			public void onReceive(Context context, Intent intent) {
				final String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
				final Short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
				if(devices.getCount() > 1)Log.d("bluetoothtag","its happening" + name + rssi);
				devices.put(name, name);
				
				Timer timer = expirations.get(name);
				
				if(timer != null) {
					timer.cancel();
					timer.purge();
				}
				
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								devices.remove(name);
							}
						});
					}
				}, 5000);
				
				expirations.put(name, timer);
			}
		};
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
 
	@Override
	protected void onPause() {
		super.onPause();
		
		timer.cancel();
		
		if(bluetoothAdapter.isDiscovering())
			bluetoothAdapter.cancelDiscovery();
		
		unregisterReceiver(receiver);
	}
 
	@Override
	protected void onResume() {
		super.onResume();
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(bluetoothAdapter.isDiscovering())
					bluetoothAdapter.cancelDiscovery();
				
				bluetoothAdapter.startDiscovery();
			}
		}, 0, 1000);
		
		registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
	}
 
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		bluetoothAdapter.cancelDiscovery();
	}
	
	public void sendGameRequest(View view) {
		Intent intent = new Intent(this, GameChooserActivity.class);
		startActivity(intent);
	}
	
	public void findGameRequest(View view) {
		System.out.println("AAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHH");
	}
 
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//			deviceList.clearChoices();
//	}
}