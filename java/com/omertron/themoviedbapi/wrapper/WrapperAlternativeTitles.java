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
package com.omertron.themoviedbapi.wrapper;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.omertron.themoviedbapi.model.AlternativeTitle;

/**
 *
 * @author Stuart
 */
public class WrapperAlternativeTitles {
    /*
     * Logger
     */

    private static final Logger logger = Logger.getLogger(WrapperAlternativeTitles.class);
    /*
     * Properties
     */
    @JsonProperty("id")
    private int id;
    @JsonProperty("titles")
    private List<AlternativeTitle> titles;

    public int getId() {
        return id;
    }

    public List<AlternativeTitle> getTitles() {
        return titles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitles(List<AlternativeTitle> titles) {
        this.titles = titles;
    }

    /**
     * Handle unknown properties and print a message
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
}
