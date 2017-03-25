package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.ComplexRecyclerAdapter;
import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    //private TweetsArrayAdapter aTweets;
    private ComplexRecyclerAdapter aTweets;
    //private ListView lvTweets;
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView rvTweets;
    private static Toast toast;
    private static String toShow;
    private static String FILEOFTWEETS = "TWEETS.TXT";
    private static boolean firstTweetAccess = false;
    // Have an utility for reading tweets
    private JSONArray readTweets() throws JSONException {
        File filesDir = getFilesDir();
        File tweetFile = new File(filesDir, FILEOFTWEETS);
        String s = null;
        try {
            byte [] sBytes = FileUtils.readFileToByteArray(tweetFile);
            s = String.valueOf(sBytes);
        } catch (IOException e) {
            s = null;
        }
        JSONArray tfromfile = new JSONArray(s);
        return tfromfile;
    }

    // Have an utility for writing tweets
    private void writeTweets(JSONArray response) {
        File filesDir = getFilesDir();
        File tweetFile = new File(filesDir, FILEOFTWEETS);
        try {
            String s = String.valueOf(response);
            byte[] sBytes = s.getBytes();
            FileUtils.writeByteArrayToFile(tweetFile, sBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.tw__ic_logo_large);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setSize(FloatingActionButton.SIZE_MINI);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showToast(getBaseContext(),"You clicked on compose button");
                ComposeTweetFragment composeTweetDialog = new ComposeTweetFragment();
                FragmentManager mFragManager = getSupportFragmentManager();
                composeTweetDialog.show(mFragManager,"Tweet");
            }
        });
        // find the listView
        //lvTweets = (ListView) findViewById(R.id.lvTweets);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        // create the arraylist from data
        tweets = new ArrayList<>();
        //Construct the adapter from data source
        //aTweets = new TweetsArrayAdapter(this, tweets);
        aTweets = new ComplexRecyclerAdapter(this, tweets);
        //connect adapter from listview

        // Configure the RecyclerView

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        rvTweets.addOnScrollListener( new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                //Log.d("DEBUG","LoadMore: page "+page+" Ct: "+totalItemsCount);
                long mUid = (tweets.get(totalItemsCount-1).getUid()-1);
                //Log.d("DEBUG","LoadMore: page "+page+" Ct: "+totalItemsCount+" uId: "+mUid);
                loadNextDataFromApi(mUid);
            }
            });
        // Adds the scroll listener to RecyclerView
        rvTweets.setAdapter(aTweets);
        client = TwitterApplication.getRestClient();
        populateTimeline(1);

    }
    public static void showToast(Context context, String message) {
        if (toast != null) { // prevent multi-toasts
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Send API request to get the timeline json
    // Fill the list view by creating the tweet objects from json
    private void populateTimeline(long uId) {
        client.setId(uId);

            client.getHomeTimelineExtended(new JsonHttpResponseHandler() {
                // Successful
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    //super.onSuccess(statusCode, headers, response);
                    //Log.d("DEBUG",json.toString()); // Got JSON here
                    // Deserialize
                    // Create models and add to adapter here
                    //Load models into listview
                    tweets.addAll(Tweet.fromJSONArray(json));
                    aTweets.notifyDataSetChanged();
                    //Log.d("DEBUG"," mmm");
                }
                // failure
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    //super.onFailure(statusCode, headers, throwable, errorResponse);
                    String JsonErrorMessage = "Json message corrupted";
                    Log.d("DEBUG", JsonErrorMessage);
                    showToast(getBaseContext(), JsonErrorMessage);
                }
            });

    }

    public void loadNextDataFromApi(Long offset) {
        //firstTweetAccess = false;
        int mTweetSize = tweets.size() - 1;
        //rvTweets.notifyItemRangeInserted(mSize, mArticleSize);
        rvTweets.setItemViewCacheSize(mTweetSize); // Should this mbe mSize?
        populateTimeline(offset);
        aTweets.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showToast(getBaseContext(),"You clicked on compose button");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
