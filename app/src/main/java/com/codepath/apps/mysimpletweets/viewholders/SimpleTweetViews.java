package com.codepath.apps.mysimpletweets.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.List;

public class SimpleTweetViews extends RecyclerView.ViewHolder implements View.OnClickListener {

        List<Tweet> tweets;
        static Context mContext;
        // Construct
        public SimpleTweetViews(Context context, View view, List<Tweet> mTweets) {
            super(view);
            this.tweets = mTweets;
            this.mContext = context;
            view.setOnClickListener(this);
        }

        // If a click on view
        @Override
        public void onClick(View view) {
            /* int position = getLayoutPosition();
            mediaArticle article = articles.get(position);
            SearchActivity.showToast(mContext, "Please wait...");
            Intent i = new Intent(mContext, ArticleActivity.class);
            i.putExtra("webUrl", article.webUrl);
            Log.d("DEBUG","TextView, Got webUrl "+ article.webUrl);
            mContext.startActivity(i);
            */
        }
        // send the article view back to requester
        //public TextView getTvArticleText() {
        //    return tvArticleText;
        //}

}

