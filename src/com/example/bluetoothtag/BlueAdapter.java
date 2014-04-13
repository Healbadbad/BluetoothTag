package com.example.bluetoothtag;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BlueAdapter extends ArrayAdapter<HashMap<String, Short>> {
	private Context ctx;
	HashMap<String,Short> hashdevices;
	ArrayList<String> devices;
	public BlueAdapter(Context context, int textViewResourceId,
			HashMap<String, Short> devicesi) {
		// let android do the initializing :)
		super(context, textViewResourceId);
		this.ctx = context;
		this.hashdevices = devicesi;
		for(String s:hashdevices.keySet()){
			devices.add(s+":"+this.hashdevices.get(s).toString());
		}
		
	}

	// class for caching the views in a row
	private class ViewHolder {
//		ImageView photo;
		TextView name, signal;

	}

	ViewHolder viewHolder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (convertView == null) {
			// inflate the custom layout
			LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.devices_layout, null);
			viewHolder = new ViewHolder();
			
			// cache the views
			// viewHolder.photo=(ImageView)
			// convertView.findViewById(R.id.photo);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.signal = (TextView) convertView.findViewById(R.id.signal);
			// link the cached views to the convertview
			convertView.setTag(viewHolder);

		} else
			viewHolder = (ViewHolder) convertView.getTag();

//		int photoId = (Integer) players.get(position).get("photo");

		// set the data to be displayed
//		viewHolder.photo.setImageDrawable(getResources().getDrawable(photoId));
		viewHolder.name.setText(this.devices.get(position).split(":")[0]);
		viewHolder.signal.setText(this.devices.get(position).split(":")[1]);

		// return the view to be displayed
		return convertView;
	}
}
