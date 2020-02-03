package com.daff.cataloguemovieapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.model.movie.ResultsItem;
import com.daff.cataloguemovieapi.view.activity.FavoriteDetail;

import java.util.ArrayList;

import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.CONTENT_URI;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Holder> {

    private ArrayList<ResultsItem> listFavorite = new ArrayList<>();
    private AppCompatActivity context;

    public FavoriteAdapter(AppCompatActivity context) {
        this.context = context;
    }

    public ArrayList<ResultsItem> getListFavorite() {
        return listFavorite;
    }

    public void setListFavorite(ArrayList<ResultsItem> listFavorite) {
        this.listFavorite.addAll(listFavorite);
        notifyItemInserted(this.listFavorite.size() -1);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.listFavorite.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listFavorite.size());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_search, viewGroup, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(listFavorite.get(position), holder.itemView.getContext());
        holder.itemView.setOnClickListener((View v) -> {
            Intent detail = new Intent(context, FavoriteDetail.class);
            Uri uri = Uri.parse(CONTENT_URI + "/" + listFavorite.get(position).getId());
            detail.setData(uri);
            detail.putExtra("favorite", listFavorite.get(position));
            detail.putExtra("position", position);
            context.startActivityForResult(detail, 3);
        });

    }


    @Override
    public int getItemCount() {
        return listFavorite.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView title, popularity, date;
        ImageView img;

        public Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_fav);
            title = itemView.findViewById(R.id.name_fav);
            popularity = itemView.findViewById(R.id.popularity_fav);
            date = itemView.findViewById(R.id.date_fav);
        }

        void bind(ResultsItem item, Context context) {
            title.setText(item.getOriginalTitle());
            popularity.setText((String.format(context.getResources().getString(R.string.vote) + " %s", item.getVoteAverage())));
            date.setText(String.format(context.getResources().getString(R.string.release_date) + " %s", item.getReleaseDate()));

            String imgUrl = "https://image.tmdb.org/t/p/w185/" + item.getPosterPath();
            Glide.with(itemView.getContext()).load(imgUrl)
                    .override(512, 512)
                    .placeholder(R.drawable.ic_replay_black_24dp)
                    .into(img);


        }
    }
}
