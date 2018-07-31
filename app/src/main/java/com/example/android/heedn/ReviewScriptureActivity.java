package com.example.android.heedn;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// import com.example.android.heedn.data.ScriptureDbHelper;
import com.example.android.heedn.data.ScriptureContract;
import com.example.android.heedn.models.Scripture;
import com.example.android.heedn.utils.CursorRecyclerAdapter;
import com.example.android.heedn.utils.ScriptureCard;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.Arrays;

import static com.example.android.heedn.dummy.Constants.SCRIPTURESTOREVIEW;

public class ReviewScriptureActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor> {
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    public Button EndReviewBtn;
    Scripture[] scriptures;
    Cursor mCursor;
    private CursorRecyclerAdapter mCusorAdapter;
    private int mPosition;
//     private RecyclerView recyclerView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(SCRIPTURESTOREVIEW,scriptures);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_scripture);
        mCursor = getContentResolver().query(
                ScriptureContract.ScriptureEntry.CONTENT_URI,
                ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION,
                null,
                null,
                null);
        if(savedInstanceState!= null && savedInstanceState.getParcelableArray(SCRIPTURESTOREVIEW) != null ){
            Parcelable[] parcelablescriptures = savedInstanceState.getParcelableArray(SCRIPTURESTOREVIEW);
            scriptures = Arrays.copyOf(parcelablescriptures, parcelablescriptures.length, Scripture[].class);
        }
        else{
            scriptures = ScriptureContract.ScriptureEntry.readFromCursor(mCursor);
        }

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f));


        if(scriptures == null || scriptures.length == 0) {
            findViewById(R.id.btn_endReview).setVisibility(View.GONE);
            findViewById(R.id.btn_addScriptures).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.tv_msg)).setText(getResources().getString(R.string.you_have_no_scripture));
        }
        else {
            for (Scripture scripture : scriptures) {
                mSwipeView.addView(new ScriptureCard(mContext, scripture, mSwipeView));
            }
        }

    }

    public void finish(View view){
        EndReview();
    }

    public void addscripture(View view){
        Intent addIntent = new Intent(this, AddScriptureActivity.class);
        this.startActivity(addIntent);
    }

    public void EndReview() {
        Log.v("HEEDn count test", String.valueOf(scriptures[0].getNumber_of_reviews()));
        ContentValues[] scriptureContents = new ContentValues[scriptures.length];

        boolean completed = true;

        for(int i=0; i<scriptureContents.length; i++) {
            scriptureContents[i] = scriptures[i].getContentValues();

            completed = completed && getContentResolver().update(ScriptureContract.ScriptureEntry.CONTENT_ID_URI_BASE,
                    scriptureContents[i],
                    ScriptureContract.ScriptureEntry._ID+"=?",
                    new String[] {String.valueOf(scriptures[i].getId())}) > 0;
        }


        if(completed) {
            Intent homeIntent = new Intent(this, ScriptureListActivity.class);
            this.startActivity(homeIntent);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,
                ScriptureContract.ScriptureEntry.CONTENT_URI,
                ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION,
                null,
                null,
                ScriptureContract.ScriptureEntry.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCusorAdapter.swapCursor(data);
//      COMPLETED (29) If mPosition equals RecyclerView.NO_POSITION, set it to 0
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//      COMPLETED (30) Smooth scroll the RecyclerView to mPosition
        // recyclerView.smoothScrollToPosition(mPosition);

//      COMPLETED (31) If the Cursor's size is not equal to 0, call showWeatherDataView
        // if (data.getCount() != 0) showWeatherDataView();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCusorAdapter.swapCursor(null);
    }
}
