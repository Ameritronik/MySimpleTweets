package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;

/**
 * Created by hkanekal on 3/21/2017.
 * "media": [{
    "id": 411031503833792512,
    "id_str": "411031503833792512",
    "indices": [55, 78],
    "media_url": "https:\/\/ton.twitter.com\/1.1\/ton\/data\/dm
    \/411031503817039874\/411031503833792512\/cOkcq9FS.jpg",
    "media_url_https": "https:\/\/ton.twitter.com\/1.1\/ton\/data\/dm
    \/411031503817039874\/411031503833792512\/cOkcq9FS.jpg",
    "url": "https:\/\/t.co\/ZSvIEMOPb8",
    "display_url": "pic.twitter.com\/ZSvIEMOPb8",
    "expanded_url": "https:\/\/ton.twitter.com\/1.1\/ton\/data\/dm
    \/411031503817039874\/411031503833792512\/cOkcq9FS.jpg",
    "type": "photo",
    "sizes": {
    "medium": {
    "w": 600,
    "h": 450,
    "resize": "fit"
     },
    "large": {
    "w": 1024,
    "h": 768,
    "resize": "fit"
    },
    "thumb": {
    "w": 150,
    "h": 150,
    "resize": "crop"
    },
    "small": {
    "w": 340,
    "h": 255,
    "resize": "fit"
 }
 }
 }]
 *
 */

public class Media {
    // list the attributes
    private String mediaType;
    private String mediaURL;
    private Long mediaId;
    private String profileImageUrl;
    private static int mediaSmallX;
    private static int mediaSmallY;
    private static int mediaMediumX;
    private static int mediaMediumY;
    private static int mediaLargeX;
    private static int mediaLargeY;

    // deserialize the user from json object
    public static Media fromJSON(JSONArray json) {
        Media m = new Media();
        try {
            // extract JSON obj in array
            JSONObject inJson = json.getJSONObject(0);
            m.mediaType = inJson.getString("type");
            m.mediaURL = inJson.getString("media_url_https");
            m.mediaId = inJson.getLong("id");
            // extract sizes in embedded JSON Obj
            JSONObject sizes = inJson.getJSONObject("sizes");
            mediaSizes(m, sizes);
            Log.d("DEBUG","Media "+m.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG", "MediaExtract: "+m.toString());

        Log.d("DEBUG", "MediaExtract: "+m.toString());
        // return a media
        return m;
    }

    public static void mediaSizes(Media m, JSONObject sizes){
        JSONObject small = new JSONObject();
        JSONObject medium = new JSONObject();
        JSONObject large = new JSONObject();
        try {
            small = sizes.getJSONObject("small");
            medium = sizes.getJSONObject("medium");
            large = sizes.getJSONObject("large");
            mediaSmallX = parseInt(small.getJSONObject("h").toString());
            mediaSmallY = parseInt(small.getJSONObject("v").toString());
            mediaMediumX = parseInt(medium.getJSONObject("h").toString());
            mediaMediumY = parseInt(medium.getJSONObject("v").toString());
            mediaLargeX = parseInt(large.getJSONObject("h").toString());
            mediaLargeY = parseInt(large.getJSONObject("v").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Getters
    public String getMediaType() {
        return mediaType;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static int getMediaSmallX() {
        return mediaSmallX;
    }

    public static int getMediaSmallY() {
        return mediaSmallY;
    }

    public static int getMediaMediumX() {
        return mediaMediumX;
    }

    public static int getMediaMediumY() {
        return mediaMediumY;
    }

    public static int getMediaLargeX() {
        return mediaLargeX;
    }

    public static int getMediaLargeY() {
        return mediaLargeY;
    }
}
