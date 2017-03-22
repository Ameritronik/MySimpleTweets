package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 Taking the tweet objects and turning them into views
 */

public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,android.R.layout.simple_list_item_1,tweets);
    }

    // override and setup custom templates here, and use view holder patterns

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //1. Get the tweets
        Tweet tweet = getItem(position);
        //2. inflate template
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }
        //3. Find the subviews to fill with data in the template
        ImageView ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvRealName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvRelTime = (TextView) convertView.findViewById(R.id.tvTimeStamp);
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
        showReltime = showReltime.replace("minutes ago","m");
        showReltime = showReltime.replace("hour ago","h");
        tvRelTime.setText(showReltime);
        tvRelTime.setTextColor(Color.GRAY);
        ivProfileImage.setImageResource((android.R.color.transparent));
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        //5. Return the view to be inserted into the list
        return convertView;
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

        return relativeDate;
    }
}
