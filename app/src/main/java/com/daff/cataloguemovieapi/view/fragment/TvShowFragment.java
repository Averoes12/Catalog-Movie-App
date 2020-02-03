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
import com.daff.cataloguemovieapi.adapter.TvShowAdapter;
import com.daff.cataloguemovieapi.model.tvshow.MainTvShowModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    RecyclerView listMovie;
    ProgressBar loading;
    TvShowAdapter adapter;
    MainTvShowModel viewModel;


    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listMovie = view.findViewById(R.id.list_tvshow);
        loading = view.findViewById(R.id.loading_tv);

    }

    void initView() {

        listMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        listMovie.setHasFixedSize(true);
        listMovie.setItemAnimator(new DefaultItemAnimator());
        listMovie.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new TvShowAdapter();
        adapter.notifyDataSetChanged();
        viewModel = ViewModelProviders.of(this).get(MainTvShowModel.class);
        viewModel.setListTv();
        viewModel.getList().observe(this, tvShow ->{
            loading.setVisibility(View.GONE);
            adapter.setTvList(tvShow);
            adapter.notifyDataSetChanged();
        });
        initView();
    }
}
