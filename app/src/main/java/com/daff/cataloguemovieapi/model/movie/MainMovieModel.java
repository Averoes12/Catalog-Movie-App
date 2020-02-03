package com.daff.cataloguemovieapi.model.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.daff.cataloguemovieapi.BuildConfig;
import com.daff.cataloguemovieapi.networking.APIService;
import com.daff.cataloguemovieapi.networking.ConfigRetrofit;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMovieModel extends ViewModel {

    private MutableLiveData<List<ResultsItem>> itemMovie = new MutableLiveData<>();
    private APIService service;

    public LiveData<List<ResultsItem>> getList() {
        return itemMovie;
    }

    public void setList() {

        service = ConfigRetrofit.getClient().create(APIService.class);
        service.getAllMovie(BuildConfig.API_KEY, "en-US").enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NotNull Call<ResponseMovie> call, @NotNull Response<ResponseMovie> response) {

                if (response.isSuccessful()){
                    itemMovie.postValue(Objects.requireNonNull(response.body()).getResults());
                    Log.d("RESPONSE", "SUCCES");
                }else {
                    Log.d("RESPONSE", "FAILED TO FETCH DATA");
                }


            }

            @Override
            public void onFailure(Call<ResponseMovie> call, Throwable t) {
                Log.e("RESPONSE", "ERROR");
               t.printStackTrace();
            }
        });
    }
}
