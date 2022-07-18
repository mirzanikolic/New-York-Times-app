package com.example.newyorktimesapp.adapter;

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
import com.example.newyorktimesapp.data.remote.ApiUtils;
import com.example.newyorktimesapp.models.Doc;
import com.example.newyorktimesapp.models.Multimedium;
import com.example.newyorktimesapp.view.activities.NewsListActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<Doc> newsList;
    private NewsListActivity newsListActivity;

    public NewsAdapter(NewsListActivity newsListActivity, List<Doc> newsList) {
        this.newsListActivity = newsListActivity;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doc article = newsList.get(position);
        holder.newsTitle.setText(getSafeString(article.getHeadline().getMain()));
        holder.newsAuthor.setText(getSafeString(article.getByline().getOriginal()));
        holder.newsDescription.setText(getSafeString(article.getAbstract()));
        ArrayList<Multimedium> multimedia = (ArrayList<Multimedium>) article.getMultimedia();
        String thumbUrl = "";
        for (Multimedium m : multimedia) {
            if (m.getType().equals("image") && m.getSubtype().equals("thumbLarge")) {
                thumbUrl = ApiUtils.API_IMAGE_BASE_URL + m.getUrl();
                break;
            }
        }
        if (!thumbUrl.isEmpty()) {
            Glide.with(newsListActivity)
                    .load(thumbUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_thumbnail)
                    .into(holder.newsThumbnail);
        } else {
            Glide.with(newsListActivity)
                    .load(R.drawable.placeholder_thumbnail)
                    .into(holder.newsThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
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

    public void clear() {
        newsList.clear();
    }
}
