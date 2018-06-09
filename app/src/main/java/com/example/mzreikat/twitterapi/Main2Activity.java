package com.example.mzreikat.twitterapi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mzreikat.twitterapi.tweets.RecyclerViewAdapter;
import com.example.mzreikat.twitterapi.tweets.TweetDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Main2Activity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getUserList();
    }
    private void getUserList() {
        Intent intent = getIntent();
        String username  = intent.getStringExtra("username");
        String tweetsNum = intent.getStringExtra("tweetsNum");

        Call<List<TweetDetails>> call = MainActivity.twitterAPI.getJSON(username, tweetsNum);
        call.enqueue(new Callback<List<TweetDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<TweetDetails>> call, @NonNull retrofit2.Response<List<TweetDetails>> response) {
                List<TweetDetails> tweetDetails = response.body();

                RecyclerView recyclerView = findViewById(R.id.card_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(Main2Activity.this);
                recyclerView.setLayoutManager(layoutManager);

                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), tweetDetails);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
            @Override
            public void onFailure(@NonNull Call<List<TweetDetails>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}