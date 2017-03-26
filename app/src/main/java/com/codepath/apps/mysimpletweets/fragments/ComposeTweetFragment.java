package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ComposeTweetFragment extends DialogFragment {
    @Bind(R.id.etTweetTo)     EditText etTweetTo;
    @Bind(R.id.etTweet) EditText etTweet;
    private int mTweetWordCountMax = 140;
    private int mTweetWordCountMin = 1;
    private String MYTWEETID = "@pjaytumkur";
    TweetToTimeLineListener composeDone;

    public ComposeTweetFragment() {
        //Empty constructor
    }

    public interface TweetToTimeLineListener {
        public void dataBack(String twBody, String twURL);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            composeDone = (TweetToTimeLineListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ToolbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_compose_tweet, container, false);
        ButterKnife.bind(this, rootView);
        getDialog().setTitle("Twitter Mytweet");
        TextInputLayout etTweetWC = (TextInputLayout) rootView.findViewById(R.id.etTweetWC);
        etTweetWC.getEditText().addTextChangedListener(new CharacterCountErrorWatcher(etTweetWC, mTweetWordCountMin, mTweetWordCountMax));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button btnTweet = (Button) view.findViewById(R.id.btnTweet);
        Button btCancel = (Button) view.findViewById(R.id.btCancel);

        //etTweet.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Toast.makeText(getContext(), "Toast to show you selected text", Toast.LENGTH_SHORT).show();
        //    }
        //});
        btCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Cancel????", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        btnTweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "OK to Tweet?", Toast.LENGTH_SHORT).show();
                String tweetTo = etTweetTo.getText().toString();
                //Toast.makeText(getContext(), "Your Tweet To: "+tweetTo, Toast.LENGTH_SHORT).show();
                if(tweetTo != null ) {
                    TimelineActivity.myTweetParams.setTweetToUserId("@"+tweetTo);
                } else {
                    TimelineActivity.myTweetParams.setTweetToUserId(MYTWEETID);
                }
                String tweetText = etTweet.getText().toString();
                if (tweetText != null ) {
                    composeDone.dataBack(tweetText, tweetTo);
                } else  {
                    TimelineActivity.myTweetParams.setMyTweetMessage("Blank Tweet");
                    Toast.makeText(getContext(), "Your Tweet Msg: Blank Tweet", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });


     }





}