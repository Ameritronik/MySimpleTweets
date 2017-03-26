package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.viewholders.PhotoTweetViews;
import com.codepath.apps.mysimpletweets.viewholders.SimpleTweetViews;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.codepath.apps.mysimpletweets.R.drawable.my_placeholder;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class ComplexRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int PHOTO = 2; // This tweet has a picture
    private final int VIDEO   = 1; // has video
    private final int NORMAL  = 0; // Normal tweet no pic or vid
    private static ArrayList<Tweet> tweets;
    private static Context mContext;
    // construct
    public ComplexRecyclerAdapter(Context context, ArrayList<Tweet> tweets) {
        this.tweets = tweets;
        mContext = context;
    }

    public static void addTweet(Tweet t) {
        tweets.add(0,t);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.tweets.size();
    }

    @Override
    public int getItemViewType(int position) {
        int retMtype = 0;
        Tweet thisTweet = tweets.get(position);
        String mType = thisTweet.gettMediaType();
        if(mType != null) {
            if (mType.equals("photo")) {
                retMtype = PHOTO;
            } else if (mType.equals("video")) {
                retMtype = VIDEO;
            }
        } else {
            retMtype =  NORMAL;
        }
        String rm = String.valueOf(retMtype);
        //Log.d("DEBUG","CRA: Mtype: "+mType+" RetM: "+rm+" pos: "+position);
        return retMtype;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
       LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case NORMAL:
                View textOnly = inflater.inflate(R.layout.simple_tweet_view,viewGroup, false);
                viewHolder = new SimpleTweetViews(mContext, textOnly, tweets);
                break;
            case PHOTO:
                View FullView = inflater.inflate(R.layout.photo_tweet_view,viewGroup, false);
                viewHolder = new PhotoTweetViews(mContext, FullView, tweets);
                break;
            default:
                FullView = inflater.inflate(R.layout.grid_view_video_tweet,viewGroup, false);
                viewHolder = new PhotoTweetViews(mContext, FullView, tweets);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case NORMAL:
                SimpleTweetViews twHolder = (SimpleTweetViews) viewHolder;
                configureSimpleTweetViews(twHolder, position);
                break;
            case PHOTO:
                PhotoTweetViews ivHolder = (PhotoTweetViews) viewHolder;
                configurePhotoTweetViews(ivHolder, position);
                break;
            default:
                twHolder = (SimpleTweetViews) viewHolder;
                configureSimpleTweetViews(twHolder, position);
                break;
        }

    }

    private void configureSimpleTweetViews(SimpleTweetViews view, int position) {
        final Tweet tweet = tweets.get(position);
        ImageView ivProfileImage = (ImageView) view.itemView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) view.itemView.findViewById(R.id.tvUserName);
        TextView tvRealName = (TextView) view.itemView.findViewById(R.id.tvName);
        TextView tvBody = (TextView) view.itemView.findViewById(R.id.tvBody);
        TextView tvRelTime = (TextView) view.itemView.findViewById(R.id.tvTimeStamp);
        TextView tvRetweetCount = (TextView) view.itemView.findViewById(R.id.tvRetweetCount);
        TextView tvStarCount = (TextView) view.itemView.findViewById(R.id.tvStarCount);
        //4. Populate data into the subviews
        tvUserName.setText(tweet.getUser().getScreenName());
        tvUserName.setTextColor(Color.BLACK);
        tvUserName.setTypeface(null, Typeface.BOLD);
        String rName = "@"+tweet.getUser().getName();
        tvRealName.setText(rName);
        tvRealName.setTextColor(Color.GRAY);
        tvBody.setText(tweet.getBody());
        String tweetTime = tweet.getCreatedAt();
        String showReltime = getRelativeTimeAgo(tweetTime);
        tvRelTime.setText(showReltime);
        tvRelTime.setTextColor(Color.GRAY);
        tvRetweetCount.setText(tweet.gettRetweetCount());
        tvStarCount.setText(tweet.gettFavoriteCount());
        ivProfileImage.setImageResource((android.R.color.transparent));
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl())
                .fit()
                .centerInside()
                .transform(new RoundedCornersTransformation(10, 10)).into(ivProfileImage);
    }

    private void configurePhotoTweetViews(PhotoTweetViews view, int position) {
        final Tweet tweet = tweets.get(position);
        ImageView ivProfileImage = (ImageView) view.itemView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) view.itemView.findViewById(R.id.tvUserName);
        TextView tvRealName = (TextView) view.itemView.findViewById(R.id.tvName);
        TextView tvBody = (TextView) view.itemView.findViewById(R.id.tvBody);
        TextView tvRelTime = (TextView) view.itemView.findViewById(R.id.tvTimeStamp);
        TextView tvRetweetCount = (TextView) view.itemView.findViewById(R.id.tvRetweetCount);
        TextView tvStarCount = (TextView) view.itemView.findViewById(R.id.tvStarCount);
        ImageView tweetPhoto = (ImageView) view.itemView.findViewById(R.id.ivPhoto);
        //Log.d("DEBUG","in Config Photo Views");
        //4. Populate data into the subviews
        tvUserName.setText(tweet.getUser().getScreenName());
        tvUserName.setTextColor(Color.BLACK);
        tvUserName.setTypeface(null, Typeface.BOLD);
        String rName = "@"+tweet.getUser().getName();
        tvRealName.setText(rName);
        tvRealName.setTextColor(Color.GRAY);
        tvBody.setText(tweet.getBody());
        String tweetTime = tweet.getCreatedAt();
        String showReltime = getRelativeTimeAgo(tweetTime);
        tvRelTime.setText(showReltime);
        tvRelTime.setTextColor(Color.GRAY);
        tvRetweetCount.setText(tweet.gettRetweetCount());
        tvStarCount.setText(tweet.gettFavoriteCount());
        ivProfileImage.setImageResource((android.R.color.transparent));
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl())
                .fit()
                .centerInside()
                .transform(new RoundedCornersTransformation(10, 10)).into(ivProfileImage);
        Picasso.with(getContext()).load(tweet.gettMediaprofileImageUrl())
                .fit()
                .centerInside()
                .placeholder(my_placeholder)
                .error(my_placeholder)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(tweetPhoto);
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf;
        sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";

        long dateMillis = 0;
        try {
            dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        relativeDate = relativeDate.replace("second ago","s");
        relativeDate = relativeDate.replace("seconds ago","s");
        relativeDate = relativeDate.replace("minute ago","m");
        relativeDate = relativeDate.replace("minutes ago","m");
        relativeDate = relativeDate.replace("hour ago","h");
        relativeDate = relativeDate.replace("hours ago","h");

        return relativeDate;
    }

} // end ComplexRecyclerAdapter
