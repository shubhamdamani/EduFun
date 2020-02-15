package com.example.droidrun;

import com.example.droidrun.model.MainRediffFeed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FeedRediffAPI {             // API to fetch content from rediff.com


    String BASE_URL = "http://www.rediff.com/rss/";   //static part

    // Dynamic feed
    @GET(BASE_URL + "{feed_name}.xml")
    Call<MainRediffFeed> getFeed(@Path("feed_name") String feed_name);

}
