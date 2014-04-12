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
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
 
public class LocalLoader extends AsyncTask {
	
	Activity parent;
	
	protected static final String TAG = "com.example.bluetoothtag.MainActivity";
 
	private static final int REQUEST_DISCOVERABLE = 1;
 
	private BroadcastReceiver receiver;
	
	private BluetoothAdapter bluetoothAdapter;
	
	private Timer timer;
	
	private ListView deviceList;
	
	private HashMap<String, Short> devices;
	
	private HashMap<String, Timer> expirations;
	
	@Override
	protected Object doInBackground(Object... params) {
		
//		devices = new HashMapter<String, String>(this, android.R.layout.activity_list_item);
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
			
//			startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
		}
		
		expirations = new HashMap<String, Timer>();
		
		receiver = new BroadcastReceiver() {
			@SuppressLint("InlinedApi")
			@Override
			public void onReceive(Context context, Intent intent) {
				final String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
				final Short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
//				final String mac = intent.getStringExtra(BluetoothDevice.);
//				if(devices.getCount() > 1)Log.d("bluetoothtag","its happening" + name + rssi);
				devices.put(name, rssi);
				
				Timer timer = expirations.get(name);
				
				if(timer != null) {
					timer.cancel();
					timer.purge();
				}
				
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
//						runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
								devices.remove(name);
//							}
						};//)
					//}
				}, 5000);
				
				expirations.put(name, timer);
			}
		};
		
		
		return null;
	}
 
	@Override
	protected void onPause() {
		super.onPause();
		
		timer.cancel();
		
		if(bluetoothAdapter.isDiscovering())
			bluetoothAdapter.cancelDiscovery();
		
		parent.unregisterReceiver(receiver);
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
		
		parent.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
	}
 	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		bluetoothAdapter.cancelDiscovery();
	}
}