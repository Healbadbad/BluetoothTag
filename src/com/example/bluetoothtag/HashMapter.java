package com.example.bluetoothtag;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class HashMapter<T, V> extends BaseAdapter {
	private ArrayMap<T, V> map;
	
	private LayoutInflater layoutInflater;
	
	private int resource;
	
	public HashMapter(Context context, int resource) {
		super();
		
		layoutInflater = LayoutInflater.from(context);
		
		this.resource = resource;
		
		map = new ArrayMap<T, V>();
	}
 
	@Override
	public int getCount() {
		return map.size();
	}
	
	public void put(T key, V value) {
		map.put(key, value);
		
		notifyDataSetChanged();
	}
	
	public void remove(T key) {
		map.remove(key);
		
		notifyDataSetChanged();
	}
 
	@Override
	public Object getItem(int position) {
		return map.valueAt(position);
	}
 
	@Override
	public long getItemId(int position) {
		return position;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = layoutInflater.inflate(resource, null);
		
		((TextView) convertView).setText(getItem(position).toString());
		
		return convertView;
	}
 
}