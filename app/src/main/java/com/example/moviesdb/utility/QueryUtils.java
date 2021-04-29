package com.example.moviesdb.utility;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.UserHandle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.moviesdb.model.CardAdapter;
import com.example.moviesdb.model.CastItem;
import com.example.moviesdb.model.MediaItem;
import com.example.moviesdb.model.ReviewItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class QueryUtils {

    private QueryUtils() {
    }

    public static Map<String, ArrayList<MediaItem>> parseMediaFromResponse(JSONArray jsonArray) {
        ArrayList<MediaItem> trendingCardItems = new ArrayList<>();
        ArrayList<MediaItem> popularCardItems = new ArrayList<>();
        ArrayList<MediaItem> topRatedCardItems = new ArrayList<>();
        ArrayList<MediaItem> recommendedCardItems = new ArrayList<>();
        Map<String, ArrayList<MediaItem>> resultMap = new HashMap<>();
        try {
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String mediaType = jsonObject.getString("media_type");
                String img = jsonObject.getString("poster_path");
                String name = jsonObject.getString("name");
                int id = jsonObject.getInt("id");
                if (jsonObject.getString("type").equals("trending")) {
                    trendingCardItems.add(new MediaItem(mediaType, img, id, name, "", ""));
                } else if (jsonObject.getString("type").equals("popular")) {
                    popularCardItems.add(new MediaItem(mediaType, img, id, name, "", ""));
                } else if (jsonObject.getString("type").equals("top-rated")) {
                    topRatedCardItems.add(new MediaItem(mediaType, img, id, name, "", ""));
                } else if (jsonObject.get("type").equals("recommended")) {
                    recommendedCardItems.add(new MediaItem(mediaType, img, id, name, "", ""));
                }
            }
            resultMap.put("trending", trendingCardItems);
            resultMap.put("popular", popularCardItems);
            resultMap.put("top-rated", topRatedCardItems);
            resultMap.put("recommended", recommendedCardItems);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<ReviewItem> parseReviewsFromResponse(JSONArray reviews) {
        ArrayList<ReviewItem> reviewItems = new ArrayList<>();
        try {
            for (int i = 0; i < reviews.length(); ++i) {
                JSONObject jsonObject = reviews.getJSONObject(i);
                ZonedDateTime dateTime = ZonedDateTime.parse(jsonObject.getString("created_at"));
                String label = "by " + jsonObject.getString("author") + " on " + convertDate(dateTime);
                String rating = jsonObject.getInt("rating") / 2.0 + "/5";
                String content = jsonObject.getString("content");
                reviewItems.add(new ReviewItem(label, rating, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewItems;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String convertDate(ZonedDateTime created_at) {
        return created_at.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("US")) + ", " + created_at.getMonth().getDisplayName(TextStyle.SHORT, new Locale("US")) + " " + created_at.getDayOfMonth() + " " + created_at.getYear();
    }

    public static ArrayList<CastItem> parseCastsFromResponse(JSONArray casts) {
        ArrayList<CastItem> castItems = new ArrayList<>();
        try {
            for (int i = 0; i < casts.length(); ++i) {
                JSONObject jsonObject = casts.getJSONObject(i);
                String img = jsonObject.getString("profile_path");
                String name = jsonObject.getString("name");
                castItems.add(new CastItem(img, name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return castItems;
    }

    public static ArrayList<MediaItem> parseSearchFromResponse(JSONArray results) {
        ArrayList<MediaItem> searchItems = new ArrayList<>();
        try {
            for (int i = 0; i < results.length(); ++i) {
                JSONObject jsonObject = results.getJSONObject(i);
                String year = jsonObject.getString("year");
                String rating = jsonObject.getString("rating");
                String name = jsonObject.getString("name");
                String imgURl = jsonObject.getString("backdrop_path");
                int id = jsonObject.getInt("id");
                String type = jsonObject.getString("media_type");
                searchItems.add(new MediaItem(type, imgURl, id, name, year, rating));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchItems;
    }

    public static boolean checkMedia(Context context, String mediaType, int mediaId) {
        ArrayList<MediaItem> watchlistItems;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
        TypeToken<ArrayList<MediaItem>> token = new TypeToken<ArrayList<MediaItem>>() {
        };
        watchlistItems = gson.fromJson(sharedPreferences.getString("values", String.valueOf(0)), token.getType());
        if (watchlistItems != null) {
            int i = 0;
            for (; i < watchlistItems.size(); ++i) {
                if (watchlistItems.get(i).getType().equals(mediaType) && watchlistItems.get(i).getId() == mediaId) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void addMedia(Context context, String mediaType, int mediaId, String mediaUrl, String mediaName) {
        ArrayList<MediaItem> watchlistItems;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
        TypeToken<ArrayList<MediaItem>> token = new TypeToken<ArrayList<MediaItem>>() {
        };
        watchlistItems = gson.fromJson(sharedPreferences.getString("values", String.valueOf(0)), token.getType());
        if (watchlistItems != null) {
            int i = 0;
            for (; i < watchlistItems.size(); ++i) {
                if (watchlistItems.get(i).getType().equals(mediaType) && watchlistItems.get(i).getId() == mediaId) {
                    break;
                }
            }
            if (i == watchlistItems.size()) {
                watchlistItems.add(new MediaItem(mediaType, mediaUrl, mediaId, mediaName, "", ""));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String jsonString = gson.toJson(watchlistItems);
                editor.putString("values", jsonString);
                editor.apply();
            }
        }

    }

    public static void removeMedia(Context context, String mediaType, int mediaId) {
        ArrayList<MediaItem> watchlistItems;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
        TypeToken<ArrayList<MediaItem>> token = new TypeToken<ArrayList<MediaItem>>() {
        };
        watchlistItems = gson.fromJson(sharedPreferences.getString("values", String.valueOf(0)), token.getType());
        if (watchlistItems != null) {
            int i=0;
            for (; i<watchlistItems.size(); ++i) {
                if (watchlistItems.get(i).getType().equals(mediaType) && watchlistItems.get(i).getId() == mediaId) {
                    watchlistItems.remove(i);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String jsonString = gson.toJson(watchlistItems);
                    editor.putString("values", jsonString);
                    editor.apply();
                }
            }
        }
    }
}
