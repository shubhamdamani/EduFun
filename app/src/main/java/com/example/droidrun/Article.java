package com.example.droidrun;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import com.example.droidrun.model.Feed ;
import com.example.droidrun.model.MainRediffFeed;
import com.example.droidrun.model.NYTimesFeed ;
import com.example.droidrun.model.YahooFeed;
import com.example.droidrun.model.channel.Item;
import com.example.droidrun.model.channelYahoo.ItemYahoo;
import com.example.droidrun.model.entry.Entry;
import com.example.droidrun.model.rediffitem.RediffItem;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Article extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://www.reddit.com/r/";
    private static final String BASE_URL_NYTIMES = "https://rss.nytimes.com/services/xml/rss/nyt/";
    private static final String BASE_URL_YAHOO="https://yahoo.com/";
    private static final String BASE_URL_REDIFF="http://www.rediff.com/rss/";
    private Button btnLoadFeed;
    private EditText mFeedName;
    // private ListView listView;
    Button b1;
    TextView t1,t2,t3,t4;
    ArrayList<Post> posts;
    //  SpotsDialog dialog;

    String retrieve;
    String json_string;
    String JSON_String;
    JSONArray jsonArray;
    int USER_ID;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        posts = new ArrayList<Post>();

        t1=findViewById(R.id.q1);
        t2=findViewById(R.id.q2);
        t3=findViewById(R.id.q3);
        t4=findViewById(R.id.q4);
      //  LoadNewsFeed();

        //   listView = (ListView) findViewById(R.id.listView);
        //      if (getActivity() != null)
        //   ret();
        b1=findViewById(R.id.ttest);

        Intent i=getIntent();
        final String input = i.getStringExtra("s");
        LoadNewsFeed(input);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Article.this,Quiz.class);

                i.putExtra("s1",t1.getText());
                i.putExtra("s2",t2.getText());
                i.putExtra("s3",t3.getText());
                i.putExtra("s4",t4.getText());


                startActivity(i);
            }
        });


    }

    private void LoadNewsFeed(String str) {

        HashMap<Character,String> map = new HashMap<>();
        map.put('A',"Physics");
        map.put('B',"Maths");
        map.put('C',"Computer");
        map.put('D',"Science");
        map.put('E',"Politics");
        map.put('F',"Business");
        map.put('G',"Technology");
        map.put('H',"Sports");
        map.put('I',"Movies");
        map.put('J',"Music");

        String s="H";            //Fetch from Topics Table for User_Id
        if(str.equals("Sports"))
            s="H";
        else if(str.equals("Movies"))
            s="I";
        else if(str.equals("Science"))
            s="D";
        else
            s="G";
        Character c;
        for(int i=0;i<s.length();i++){

            c = s.charAt(i);
            AddFeed(c,map.get(c));
            Log.d("tag" ,c+":"+map.get(c));

        }


    }

    private void AddFeed(Character c,String s) {

        switch(c)
        {
            case 'I':
                initRediff("moviescolumnrss");
            case 'J':
                initYahoo(s);
            case 'H':
            case 'G':
            case 'F':
            case 'E':
            case 'D':
                initNYTimes(s);
            case 'C':
            case 'B':
            case 'A':
                init(s);
        }

    }

    private void init(String currentFeed){

        Retrofit retrofit = new Retrofit.Builder()                                          //Using retrofit for xml parsing
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);                           //establish connection with link given in FeedAPI according to user's interests

        Call<Feed> call = feedAPI.getFeed(currentFeed);

        call.enqueue(new Callback<Feed>() {                                      // getting our objects(here entry) of news feed
            @Override
            public void onResponse(Call<Feed> call, retrofit2.Response<Feed> response) {        //conversion of retrofit object to our class feed object


                List<Entry> entrys = response.body().getEntrys();


                for(int i=0;i<entrys.size();i++) {

                    ExtractXML extractXML1 = new ExtractXML(entrys.get(i).getContent(), "<a href=");
                    List<String> postContent = extractXML1.start();

                    ExtractXML extractXML2 = new ExtractXML(entrys.get(i).getContent(), "<img src=");
                    try {
                        postContent.add(extractXML2.start().get(0));
                    }
                    catch (NullPointerException e){          // null image
                        Log.d(TAG,"onResponse : NULL Pointer Exception : "+ e.getMessage());
                        postContent.add(null);
                    }
                    catch (IndexOutOfBoundsException e) {    // no image tag
                        Log.d(TAG,"onResponse : No image tag Exception : "+ e.getMessage());
                        postContent.add(null);
                    }
                    int lastPosition = postContent.size()-1;
                    try {
                        Post newPost = new Post(entrys.get(i).getTitle(),entrys.get(i).getAuthor().getName().substring(3),entrys.get(i).getUpdated(),postContent.get(0),postContent.get(lastPosition),"No description available ", "Source : Reddit");
                        posts.add(newPost);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }


                setListView();

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                //  dialog.dismiss();
                // Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void initRediff(String currentFeed){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_REDIFF)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedRediffAPI feedAPI = retrofit.create(FeedRediffAPI.class);

        Call<MainRediffFeed> call = feedAPI.getFeed(currentFeed);

        call.enqueue(new Callback<MainRediffFeed>() {
            @Override
            public void onResponse(Call<MainRediffFeed> call, retrofit2.Response<MainRediffFeed> response) {

                List<RediffItem> entrys = response.body().getmChannel().getItemRediff();


                for(int i=0;i<entrys.size();i++) {
                    try {
                        Post newPost = new Post(entrys.get(i).getTitle(),entrys.get(i).getDesc(),entrys.get(i).getAuth(),entrys.get(i).getLink(),entrys.get(i).getImag(),entrys.get(i).getDesc(),"Source : Rediff" );
                        posts.add(newPost);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }

                for(int j=0;j<posts.size();j++){

                    Log.d(TAG,"PostURL :"+posts.get(j).getPostURL()+
                            "\nThumbnailURL = "+posts.get(j).getThumbnailURL()+
                            "\nTitle : "+ posts.get(j).getTitle()+
                            "\nAuthor : "+posts.get(j).getAuthor()+
                            "\nUpdated : "+posts.get(j).getDate_updated()+
                            "\n");
                }


                setListView();





            }

            @Override
            public void onFailure(Call<MainRediffFeed> call, Throwable t) {
                //   dialog.dismiss();
                // Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();
                //Log.e(TAG, "Failure : Unable to retrieve RSS Feeds "+t.getMessage());
            }
        });
    }



    private void initNYTimes(String currentFeed){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_NYTIMES)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedNYTimesAPI feedNYTimesAPI = retrofit.create(FeedNYTimesAPI.class);

        Call<NYTimesFeed> call = feedNYTimesAPI.getFeed(currentFeed);



        call.enqueue(new Callback<NYTimesFeed>() {
            @Override
            public void onResponse(Call<NYTimesFeed> call, retrofit2.Response<NYTimesFeed> response) {


                List<Item> items = response.body().getmChannel().getItems();


                for(int i=0;i<items.size();i++) {


                    try {
                        Post newPost = new Post(items.get(i).getTitle(),items.get(i).getCreator(),items.get(i).getLastBuildDate(),items.get(i).getLink(),"",items.get(i).getDescription(), "Source : The New York Times");
                        posts.add(newPost);
                    }catch (NullPointerException e){
                        Log.d(TAG,"GADBADI");
                        e.printStackTrace();
                    }

                }




                setListView();


            }

            @Override
            public void onFailure(Call<NYTimesFeed> call, Throwable t) {
                //dialog.dismiss();
                // Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void initYahoo(String currentFeed){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_YAHOO)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedYahooAPI feedAPIk = retrofit.create(FeedYahooAPI.class);

        Call<YahooFeed> call = feedAPIk.getFeed(currentFeed);

        call.enqueue(new Callback<YahooFeed>() {
            @Override
            public void onResponse(Call<YahooFeed> call, retrofit2.Response<YahooFeed> response) {


                List<ItemYahoo> items = response.body().getmChannel().getItems();



                for(int i=0;i<items.size();i++) {



                    try {
                        Post newPost = new Post(items.get(i).getTitle(),"Yahoo",items.get(i).getLastBuildDate(),items.get(i).getLink(),"",items.get(i).getDescription(), "Source : Yahoo");
                        posts.add(newPost);
                    }catch (NullPointerException e){
                        Log.d(TAG,"GADBADI");
                        e.printStackTrace();
                    }

                }


                setListView();




            }

            @Override
            public void onFailure(Call<YahooFeed> call, Throwable t) {
                //Log.e(TAG, "Failure : Unable to retrieve RSS Feeds "+t.getMessage());
            }
        });
    }




    private void setListView(){

        t1.setText(posts.get(0).getTitle());
        t2.setText(posts.get(1).getTitle());
        t3.setText(posts.get(2).getTitle());
        t4.setText(posts.get(3).getTitle());





    }

    /*----------------------------------------------*/

    public void met()
    {
       /* Log.d("Met : ","Met STARTED");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_URL,
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        Log.d("Entered Response : ", "Now call ret && fun");
                        ret();



                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEY ERROR : ","Volley error ");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> prams = new HashMap<>();

                return prams;
            }
        };

    */


    }

  /*  public void ret()
    {
        Integer integer=77;
        BackgroundTask backgroundTask=new BackgroundTask();
        backgroundTask.execute(integer,USER_ID);
    }

    class BackgroundTask extends AsyncTask<Integer,Void,String>
    {
        String json_url="https://vlearndroidrun.000webhostapp.com/getUserTopics.php";


        @Override
        protected String doInBackground(Integer... integers) {

            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                //my
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("User_Id","UTF-8")+"="+URLEncoder.encode(USER_Class.getLoggedUserId()+"","UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();

                while((json_string=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                JSON_String=stringBuilder.toString().trim();
                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        public BackgroundTask()
        {
            super();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            try
            {JSON_String=result;
                getUserTopicString();}catch (Exception e)
            {
                dialog.dismiss();
                Toast.makeText(MainActivity.this,"No Internet connection",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public void getUserTopicString() {
        json_string = JSON_String;



        try {
            jsonObject = new JSONObject(JSON_String);

            int count = 0;
            jsonArray = jsonObject.getJSONArray("server_response");

            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                retrieve = jo.getString("TopicStr");             // String is converted from JSON format and finally stored in retrieve
                LoadNewsFeed();
                count++;


            }
            dialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    */
}




