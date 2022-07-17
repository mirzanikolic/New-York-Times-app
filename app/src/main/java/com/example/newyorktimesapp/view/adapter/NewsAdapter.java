package com.example.newyorktimesapp.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.newyorktimesapp.R;
import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.models.Multimedium;
import com.example.newyorktimesapp.data.remote.APIService;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<Doc> news;

    public NewsAdapter(List<Doc> newsList) {
        this.news = newsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doc article = news.get(position);
        holder.newsTitle.setText(getSafeString(article.getHeadline().getMain()));
        holder.newsAuthor.setText(getSafeString(article.getByline().getOriginal()));
        holder.newsDescription.setText(getSafeString(article.getAbstract()));
        ArrayList<Multimedium> multimedia = (ArrayList<Multimedium>) article.getMultimedia();
        String thumbUrl = "";
        for (Multimedium m : multimedia) {
            if (m.getType().equals("image") && m.getSubtype().equals("thumbLarge")) {
                thumbUrl = APIService.API_IMAGE_BASE_URL + m.getUrl();
                break;
            }
        }
        if (!thumbUrl.isEmpty()) {
            Glide.with(holder.itemView)
                    .load(thumbUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_thumbnail)
                    .into(holder.newsThumbnail);
        } else {
            Glide.with(holder.itemView)
                    .load(R.drawable.placeholder_thumbnail)
                    .into(holder.newsThumbnail);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeData(ArrayList<Doc> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (news==null)
            return 0;
        return news.size();
    }

    private String getSafeString(String string) {
        if (string == null)
            return "";
        else
            return string;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle, newsDescription, newsAuthor;
        ImageView newsThumbnail;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.item_title);
            newsDescription = itemView.findViewById(R.id.item_description);
            newsAuthor = itemView.findViewById(R.id.item_author);
            newsThumbnail = itemView.findViewById(R.id.item_image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearNews() {
        int size = news.size();
        news.clear();
        notifyDataSetChanged();
    }
}
