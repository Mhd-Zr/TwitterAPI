package com.example.mzreikat.twitterapi.tweets;

public class TweetDetails
{
    private String created_at;
    private String id;
    private String text;

    public TweetDetails() {
    }

    public TweetDetails(String created_at, String id, String text) {
        this.created_at = created_at;
        this.id = id;
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}