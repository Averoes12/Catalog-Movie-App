package com.daff.cataloguemovieapi.model.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import com.daff.cataloguemovieapi.BuildConfig;
import com.daff.cataloguemovieapi.model.movie.ResponseMovie;
import com.daff.cataloguemovieapi.model.movie.ResultsItem;
import com.daff.cataloguemovieapi.model.tvshow.TvShow;
import com.daff.cataloguemovieapi.networking.APIService;
import com.daff.cataloguemovieapi.networking.ConfigRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

    public MutableLiveData<List<ResultsItem>> listMovie = new MutableLiveData<>();

    public LiveData<List<ResultsItem>> getList() {
        return listMovie;
    }

    public void setSearchQuery(String query) {
        APIService service = ConfigRetrofit.getClient().create(APIService.class);
        service.getSearchMovie(query, BuildConfig.API_KEY)
                .enqueue(new Callback<ResponseMovie>() {
                    @Override
                    public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
                        if (response.isSuccessful()){
                            listMovie.postValue(response.body().getResults());
                            Log.d("RESPONSE", "SUCCESS");
                        }else {
                            Log.d("RESPONSE", "FAILED TO FETCH DATA");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMovie> call, Throwable t) {
                        Log.e("RESPONSE", "ERROR");
                    }
                });
    }

    private MutableLiveData<List<com.daff.cataloguemovieapi.model.tvshow.ResultsItem>> listTv = new MutableLiveData<>();

    public LiveData<List<com.daff.cataloguemovieapi.model.tvshow.ResultsItem>> getListTv(){
        return listTv;
    }
    public void setSearchQueryTv(String queryTv){
        APIService service = ConfigRetrofit.getClient().create(APIService.class);
        service.getSearchTv(queryTv, BuildConfig.API_KEY)
                .enqueue(new Callback<TvShow>() {
                    @Override
                    public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                        if (response.isSuccessful()){
                            listTv.postValue(response.body().getResults());
                            Log.d("RESPONSE", "SUCCESS");
                        }else {
                            Log.d("RESPONSE", "FAILED TO FETCH DATA");
                        }
                    }

                    @Override
                    public void onFailure(Call<TvShow> call, Throwable t) {
                        Log.e("RESPONSE", "ERROR");
                    }
                });
    }
}
