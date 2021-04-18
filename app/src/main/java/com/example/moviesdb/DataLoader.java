package com.example.moviesdb;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class DataLoader extends AsyncTaskLoader<String> {

    private static String mUrl;

    public DataLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return QueryUtils.fetchData(mUrl);
    }
}
