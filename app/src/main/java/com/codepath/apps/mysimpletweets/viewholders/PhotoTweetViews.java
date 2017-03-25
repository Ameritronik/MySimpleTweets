package com.codepath.apps.mysimpletweets.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.List;

/**
 * Created by hkanekal on 3/18/2017.
 */

public class PhotoTweetViews extends RecyclerView.ViewHolder implements View.OnClickListener
{
    //@Bind(R.id.ivArticle) public ImageView ivArticle;
    //@Bind(R.id.tvArticle) public TextView tvArticle;
    List<Tweet> tweets;
    Context mContext;
    // construct
    public PhotoTweetViews(Context context, View view, List<Tweet> mTweets) {
        super(view);
        this.tweets = mTweets;
        this.mContext = context;
        view.setOnClickListener(this);
        //ButterKnife.bind(this, view);
    }

    // if clicked do this
    @Override
    public void onClick(View view) {
        //int position = getLayoutPosition();
        //mediaArticle article = articles.get(position);
        //SearchActivity.showToast(mContext, "Please wait...");
        //Intent i = new Intent(mContext, ArticleActivity.class);
        //i.putExtra("webUrl", article.webUrl);
        //mContext.startActivity(i);
    }

    // getters and setters
    /*
    public ImageView getIvArticle() {
        return ivArticle;
    }
    public void setIvArticle(ImageView ivArticle) {
        this.ivArticle = ivArticle;
    }
    public TextView getTvArticle() {
        return tvArticle;
    }
    public void setTvArticle(String tvArticle) {
        this.tvArticle.setText(tvArticle);
    }
    */
}



