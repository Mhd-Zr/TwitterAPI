package com.example.mzreikat.twitterapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String credentials = Credentials.basic("xeqwxQI0LfqvNS4vczUTfank3",
            "4ipr1Dc3aDxkeedFxgr8NYzIeX7WXm4Omsv1M7NzgEApRvwQI2");

    private Button requestTokenBtn;
    private Button requestUserDetailsBtn;
    private EditText userNameEditTxt;
    private TextView userNameTxtView;

    private ImageView imageView;
    private TextView nameTxtView;
    private TextView locationTxtView;
    private TextView urlTxtView;
    private TextView descriptionTxtView;

    private TwitterAPI twitterAPI;
    private OAuthToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestTokenBtn = findViewById(R.id.request_token_btn);
        requestTokenBtn.setOnClickListener(this);
        requestUserDetailsBtn = findViewById(R.id.request_user_details_btn);
        requestUserDetailsBtn.setOnClickListener(this);

        imageView = findViewById(R.id.imageView);
        userNameEditTxt = findViewById(R.id.username_editText);
        userNameTxtView = findViewById(R.id.username_textView);
        nameTxtView = findViewById(R.id.name_textView);
        locationTxtView = findViewById(R.id.location_textView);
        urlTxtView = findViewById(R.id.url_textView);
        descriptionTxtView = findViewById(R.id.description_textView);

        TextView resetTxtView = findViewById(R.id.resetAll_editText);
        resetTxtView.setOnClickListener(this);

        createTwitterApi();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.resetAll_editText:
                resetAll();
                break;
            case R.id.request_token_btn:
                if (checkInternet()) {
                    twitterAPI.postCredentials("client_credentials").enqueue(tokenCallback);
                }
                else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.request_user_details_btn:
                if (checkInternet())
                {
                    String editTxtInput = userNameEditTxt.getText().toString().trim();
                    if (!editTxtInput.isEmpty()) {
                        twitterAPI.getUserDetails(editTxtInput).enqueue(userDetailsCallback);
                    } else {
                        Toast.makeText(this, "Please provide a username", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void createTwitterApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        token != null ? token.getAuthorization() : credentials);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TwitterAPI.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        twitterAPI = retrofit.create(TwitterAPI.class);
    }

    private void resetAll() {
        userNameEditTxt.setText(null);
        imageView.setImageBitmap(null);
        nameTxtView.setText(R.string.api_txt_views);
        locationTxtView.setText(R.string.api_txt_views);
        urlTxtView.setText(R.string.api_txt_views);
        descriptionTxtView.setText(R.string.api_txt_views);

        userNameEditTxt.setFocusable(true);
    }

    private Callback<OAuthToken> tokenCallback = new Callback<OAuthToken>() {
        @Override
        public void onResponse(@NonNull Call<OAuthToken> call, retrofit2.Response<OAuthToken> response) {
            if (response.isSuccessful()) {
                requestTokenBtn.setEnabled(false);
                requestUserDetailsBtn.setEnabled(true);
                userNameTxtView.setEnabled(true);
                userNameEditTxt.setEnabled(true);
                token = response.body();
            } else {
                Toast.makeText(MainActivity.this, "Failure while requesting token", Toast.LENGTH_SHORT).show();
                Log.d("RequestTokenCallback", "Code: " + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(@NonNull Call<OAuthToken> call, Throwable t) {
            t.printStackTrace();
        }
    };

    private Callback<UserDetails> userDetailsCallback = new Callback<UserDetails>() {
        @Override
        public void onResponse(@NonNull Call<UserDetails> call, retrofit2.Response<UserDetails> response) {
            if (response.isSuccessful()) {
                UserDetails userDetails = response.body();

                nameTxtView.setText(Objects.requireNonNull(userDetails).getName() == null ? "name not found" : userDetails.getName());
                locationTxtView.setText(userDetails.getLocation() == null ? "location not found" : userDetails.getLocation());
                urlTxtView.setText(userDetails.getUrl() == null ? "url not found" : userDetails.getUrl());
                descriptionTxtView.setText(userDetails.getDescription().isEmpty() ? "description not found" : userDetails.getDescription());

                Picasso.with(MainActivity.this).load(processImageUrl(userDetails)).into(imageView);

            } else {
                Toast.makeText(MainActivity.this, "Failure while requesting user details", Toast.LENGTH_SHORT).show();
                Log.d("UserDetailsCallback", "Code:" + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(@NonNull Call<UserDetails> call, Throwable t) {
            t.printStackTrace();
        }
    };

    private String processImageUrl(UserDetails userDetails) {
        String url = userDetails.getProfile_image_url_https();
        return url.substring(0, url.length() - 11) + ".jpg";
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return Objects.requireNonNull(connectivityManager).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}