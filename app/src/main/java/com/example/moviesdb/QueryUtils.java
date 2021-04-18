package com.example.moviesdb;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class QueryUtils {

    private static final String app_name = "MoviesDB-App";

    private QueryUtils() {}

    private static URL createURL(String query) {
        URL url = null;
        try {
            url = new URL(query);
        } catch (MalformedURLException e) {
            Log.v(app_name, "Unable to transform string to URL.", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        if (url == null) {
            return jsonResponse;
        }
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.v(app_name, "Unable to fetch data, returned with code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.v(app_name, "Unable to open a connection", e);
        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder sb = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF_8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            sb.append(line);
            line = bufferedReader.readLine();
        }
        return sb.toString();
    }

    public static String fetchData(String request_url) {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        String jsonResponse = null;
        URL url = createURL(request_url);
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.v(app_name, "Unable to make request", e);
        }
        return jsonResponse;
    }
}
