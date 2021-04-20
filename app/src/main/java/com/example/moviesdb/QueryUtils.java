package com.example.moviesdb;

import android.provider.ContactsContract;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryUtils {

    private QueryUtils() {}

    public static Map<String, ArrayList<CardItem>> parseMediaFromResponse(JSONArray jsonArray) {
        ArrayList<CardItem> trendingCardItems = new ArrayList<>();
        ArrayList<CardItem> popularCardItems = new ArrayList<>();
        ArrayList<CardItem> topRatedCardItems = new ArrayList<>();
        ArrayList<CardItem> recommendedCardItems = new ArrayList<>();
        Map<String, ArrayList<CardItem>> resultMap = new HashMap<>();
        try {
            for (int i=0; i<jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String img = jsonObject.getString("poster_path");
                int id = jsonObject.getInt("id");
                if (jsonObject.getString("type").equals("trending")) {
                    trendingCardItems.add(new CardItem(img, id));
                } else if (jsonObject.getString("type").equals("popular")) {
                    popularCardItems.add(new CardItem(img, id));
                } else if (jsonObject.getString("type").equals("top-rated")) {
                    topRatedCardItems.add(new CardItem(img, id));
                } else if (jsonObject.get("type").equals("recommended")) {
                    recommendedCardItems.add(new CardItem(img, id));
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
}
