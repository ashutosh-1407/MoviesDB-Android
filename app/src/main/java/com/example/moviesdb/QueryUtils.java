package com.example.moviesdb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryUtils {

    private QueryUtils() {}

    public static Map<String, ArrayList<MediaItem>> parseMediaFromResponse(JSONArray jsonArray) {
        ArrayList<MediaItem> trendingCardItems = new ArrayList<>();
        ArrayList<MediaItem> popularCardItems = new ArrayList<>();
        ArrayList<MediaItem> topRatedCardItems = new ArrayList<>();
        ArrayList<MediaItem> recommendedCardItems = new ArrayList<>();
        Map<String, ArrayList<MediaItem>> resultMap = new HashMap<>();
        try {
            for (int i=0; i<jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String img = jsonObject.getString("poster_path");
                int id = jsonObject.getInt("id");
                if (jsonObject.getString("type").equals("trending")) {
                    trendingCardItems.add(new MediaItem(img, id, "a"));
                } else if (jsonObject.getString("type").equals("popular")) {
                    popularCardItems.add(new MediaItem(img, id, "a"));
                } else if (jsonObject.getString("type").equals("top-rated")) {
                    topRatedCardItems.add(new MediaItem(img, id, "a"));
                } else if (jsonObject.get("type").equals("recommended")) {
                    recommendedCardItems.add(new MediaItem(img, id, "a"));
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

    public static ArrayList<ReviewItem> parseReviewsFromResponse(JSONArray reviews) {
        ArrayList<ReviewItem> reviewItems = new ArrayList<>();
        try {
            for (int i=0; i<reviews.length(); ++i) {
                JSONObject jsonObject = reviews.getJSONObject(i);
                String label = "by " + jsonObject.getString("author") + " on " + jsonObject.getString("created_at");
                String rating = jsonObject.getInt("rating")/2 + "/5";
                String content = jsonObject.getString("content");
                reviewItems.add(new ReviewItem(label, rating, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewItems;
    }

    public static ArrayList<CastItem> parseCastsFromResponse(JSONArray casts) {
        ArrayList<CastItem> castItems = new ArrayList<>();
        try {
            for (int i=0; i<casts.length(); ++i) {
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
}
