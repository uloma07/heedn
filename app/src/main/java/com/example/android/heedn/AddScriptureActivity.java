package com.example.android.heedn;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.android.heedn.data.ScriptureDbHelper;
import com.example.android.heedn.models.Scripture;
import com.example.android.heedn.utils.JSONUtils;
import com.example.android.heedn.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class AddScriptureActivity extends AppCompatActivity {

    FloatingSearchView mSearchView;
    ConstraintLayout mConstraintLayout;
    TextView mScriptureText;
    Button mSearch, mAdd;

    Scripture scripturetext;

    ScriptureDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scripture);

        dbHelper = new ScriptureDbHelper(this);

        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.add_scripture_contraint_layout);
        mScriptureText = (TextView) findViewById(R.id.tv_scripture_text);
        mSearch = (Button) findViewById(R.id.btn_search);
        mAdd = (Button) findViewById(R.id.btn_add);

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener(){
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Search();
            }

        });


    }

    public void Search(){
        if(!NetworkUtils.isConnectedToInternet(mConstraintLayout.getContext())){
            Log.v("HEEDn", "no internet");
            ShowSnackbar("Please check internet connection");
        }

        String currentQuery = mSearchView.getQuery().trim();

        String[] ref_items = currentQuery.split(" ");
        if(ref_items.length>1){
            scripturetext = new Scripture();
            scripturetext.setBook(ref_items[0]);

            if(ref_items.length == 2){
                String temp = ref_items[1];
                String[] nested_ref = temp.split(":");

                if(nested_ref.length == 1){
                    Log.v("HEEDn", "scripture is chapter");
                    ShowSnackbar("Sorry, we do not offer chapter memorisation.");
                    return;
                }

                if(nested_ref.length > 1){
                    scripturetext.setChapter(nested_ref[0]);
                    scripturetext.setVerse(nested_ref[1]);
                }
            }
            else if(ref_items.length == 3){
                scripturetext.setChapter(ref_items[1]);
                scripturetext.setVerse(ref_items[2]);
            }
        }

        if (scripturetext.isValidScripture()){
            Log.v("HEEDn", "scripture is valid");
            URL theUrl = NetworkUtils.buildUrl(scripturetext.getBook(),scripturetext.getChapter(), scripturetext.getVerse());
            new ScriptureQueryTask().execute(theUrl);
        }
        else{
            Log.v("HEEDn", "scripture is not valid");
            ShowSnackbar("Sorry, the scripture you have entered is not in the correct format. Please use book chapter:verse format. For example, James 1:1");
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
        long id = dbHelper.addNewScripture(scripturetext);
        if(id> 0){
            Intent homeIntent = new Intent(this,ScriptureListActivity.class);
            this.startActivity(homeIntent);
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
                ShowSnackbar("This Scripture was not found! Please check reference and retry.");
            }
        }
    }
}
