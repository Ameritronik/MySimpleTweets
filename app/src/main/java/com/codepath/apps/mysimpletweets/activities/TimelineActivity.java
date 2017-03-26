package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.ComplexRecyclerAdapter;
import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity
        implements ComposeTweetFragment.TweetToTimeLineListener {

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
    private static long myCurrentUidPosition = 1;
    private Tweet myTweet = new Tweet();
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

    private void showEditDialog() {
        //showToast(getBaseContext(),"You clicked on compose button");
        ComposeTweetFragment composeTweetDialog = new ComposeTweetFragment();
        FragmentManager mFragManager = getSupportFragmentManager();
        composeTweetDialog.show(mFragManager,"Tweet");
        //FragmentManager fm = getSupportFragmentManager();
        //EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Compose Tweet");
        //editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    public void makeMyTweet(String twBody) {
        //Tweet t = new Tweet();
        //t.setBody(twBody);
        //t.setUid(1923);
        User iAm = new User();
        iAm.setName("HK");
        iAm.setScreenName("pjaytumkur");
        String id_str = "843926695107166208";
        long uid = Long.parseLong(id_str);
        iAm.setUid(uid);
        Uri uri = Uri.parse("android.resource://com.codepath.apps.mysimpletweets/drawable/mytwitimage");
        iAm.setProfileImageUrl(uri.toString());
        // Get 'now' time
        Date today = new Date();
        CharSequence cTime  = DateFormat.format("EEE MMM dd HH:mm:ss -0400 yyyy", today.getTime());
        // Compose my Tweet
        myTweet.setBody(twBody);
        myTweet.setUid(uid);
        myTweet.setCreatedAt(cTime.toString());
        myTweet.setUser(iAm);
        myTweet.settMediaType(null);
        myTweet.settMediaType(null);
        myTweet.settMediaprofileImageUrl("");
        myTweet.settMediaId(Long.valueOf(1));
    }

    public void dataBack(final String twBody, String twURL) {
        //Toast.makeText(this, "Body: " + twBody +" URL: "+twURL, Toast.LENGTH_SHORT).show();
        client.setMyTweet(twBody);
        client.settweetUser_id(twURL);
        client.postHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                showCustomToast("Tweet successful", "GREEN");
                makeMyTweet(twBody);
                aTweets.addTweet(myTweet);
                aTweets.notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                showCustomToast("Tweet unsuccessful","RED");
            }
        });
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
                showEditDialog();
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
                myCurrentUidPosition = mUid;
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
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
    public void showCustomToast(String message, String color) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        TextView text = (TextView) layout.findViewById(R.id.toastText);
        text.setText(message);
        if(color != null) {
            if (color.equals("BLACK")) {
                text.setTextColor(Color.BLACK);
            } else if (color.equals("RED")) {
                text.setTextColor(Color.RED);
            } else if (color.equals("BLUE")) {
                text.setTextColor(Color.BLUE);
            }
        } // else defaul toast text color of green
        Toast toast = new Toast(getBaseContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    // Send API request to get the timeline json
    // Fill the list view by creating the tweet objects from json
    private void populateTimeline(long uId) {
        client.setId(uId);
        client.getHomeTimeline(new JsonHttpResponseHandler() {
                // Successful
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    //super.onSuccess(statusCode, headers, response);
                    //Log.d("DEBUG",json.toString()); // Got JSON here
                    // Deserialize
                    // Create models and add to adapter here
                    //Load models into listview
                    tweets.addAll(Tweet.fromJSONArray(json));
                    //Log.d("DEBUG","Tw:"+tweets.toString());
                    aTweets.notifyDataSetChanged();

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
        showCustomToast("Getting more tweets...", "BLUE");
        //showToast(getBaseContext(),"Getting more tweets");
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

    public void tweetReply(View view) {
        Log.d("DEBUG","Added to Favorite"+tweets.toString());
    }

    public void reTweet(View view) {
        Log.d("DEBUG","Added to Favorite"+tweets.toString());
    }

    public void addToFavorite(View view) {

        Log.d("DEBUG","Added to Favorite"+tweets.toString());
    }
}
