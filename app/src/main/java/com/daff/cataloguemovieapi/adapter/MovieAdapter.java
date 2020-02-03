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
import com.daff.cataloguemovieapi.view.activity.MovieDetail;
import com.daff.cataloguemovieapi.model.movie.ResultsItem;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Holder> {

    private List<ResultsItem> itemMovie = new ArrayList<>();
    public void setItemMovie(List<ResultsItem> list) {
        itemMovie.clear();
        itemMovie.addAll(list);
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

        holder.bind(itemMovie.get(position),holder.itemView.getContext());

    }

    @Override
    public int getItemCount() {
        if (itemMovie == null) {
            return 0;
        } else {
            return itemMovie.size();
        }
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView title, popularity, date;
        ImageView img;

        public Holder(@NonNull View itemView) {
            super(itemView);


            img = itemView.findViewById(R.id.img_item);
            title = itemView.findViewById(R.id.name_item);
            popularity = itemView.findViewById(R.id.popularity_item);
            date = itemView.findViewById(R.id.date_item);


        }

        void bind(final ResultsItem item, Context context) {
            title.setText(item.getTitle());
            popularity.setText((String.format(context.getResources().getString(R.string.vote)+" %s", item.getVoteAverage())));
            date.setText(String.format(context.getResources().getString(R.string.release_date)+" %s", item.getReleaseDate()));

            String imgUrl = "https://image.tmdb.org/t/p/w185/" + item.getPosterPath();
            Glide.with(itemView.getContext()).load(imgUrl)
                    .override(512, 512)
                    .placeholder(R.drawable.ic_replay_black_24dp)
                    .into(img);

            itemView.setOnClickListener(v -> {
                Intent detail = new Intent(itemView.getContext(), MovieDetail.class);
                detail.putExtra("movie", item);
                itemView.getContext().startActivity(detail);
            });
        }

    }
}

