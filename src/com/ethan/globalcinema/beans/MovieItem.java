package com.ethan.globalcinema.beans;

import java.net.URL;
import java.util.List;

import android.util.Log;

import com.ethan.globalcinema.api.TMDBApi;
import com.ethan.globalcinema.utils.Utils;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.Language;
import com.omertron.themoviedbapi.model.MovieDb;

public class MovieItem{
	
	private static final String TAG = "MovieItem";
	
	private MovieDb movieDb;
	
	private double imdbRanking;
	private int imdbRating;
	
	public MovieItem(MovieDb movieDb){
		this.movieDb = movieDb;
	}
	
	public String getGenresListAsText(){
		String genres = "";
		List<Genre> genreList = movieDb.getGenres();
		if (Utils.isListValid(genreList)){
			for (int i = 0; i < genreList.size(); i++){
				genres += movieDb.getGenres().get(i).getName();
				if (i < genreList.size() - 1){
					genres += ", ";
				}
			}
		}
		
		return genres;
	}
	
	public String getSpokenLanguagesListAsText(){
		String spokenLanguages = "";
		List<Language> languages = movieDb.getSpokenLanguages();
		if (Utils.isListValid(languages)){
			for (int i = 0; i < languages.size(); i++){
				spokenLanguages += languages.get(i).getName();
				if (i < languages.size() - 1){
					spokenLanguages += ", ";
				}
			}
		}
		return spokenLanguages;
	}
	
	public String getTitle(){
		return movieDb.getTitle();
	}
	
	public String getOriginalTitle(){
		if (!movieDb.getOriginalTitle().equals(movieDb.getTitle())){
			return movieDb.getOriginalTitle();
		}
		return "";
	}
	
	public String getReleasedDate(){
		return movieDb.getReleaseDate();
	}
	
	public String getReleasedYear(){
		return movieDb.getReleaseDate().substring(0,4);
	}
	
	public double getRating(){
		return movieDb.getVoteAverage();
	}
	
	public String getPosterThumb(){
		try{
			URL url = TMDBApi.getInstance().createThumbnailPoster(getPosterPath());
			return url.toString();
		}
		catch (Exception e){
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}
	
	public String getPosterFull(){
		try{
			URL url = TMDBApi.getInstance().createFullPoster(getPosterPath());
			return url.toString();
		}
		catch (Exception e){
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}
	
	public String getPosterPath(){
		return movieDb.getPosterPath();
	}
	
	public String getRuntimeAsHourMinFormat(){
		int hour = getRuntime() / 60;
		int min = getRuntime() % 60;
		if (hour > 0){
			return hour + " hours " + min + " mins";
		}
		else{
			return min + "mins";
		}
	}
	
	public int getRuntime(){
		return movieDb.getRuntime();
	}
	
	public String getTagLine(){
		return movieDb.getTagline();
	}
	
	public String getOverview(){
		return movieDb.getOverview();
	}
	
	public int getId(){
		return movieDb.getId();
	}
}
