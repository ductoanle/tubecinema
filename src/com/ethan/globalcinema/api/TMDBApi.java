package com.ethan.globalcinema.api;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.util.Log;

import com.ethan.globalcinema.beans.MovieItem;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.MovieDb;

public class TMDBApi {
	private static final String TAG = "TMDBApi";
	
	private static final String APIKEY = "b39ce5553326dbd9834997a533e23d05";
	private static final int DEFAULT_SEARCH_YEAR = 0;
	private static final boolean DEFAULT_INCLUDE_ADULT = true;
	private static final String DEFAULT_LANGUAGE = null;
	
	public static TMDBApi instance = null;
	
	private TheMovieDbApi api;
	
	private TMDBApi() throws MovieDbException{
		api = new TheMovieDbApi(APIKEY);
	}
	
	public static TMDBApi getInstance() throws MovieDbException{
		if (instance == null){
			instance = new TMDBApi();
		}
		return instance;
	}
	
	public List<MovieItem> getPopularMovieList(int page) throws MovieDbException{
		List<MovieDb> moviesDb = api.getTopRatedMovies(DEFAULT_LANGUAGE, page);
		return convertToMovieItem(moviesDb);
	}
	
	public List<MovieItem> searchMovies(String movieName, int page) throws MovieDbException{
		List<MovieDb> moviesDb = api.searchMovie(movieName, DEFAULT_SEARCH_YEAR, DEFAULT_LANGUAGE, DEFAULT_INCLUDE_ADULT, page);
		return convertToMovieItem(moviesDb);
	}
	
	public MovieItem getMovieInfo(int movieId) throws MovieDbException{
		MovieDb movieDb = api.getMovieInfo(movieId, DEFAULT_LANGUAGE);
		if (movieDb != null){
			Log.i(TAG, "movie info is "  + movieDb);
			return new MovieItem(movieDb);
		}
		return null;
	}
	
	public URL createThumbnailPoster(String path) throws MovieDbException{
		return api.createImageUrl(path, api.getConfiguration().getPosterSizes().get(1));
	}
	
	public URL createFullPoster(String path) throws MovieDbException{
		return api.createImageUrl(path, api.getConfiguration().getPosterSizes().get(3));
	}
	
	private List<MovieItem> convertToMovieItem(List<MovieDb> moviesDb){
		List<MovieItem> movies = new ArrayList<MovieItem>();
		for (MovieDb movie: moviesDb){
			if (movie != null){
				movies.add(new MovieItem(movie));
			}
		}
		return movies;
	}
}
