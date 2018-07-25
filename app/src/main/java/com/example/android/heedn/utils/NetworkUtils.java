package com.example.android.heedn.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String SCRIPTURES_BASE_URL = "https://bible-api.com/";

    public static URL buildUrl(String book, String chapter, String verse) {
        // return the URL used to query Open Bible.com's API
        String complete_url = SCRIPTURES_BASE_URL + book + "+" + chapter + ":" + verse;
        Uri ScriptureUri = Uri.parse(complete_url)
                .buildUpon()
                .build();

        URL url = null;
        try {
            Log.v("HEEDn", ScriptureUri.toString());
            url = new URL(ScriptureUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static Boolean isConnectedToInternet(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            urlConnection.disconnect();
        }
    }

}
