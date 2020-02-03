package com.daff.cataloguemovieapi.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.view.activity.TvShowDetail;
import com.daff.cataloguemovieapi.model.tvshow.ResultsItem;

import java.util.ArrayList;
import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.Holder> {

    private List<ResultsItem> tvList = new ArrayList<>();

    public void setTvList(List<ResultsItem> list){
        tvList.clear();
        tvList.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(tvList.get(position), holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return tvList.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView title, popularity, date;
        ImageView img;

        public Holder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_item);
            title = itemView.findViewById(R.id.name_item);
            popularity = itemView.findViewById(R.id.popularity_item);
            date = itemView.findViewById(R.id.date_item);

        }
        void bind(final ResultsItem item, Context context){
            title.setText(item.getName());
            popularity.setText((String.format(context.getResources().getString(R.string.vote)+" %s",item.getVoteAverage())));
            date.setText(String.format(context.getResources().getString(R.string.release_date)+" %s", item.getFirstAirDate()));

            String imgUrl = "https://image.tmdb.org/t/p/w185/" + item.getPosterPath();
            Glide.with(itemView.getContext()).load(imgUrl)
                    .override(512, 512)
                    .placeholder(R.drawable.ic_replay_black_24dp)
                    .into(img);

            itemView.setOnClickListener(v -> {
                Intent detail = new Intent(itemView.getContext(), TvShowDetail.class);
                detail.putExtra("tvshow", item);
                itemView.getContext().startActivity(detail);
            });
        }
    }
}
