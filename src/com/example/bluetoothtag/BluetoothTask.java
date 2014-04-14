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
import android.view.Menu;
import android.view.MenuItem;

public class BluetoothTask extends
		AsyncTask<Context, HashMap<String, Short>, Integer> {

	Activity parent;

	private BroadcastReceiver receiver;

	private BluetoothAdapter bluetoothAdapter;

	private static final int REQUEST_DISCOVERABLE = 1;

	private Timer timer;

	HashMap<String, Short> devices;

	private HashMap<String, Timer> expirations;

	Context context;

	@Override
	protected Integer doInBackground(Context... contextarray) {
		context = contextarray[0];
		// TODO Auto-generated method stub
		devices = new HashMap<String, Short>();// this,
												// android.R.layout.activity_list_item

		// deviceList = (ListView) findViewById(R.id.device_list);
		// deviceList.setAdapter(devices);

		// deviceList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		// deviceList.setOnItemClickListener(this);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		expirations = new HashMap<String, Timer>();
		Log.d("bluetoothtag","initialized the asynctask");
		receiver = new BroadcastReceiver() {
			@SuppressLint("InlinedApi")
			@Override
			public void onReceive(Context context, Intent intent) {
				final String name = intent
						.getStringExtra(BluetoothDevice.EXTRA_NAME);// XTRA_Device
																	// intent.getparseableextra
				final Short rssi = intent.getShortExtra(
						BluetoothDevice.EXTRA_RSSI, (short) 0);
				if (devices.size() > 1)
					Log.d("bluetoothtag", "Async its happening" + name + rssi);
				devices.put(name, rssi);

				Timer timer = expirations.get(name);

				if (timer != null) {
					timer.cancel();
					timer.purge();
				}

				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {

						devices.remove(name);
					}
				}, 5000);

				expirations.put(name, timer);
			}
		};
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Log.i("bluetoothtag","in run");
				if (bluetoothAdapter.isDiscovering())
					bluetoothAdapter.cancelDiscovery();

				bluetoothAdapter.startDiscovery();
				//publishProgress(devices);
			}
		}, 0, 1000);
		Log.d("bluetoothtag","started timers");
		context.registerReceiver(receiver, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
		Log.d("bluetoothtag","registered receiver");
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeException e){
			//
		}
		Log.d("bluetoothtag","initialized loops");
		return null;

		// bluetoothAdapter.cancelDiscovery();

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		Log.w("bluetoothtag","cancelling asynctask");
		timer.cancel();
		context.unregisterReceiver(receiver);
		if (bluetoothAdapter.isDiscovering())
			bluetoothAdapter.cancelDiscovery();

	}

	protected void onProgressUpdate(HashMap<String, Short>... progress) {

		// Log.d("bluetoothtag",""+parent.getClass() + "   " +
		// GameChooserActivity.class);
		Log.d("bluetoothtag","sending bluetooth list");
		if (parent.getClass().equals(GameJoinActivity.class)) ((GameJoinActivity) parent).setdevices(progress[0]);
//			Log.d("bluetoothtag", "" + parent.getClass() + "   "
//					+ GameChooserActivity.class);
		Log.d("bluetoothtag","sent the device list data");

	}

}
