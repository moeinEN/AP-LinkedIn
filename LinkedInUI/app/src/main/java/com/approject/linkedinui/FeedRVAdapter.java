package com.approject.linkedinui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.Post;
import de.hdodenhof.circleimageview.CircleImageView;

public class FeedRVAdapter extends RecyclerView.Adapter<FeedRVAdapter.ViewHolder> {

    // arraylist for our posts.
    private ArrayList<Post> feedModalArrayList;
    private Context context;
    private OnItemClickListener listener;

    // creating a constructor for our adapter class.
    public FeedRVAdapter(ArrayList<Post> feedModalArrayList, Context context, OnItemClickListener listener) {
        this.feedModalArrayList = feedModalArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // getting data from array list and setting it to our modal class.
        Post modal = feedModalArrayList.get(position);

        // setting data to each view of recyclerview item.
        Picasso.get().load(R.drawable.test).into(holder.authorIV);
        holder.authorNameTV.setText(modal.getMiniProfile().getFirstName() + " " + modal.getMiniProfile().getLastName());
        holder.timeTV.setText("20 January at 11:06");
        StringBuilder hashtags = new StringBuilder();
        for (String str : modal.getHashtags())
            hashtags.append("#").append(str);
        holder.descTV.setText(modal.getText() + "\n" + hashtags);
        Picasso.get().load(R.drawable.test).into(holder.postIV);
        holder.likesTV.setText(String.valueOf(modal.getLikes().getLikedUsers().size()));
        holder.commentsTV.setText(String.valueOf(modal.getLikes().getLikedUsers().size()));

        // Setting click listeners
        holder.authorIV.setOnClickListener(v -> listener.onAuthorImageClick(position));
        holder.postIV.setOnClickListener(v -> listener.onPostImageClick(position));
        holder.authorNameTV.setOnClickListener(v -> listener.onAuthorNameClick(position));

        // Set initial like icon
        holder.likeIV.setImageResource(R.drawable.ic_like);

        // Set click listener for like image
        holder.likeIV.setOnClickListener(v -> {
            listener.onLikeImageClick(position);
            holder.likeIV.setImageResource(R.drawable.ic_like_filled);
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return feedModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views
        // of recycler view items.
        private CircleImageView authorIV;
        private TextView authorNameTV, timeTV, descTV;
        private ImageView postIV, likeIV;
        private TextView likesTV, commentsTV;
        private LinearLayout shareLL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our variables
            shareLL = itemView.findViewById(R.id.idLLShare);
            authorIV = itemView.findViewById(R.id.idCVAuthor);
            authorNameTV = itemView.findViewById(R.id.idTVAuthorName);
            timeTV = itemView.findViewById(R.id.idTVTime);
            descTV = itemView.findViewById(R.id.idTVDescription);
            postIV = itemView.findViewById(R.id.idIVPost);
            likesTV = itemView.findViewById(R.id.idTVLikes);
            commentsTV = itemView.findViewById(R.id.idTVComments);
            likeIV = itemView.findViewById(R.id.likeIV);
        }
    }
}
