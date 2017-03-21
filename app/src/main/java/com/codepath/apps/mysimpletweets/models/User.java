package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hkanekal on 3/20/2017.
 */

public class User {
    // list the attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    // Getters

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    // deserialize the user from json object
    public static User fromJSON(JSONObject json) {
        User u = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id_str");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url_https");
            Log.d("DEBUG","User "+u.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // return a user
        return u;
    }
}
