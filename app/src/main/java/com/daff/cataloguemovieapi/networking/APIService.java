package com.daff.cataloguemovieapi.networking;

import com.daff.cataloguemovieapi.model.movie.ResponseMovie;
import com.daff.cataloguemovieapi.model.tvshow.TvShow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("movie/now_playing")
    Call<ResponseMovie> getAllMovie(@Query("api_key") String api_key,
                                    @Query("language") String language);

    @GET("tv/top_rated")
    Call<TvShow> getAllTvShow(@Query("api_key") String api_key,
                              @Query("language") String language);

    @GET("search/movie")
    Call<ResponseMovie> getSearchMovie(
            @Query("query") String query,
            @Query("api_key") String api_key
    );

    @GET("search/tv")
    Call<TvShow> getSearchTv(
            @Query("query") String query,
            @Query("api_key") String api_key
    );

    @GET("discover/movie")
    Call<ResponseMovie> getReleaseToday(
            @Query("api_key") String api_key,
            @Query("primary_release_date.gte") String date,
            @Query("primary_release_date.lte") String mDate);
}
