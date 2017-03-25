package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.apps.mysimpletweets.R;


public class ComposeTweetFragment extends DialogFragment {

    private int mTweetWordCountMax = 140;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_compose_tweet, container, false);
        getDialog().setTitle("Twitter Mytweet");
        TextInputLayout etTweetWC = (TextInputLayout) rootView.findViewById(R.id.etTweetWC);
        etTweetWC.getEditText().addTextChangedListener(new CharacterCountErrorWatcher(etTweetWC, 1, 140));
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
                dismiss();
            }
        });


     }



}