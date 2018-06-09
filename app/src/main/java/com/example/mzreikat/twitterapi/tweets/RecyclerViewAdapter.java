package com.example.mzreikat.twitterapi.tweets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mzreikat.twitterapi.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private List<TweetDetails> tweetDetails;
    private Context context;

    public RecyclerViewAdapter(Context context, List<TweetDetails> tweetDetails) {
        this.tweetDetails = tweetDetails;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.tv_created_at.setText(tweetDetails.get(position).getCreated_at());
        holder.tv_id.setText(tweetDetails.get(position).getId());
        holder.tv_text.setText(tweetDetails.get(position).getText());
    }
    @Override
    public int getItemCount() {
        return tweetDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_created_at;
        private TextView tv_id;
        private TextView tv_text;

        ViewHolder(View itemView) {
            super(itemView);

            tv_created_at = itemView.findViewById(R.id.tv_created_at);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_text = itemView.findViewById(R.id.tv_text);
        }
    }
}