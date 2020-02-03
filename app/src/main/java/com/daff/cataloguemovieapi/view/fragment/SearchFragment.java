package com.daff.cataloguemovieapi.view.fragment;


import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.adapter.MovieAdapter;
import com.daff.cataloguemovieapi.adapter.TvShowAdapter;
import com.daff.cataloguemovieapi.model.search.SearchViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    SearchView searchTitle;
    RecyclerView listMovie, listTv;
    ProgressBar loadingMovie, loadingTv;
    SearchViewModel searchViewModel;
    TextView movie, tv;

    MovieAdapter movieAdapter;
    TvShowAdapter tvShowAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchTitle = view.findViewById(R.id.search_title);
        listMovie = view.findViewById(R.id.list_movie_search);
        listTv = view.findViewById(R.id.list_tv_search);
        loadingMovie = view.findViewById(R.id.loading_searchMovie);
        loadingTv = view.findViewById(R.id.loading_searchTv);
        movie = view.findViewById(R.id.movie);
        tv = view.findViewById(R.id.tv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        movieAdapter = new MovieAdapter();
        tvShowAdapter = new TvShowAdapter();

        final SnapHelper snapHelper = new PagerSnapHelper();

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.getList().observe(this, resultsItems -> {
            loadingMovie.setVisibility(View.GONE);
            movieAdapter.setItemMovie(resultsItems);
            listMovie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            listMovie.setAdapter(movieAdapter);
            listMovie.setHasFixedSize(true);
            snapHelper.attachToRecyclerView(listMovie);

        });

        searchViewModel.getListTv().observe(this, resultsItems -> {
            loadingTv.setVisibility(View.GONE);
            tvShowAdapter.setTvList(resultsItems);
            listTv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            listTv.setAdapter(tvShowAdapter);
            listTv.setHasFixedSize(true);
            snapHelper.attachToRecyclerView(listTv);

        });
        searchTitle.setQueryHint(getString(R.string.i_m_looking_for));
        searchTitle.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewModel.setSearchQuery(query);
                searchViewModel.setSearchQueryTv(query);

                movie.setText(String.format(getString(R.string.result_for_movie) + "%s", " '" + query + "' "));
                tv.setText(String.format(getString(R.string.result_for_Tv) + "%s", " '" + query + "' "));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
