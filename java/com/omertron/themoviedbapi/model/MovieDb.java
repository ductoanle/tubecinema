/*
 *      Copyright (c) 2004-2013 Stuart Boston
 *
 *      This file is part of TheMovieDB API.
 *
 *      TheMovieDB API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      TheMovieDB API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with TheMovieDB API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.omertron.themoviedbapi.model;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.ethan.globalcinema.api.TMDBApi;
import com.ethan.globalcinema.utils.Utils;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieDb implements Serializable, Parcelable {
	
	private static final String TAG = "MovieDb";
    private static final long serialVersionUID = 1L;
    /*
     * Logger
     */
    private static final Logger logger = Logger.getLogger(MovieDb.class);
    /*
     * Properties
     */
    @JsonProperty(("backdrop_path"))
    private String backdropPath;
    @JsonProperty(("id"))
    private int id;
    @JsonProperty(("original_title"))
    private String originalTitle;
    @JsonProperty(("popularity"))
    private float popularity;
    @JsonProperty(("poster_path"))
    private String posterPath;
    @JsonProperty(("release_date"))
    private String releaseDate;
    @JsonProperty(("title"))
    private String title;
    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("belongs_to_collection")
    private Collection belongsToCollection;
    @JsonProperty("budget")
    private long budget;
    @JsonProperty("genres")
    private List<Genre> genres;
    @JsonProperty("homepage")
    private String homepage;
    @JsonProperty("imdb_id")
    private String imdbID;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies;
    @JsonProperty("production_countries")
    private List<ProductionCountry> productionCountries;
    @JsonProperty("revenue")
    private long revenue;
    @JsonProperty("runtime")
    private int runtime;
    @JsonProperty("spoken_languages")
    private List<Language> spokenLanguages;
    @JsonProperty("tagline")
    private String tagline;
    @JsonProperty("vote_average")
    private float voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;
    @JsonProperty("status")
    private String status;
    
    public MovieDb(){}
    
    public MovieDb(Parcel in){
    	super();
    	readFromParcel(in);
    }
    
    public String getGenresListAsText(){
		String genres = "";
		List<Genre> genreList = getGenres();
		if (Utils.isListValid(genreList)){
			for (int i = 0; i < genreList.size(); i++){
				genres += getGenres().get(i).getName();
				if (i < genreList.size() - 1){
					genres += ", ";
				}
			}
		}
		
		return genres;
	}
	
	public String getSpokenLanguagesListAsText(){
		String spokenLanguages = "";
		List<Language> languages = getSpokenLanguages();
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
	
	public String getReleasedYear(){
		return StringUtils.substring(releaseDate, 0, 4);
	}
	
	public String getOriginalTitleText(){
		if (originalTitle.equals(title)){
			return originalTitle;
		}
		return "";
	}
	
	public String getRuntimeAsHourMinFormat(){
		int hour = runtime / 60;
		int min = runtime % 60;
		if (hour > 0){
			return hour + " hours " + min + " mins";
		}
		else{
			return min + "mins";
		}
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

    // <editor-fold defaultstate="collapsed" desc="Getter methods">
    public String getBackdropPath() {
        return backdropPath;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAdult() {
        return adult;
    }

    public Collection getBelongsToCollection() {
        return belongsToCollection;
    }

    public long getBudget() {
        return budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getOverview() {
        return overview;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public long getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<Language> getSpokenLanguages() {
        return spokenLanguages;
    }

    public String getTagline() {
        return tagline;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getStatus() {
        return status;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Setter methods">
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setBelongsToCollection(Collection belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public void setProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setSpokenLanguages(List<Language> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // </editor-fold>

    /**
     * Handle unknown properties and print a message
     *
     * @param key
     * @param value
     */
    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown property: '").append(key);
        sb.append("' value: '").append(value).append("'");
        logger.trace(sb.toString());
    }

    //<editor-fold defaultstate="collapsed" desc="Equals and HashCode">
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovieDb other = (MovieDb) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.imdbID == null) ? (other.imdbID != null) : !this.imdbID.equals(other.imdbID)) {
            return false;
        }
        if (this.runtime != other.runtime) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.id;
        hash = 89 * hash + (this.imdbID != null ? this.imdbID.hashCode() : 0);
        hash = 89 * hash + this.runtime;
        return hash;
    }
    //</editor-fold>

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[MovieDB=");
        sb.append("[backdropPath=").append(backdropPath);
        sb.append("],[id=").append(id);
        sb.append("],[originalTitle=").append(originalTitle);
        sb.append("],[popularity=").append(popularity);
        sb.append("],[posterPath=").append(posterPath);
        sb.append("],[releaseDate=").append(releaseDate);
        sb.append("],[title=").append(title);
        sb.append("],[adult=").append(adult);
        sb.append("],[belongsToCollection=").append(belongsToCollection);
        sb.append("],[budget=").append(budget);
        sb.append("],[genres=").append(genres);
        sb.append("],[homepage=").append(homepage);
        sb.append("],[imdbID=").append(imdbID);
        sb.append("],[overview=").append(overview);
        sb.append("],[productionCompanies=").append(productionCompanies);
        sb.append("],[productionCountries=").append(productionCountries);
        sb.append("],[revenue=").append(revenue);
        sb.append("],[runtime=").append(runtime);
        sb.append("],[spokenLanguages=").append(spokenLanguages);
        sb.append("],[tagline=").append(tagline);
        sb.append("],[voteAverage=").append(voteAverage);
        sb.append("],[voteCount=").append(voteCount);
        sb.append("],[status=").append(status);
        sb.append("]]");
        return sb.toString();
    }
    
    @Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flag) {
		out.writeInt(this.id);
		out.writeString(this.title);
		out.writeString(this.originalTitle);
		out.writeFloat(this.popularity);
		out.writeString(this.posterPath);
		out.writeString(this.releaseDate);
		out.writeByte((byte) (adult ? 1 : 0));
		out.writeLong(this.budget);
		out.writeString(this.homepage);
		out.writeString(this.imdbID);
		out.writeString(this.overview);
		out.writeLong(this.revenue);
		out.writeInt(this.runtime);
		out.writeString(this.tagline);
		out.writeFloat(this.voteAverage);
		out.writeInt(this.voteCount);
		out.writeString(this.status);
		out.writeString(this.backdropPath);
		out.writeList(this.productionCompanies);
		out.writeList(this.productionCountries);
		out.writeList(this.genres);
		out.writeList(this.spokenLanguages);
	}

	public void readFromParcel(Parcel in){
		this.id = in.readInt();
		this.title = in.readString();
		this.originalTitle = in.readString();
		this.popularity = in.readFloat();
		this.posterPath = in.readString();
		this.releaseDate = in.readString();
		this.adult = in.readByte() == 1 ? true: false;
		this.budget = in.readLong();
		this.homepage = in.readString();
		this.imdbID = in.readString();
		this.overview = in.readString();
		this.revenue = in.readLong();
		this.runtime = in.readInt();
		this.tagline = in.readString();
		this.voteAverage = in.readFloat();
		this.voteCount = in.readInt();
		this.status = in.readString();
		this.backdropPath = in.readString();
		this.productionCompanies = new ArrayList<ProductionCompany>();
		in.readList(productionCompanies,null);
		this.productionCountries = new ArrayList<ProductionCountry>();
		in.readList(productionCountries,null);
		this.genres = new ArrayList<Genre>();
		in.readList(genres, null);
		this.spokenLanguages = new ArrayList<Language>();
		in.readList(spokenLanguages, null);
	}

	public static final Parcelable.Creator<MovieDb> CREATOR = new Parcelable.Creator<MovieDb>() {
		public MovieDb createFromParcel(Parcel in) {
			return new MovieDb(in);
		}

		public MovieDb[] newArray(int size) {
			return new MovieDb[size];
		}
	};

}
