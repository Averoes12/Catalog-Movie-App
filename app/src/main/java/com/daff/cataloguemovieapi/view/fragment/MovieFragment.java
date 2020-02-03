package com.daff.cataloguemovieapi.view.fragment;


import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.adapter.MovieAdapter;
import com.daff.cataloguemovieapi.model.movie.MainMovieModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    RecyclerView listMovie;
    ProgressBar loading;
    MovieAdapter adapter;
    MainMovieModel viewModel;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_movie, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listMovie = view.findViewById(R.id.list_movie);
        loading = view.findViewById(R.id.loading);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();
        viewModel = ViewModelProviders.of(this).get(MainMovieModel.class);
        viewModel.setList();
        viewModel.getList().observe(this, getMovie -> {
            loading.setVisibility(View.GONE);
            adapter.setItemMovie(getMovie);
            adapter.notifyDataSetChanged();
        });
        showList();

    }


    void showList() {
        listMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        listMovie.setHasFixedSize(true);
        listMovie.setItemAnimator(new DefaultItemAnimator());
        listMovie.setAdapter(adapter);
    }

}
