package com.example.android.heedn;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.android.heedn.data.ScriptureContract;
import com.example.android.heedn.models.Scripture;
import com.example.android.heedn.utils.JSONUtils;
import com.example.android.heedn.utils.NetworkUtils;
import com.example.android.heedn.widgets.HEEDn_widget;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static com.example.android.heedn.dummy.Constants.ADDEDSCRIPTURE;
import static com.example.android.heedn.dummy.Constants.COUNT;

public class AddScriptureActivity extends AppCompatActivity {

    FloatingSearchView mSearchView;
    ConstraintLayout mConstraintLayout;
    TextView mScriptureText;
    Button mSearch, mAdd;

    Scripture scripturetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scripture);


        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.add_scripture_contraint_layout);
        mScriptureText = (TextView) findViewById(R.id.tv_scripture_text);
        mSearch = (Button) findViewById(R.id.btn_search);
        mAdd = (Button) findViewById(R.id.btn_add);


        if(savedInstanceState!= null && savedInstanceState.getParcelable(ADDEDSCRIPTURE) != null ){
            scripturetext = savedInstanceState.getParcelable(ADDEDSCRIPTURE);
            if(scripturetext.getText() != null && !scripturetext.getText().isEmpty()) {
                mScriptureText.setText(scripturetext.getText());
                mSearch.setVisibility(View.GONE);
                mAdd.setVisibility(View.VISIBLE);
            }
        }

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener(){
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Search();
            }

        });

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                mScriptureText.setText(getResources().getString(R.string.no_scripture_found));
                mSearch.setVisibility(View.VISIBLE);
                mAdd.setVisibility(View.GONE);
                scripturetext = null;
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ADDEDSCRIPTURE,scripturetext);
    }

    public void Search(){
        if(!NetworkUtils.isConnectedToInternet(mConstraintLayout.getContext())){
            Log.v("HEEDn", "no internet");
            ShowSnackbar(getResources().getString(R.string.internet));
        }

        String currentQuery = mSearchView.getQuery().trim();

        String[] temp = currentQuery.split(":");

        if(temp.length != 2){
            Log.v("HEEDn", "scripture is not valid");
            ShowSnackbar(getResources().getString(R.string.invalid_scripture_search_format));
            return;
        }
        scripturetext = new Scripture();
        scripturetext.setVerse(temp[1].trim());

        temp = temp[0].split(" ");

        if(temp.length < 2){
            Log.v("HEEDn", "scripture is not valid");
            ShowSnackbar(getResources().getString(R.string.invalid_scripture_search_format));
            return;
        }

        scripturetext.setChapter(temp[temp.length-1].trim());

        String[] bookarr = Arrays.copyOfRange(temp,0, temp.length-1);
        String bookname = "";
        for(String s: bookarr ){
            bookname += (s.trim()+" ");
        }

        scripturetext.setBook(bookname.trim());

        if (scripturetext.isValidScripture()){
            Log.v("HEEDn", "scripture is valid");
            URL theUrl = NetworkUtils.buildUrl(scripturetext.getBook(),scripturetext.getChapter(), scripturetext.getVerse());
            new ScriptureQueryTask().execute(theUrl);
        }
        else{
            Log.v("HEEDn", "scripture is not valid");
            ShowSnackbar(getResources().getString(R.string.invalid_scripture_search_format));
            return;
        }

    }

    public final void ShowSnackbar(String msg){
        Snackbar.make(mConstraintLayout, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        return;
    }

    public void Search(View view) {
        Search();
    }

    public void Add(View view) {
        Uri id = getContentResolver().insert(ScriptureContract.ScriptureEntry.CONTENT_URI, scripturetext.getContentValues());
        if(id != Uri.EMPTY){

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if(prefs.contains(COUNT)) {

                SharedPreferences.Editor editor = prefs.edit();
                int numberofScriptures = prefs.getInt(COUNT, 0);
                editor.putInt(COUNT, numberofScriptures+1);
                editor.apply();
            }
            else{

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(COUNT, 1);
                editor.apply();
            }
            doWidgetUpdate();
            Intent homeIntent = new Intent(this,ScriptureListActivity.class);
            this.startActivity(homeIntent);
        }
        else{
            Log.v("HEEDn", "Sorry, you have previously added this scripture");
            ShowSnackbar(getResources().getString(R.string.previously_added_scripture));
        }
    }

    public class ScriptureQueryTask extends AsyncTask<URL, Void, Scripture> {

        @Override
        protected Scripture doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String SearchResults = null;
            try {
                SearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //put items into a scripture
            Scripture results = JSONUtils.parseScriptureJson(scripturetext,SearchResults);
            return results;
        }

        @Override
        protected void onPostExecute(Scripture result) {
            if(result != null) {
                scripturetext = result;
                if(scripturetext.getText() != null && !scripturetext.getText().isEmpty()) {
                    mScriptureText.setText(scripturetext.getText());
                    mSearch.setVisibility(View.GONE);
                    mAdd.setVisibility(View.VISIBLE);
                }
            }
            else{
                ShowSnackbar(getResources().getString(R.string.scripture_not_found));
            }
        }
    }

    public void doWidgetUpdate(){
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(this, HEEDn_widget.class));
        HEEDn_widget.updateHEEDnWidgets(this, widgetManager, appWidgetIds);
    }
}
