package com.ethan.globalcinema.api;

import java.net.URL;
import java.util.List;

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
	
	public List<MovieDb> getPopularMovieList(String page) throws MovieDbException{
		List<MovieDb> moviesDb = api.getTopRatedMovies(DEFAULT_LANGUAGE, Integer.parseInt(page));
		return moviesDb;
	}
	
	public List<MovieDb> searchMovies(String movieName, String page) throws MovieDbException{
		List<MovieDb> moviesDb = api.searchMovie(movieName, DEFAULT_SEARCH_YEAR, DEFAULT_LANGUAGE, DEFAULT_INCLUDE_ADULT, Integer.parseInt(page));
		return moviesDb;
	}
	
	public MovieDb getMovieInfo(int movieId) throws MovieDbException{
		MovieDb movieDb = api.getMovieInfo(movieId, DEFAULT_LANGUAGE);
		return movieDb;
	}
	
	public URL createThumbnailPoster(String path) throws MovieDbException{
		return api.createImageUrl(path, api.getConfiguration().getPosterSizes().get(1));
	}
	
	public URL createFullPoster(String path) throws MovieDbException{
		return api.createImageUrl(path, api.getConfiguration().getPosterSizes().get(3));
	}
}
