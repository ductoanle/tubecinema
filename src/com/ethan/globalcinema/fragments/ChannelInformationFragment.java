package com.ethan.globalcinema.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.globalcinema.ChannelLoadActivity;
import com.ethan.globalcinema.R;
import com.ethan.globalcinema.cache.SuperImageLoader;
import com.omertron.themoviedbapi.model.MovieDb;

public class ChannelInformationFragment extends Fragment{

	private static final String TAG = "ChannelInformationFragment";
	
	private MovieDb movie;
	private ImageView poster;
	private TextView title;
	private TextView originalTitle;
	private TextView languages;
	private TextView runtime;
	private TextView tagLine;
	private TextView genres;
	private TextView overview;
	private View movieInformation;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		movie = ((ChannelLoadActivity)getActivity()).getMovie();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.channel_information, container, false);
		movieInformation = (View)root.findViewById(R.id.movie_information);
		poster = (ImageView)root.findViewById(R.id.movie_poster);
		title = (TextView)root.findViewById(R.id.movie_title);
		originalTitle = (TextView)root.findViewById(R.id.movie_original_title);
		languages = (TextView)root.findViewById(R.id.movie_spoken_languages);
		runtime = (TextView)root.findViewById(R.id.movie_runtime);
		tagLine = (TextView)root.findViewById(R.id.movie_tag_line);
		genres = (TextView)root.findViewById(R.id.movie_genres);
		overview = (TextView)root.findViewById(R.id.movie_overview);
		renderInformation();
		return root;
    }
	
	private void renderInformation(){
		title.setText(movie.getTitle());
		if (!TextUtils.isEmpty(movie.getOriginalTitle())){
			originalTitle.setText(movie.getOriginalTitle());
		}
		else{
			originalTitle.setVisibility(View.GONE);
		}
		languages.setText(movie.getSpokenLanguagesListAsText());
		runtime.setText(movie.getRuntimeAsHourMinFormat());
		tagLine.setText(movie.getTagline());
		genres.setText(movie.getGenresListAsText());
		overview.setText(movie.getOverview());
		SuperImageLoader.getInstance(getActivity()).loadImage(movie.getPosterFull(), poster, 0, null, 0);
		movieInformation.setVisibility(View.VISIBLE);
	}
}
