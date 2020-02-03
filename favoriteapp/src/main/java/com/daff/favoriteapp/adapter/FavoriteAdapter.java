package com.daff.favoriteapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daff.favoriteapp.DetailActivity;
import com.daff.favoriteapp.R;
import com.daff.favoriteapp.model.FavoriteResult;

import java.util.ArrayList;
import java.util.List;

import static com.daff.favoriteapp.database.DatabaseConstract.FavoriteColumns.CONTENT_URI;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Holder> {

    List<FavoriteResult> list = new ArrayList<>();
    Context context;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    public void setMovieResult(List<FavoriteResult> movieResult) {
        this.list.clear();
        this.list.addAll(movieResult);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        holder.bind(list.get(position), holder.itemView.getContext());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Are you sure ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.itemView.getContext().getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + list.get(position).getId())
                                        ,null, null);
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, list.size());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView title, popularity, date;
        ImageView img;
        ImageButton delete;

        public Holder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_item);
            title = itemView.findViewById(R.id.name_item);
            popularity = itemView.findViewById(R.id.popularity_item);
            date = itemView.findViewById(R.id.date_item);
            delete = itemView.findViewById(R.id.btn_del);
        }
        void bind(final FavoriteResult item, final Context context) {
            title.setText(item.getOriginalTitle());
            popularity.setText((String.format(context.getResources().getString(R.string.vote) + " %s", item.getVoteAverage())));
            date.setText(String.format(context.getResources().getString(R.string.release_date) + " %s", item.getReleaseDate()));

            String imgUrl = "https://image.tmdb.org/t/p/w185/" + item.getPosterPath();
            Glide.with(itemView.getContext()).load(imgUrl)
                    .override(512, 512)
                    .into(img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("item_favorite", item);
                    context.startActivity(intent);
                }
            });
        }
    }
}
