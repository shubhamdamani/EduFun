package com.example.droidrun;

import com.example.droidrun.model.NYTimesFeed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FeedNYTimesAPI {                // API to fetch content from nytimes.com

    String BASE_URL = "https://rss.nytimes.com/services/xml/rss/nyt/";   //static part

    // Dynamic feed
    @GET(BASE_URL + "{item_name}.xml")
    Call<NYTimesFeed> getFeed(@Path("item_name") String item_name);

   /* @GET("https://rss.nytimes.com/services/xml/rss/nyt/Soccer.xml")
    Call<NYTimesFeed> getFeed();*/



}
