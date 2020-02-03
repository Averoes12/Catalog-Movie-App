package com.daff.cataloguemovieapi.model.tvshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.daff.cataloguemovieapi.networking.APIService;
import com.daff.cataloguemovieapi.networking.ConfigRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.daff.cataloguemovieapi.BuildConfig.API_KEY;

public class MainTvShowModel extends ViewModel {

    private MutableLiveData<List<com.daff.cataloguemovieapi.model.tvshow.ResultsItem>> itemMovie = new MutableLiveData<>();
    private APIService service;

    public LiveData<List<ResultsItem>> getList() {
        return itemMovie;
    }
    public void setListTv(){
        service = ConfigRetrofit.getClient().create(APIService.class);
        service.getAllTvShow(API_KEY, "en-ID")
                .enqueue(new Callback<TvShow>() {
                    @Override
                    public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                        if (response.isSuccessful()){
                            itemMovie.postValue(response.body().getResults());
                            Log.d("RESPONSETV", "SUCCES");
                        }else {
                            Log.d("RESPONSETV", "FAILED TO FETCH DATA");
                        }
                    }

                    @Override
                    public void onFailure(Call<TvShow> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
