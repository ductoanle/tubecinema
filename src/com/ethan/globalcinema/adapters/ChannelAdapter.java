package com.ethan.globalcinema.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.ethan.globalcinema.R;
import com.ethan.globalcinema.cache.SuperImageLoader;
import com.omertron.themoviedbapi.model.MovieDb;

public class ChannelAdapter extends ArrayAdapter<MovieDb> {
	private static final String TAG = "ChannelAdapter";
	
	private LayoutInflater mLayoutInflater;

	private final List<MovieDb> movieList;
	
	public ChannelAdapter(Context context, List<MovieDb> objects) {
		super(context, 0, objects);
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.movieList = objects;
	}

	/////////////// Inner classes ///////////////

	private static class ViewHolder {
		public ImageView thumb;
		public TextView title;
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
	    final int time_1_day = 86400;
	    MovieDb item = movieList.get(position);
		if (convertView == null){
			convertView = mLayoutInflater.inflate(R.layout.channel_item, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.thumb = (ImageView)convertView.findViewById(R.id.channel_thumb);
			holder.title = (TextView)convertView.findViewById(R.id.channel_name);
			convertView.setTag(R.string.browse_movies ,holder);
		}
		ViewHolder holder = (ViewHolder)convertView.getTag(R.string.browse_movies); 

		// Set view content
		SuperImageLoader.getInstance(getContext()).loadImage(item.getPosterThumb(), holder.thumb, R.color.white, ScaleType.FIT_CENTER, time_1_day);
		holder.title.setText(item.getTitle() + " (" + item.getReleasedYear() + ")");
		return convertView;
	}

}
