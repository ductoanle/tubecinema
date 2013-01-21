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
import com.ethan.globalcinema.beans.YoutubeVideo;
import com.ethan.globalcinema.cache.SuperImageLoader;

public class YoutubeVideoAdapter extends ArrayAdapter<YoutubeVideo>{
private static final String TAG = "YoutubeVideoAdapter";
    
    private LayoutInflater mLayoutInflater;

    private final List<YoutubeVideo> videoList;
    
    public YoutubeVideoAdapter(Context context, List<YoutubeVideo> arrayList) {
        super(context, 0, arrayList);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.videoList = arrayList;
    }

    /////////////// Inner classes ///////////////

    private static class ViewHolder {
        public ImageView thumb;
        public TextView title;
        public TextView duration;
    }
    
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        YoutubeVideo item = videoList.get(position);
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.youtube_video_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.thumb = (ImageView)convertView.findViewById(R.id.youtube_video_thumb);
            holder.title = (TextView)convertView.findViewById(R.id.youtube_video_title);
            holder.duration = (TextView)convertView.findViewById(R.id.youtube_video_duration);
            convertView.setTag(R.string.tab_channel_videos ,holder);
        }
        ViewHolder holder = (ViewHolder)convertView.getTag(R.string.tab_channel_videos); 

        // Set view content
        SuperImageLoader.getInstance(getContext()).loadImage(item.getThumbnail(), holder.thumb, 0, ScaleType.FIT_CENTER, 100000);
        holder.title.setText(item.getTitle());
        return convertView;
    }
}
