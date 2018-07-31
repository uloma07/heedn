package com.example.android.heedn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.example.android.heedn.data.ScriptureContract;
import com.example.android.heedn.utils.CursorRecyclerAdapter;

// import com.example.android.heedn.data.ScriptureDbHelper;

/**
 * An activity representing a single Scripture detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ScriptureListActivity}.
 */
public class ScriptureDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorRecyclerAdapter mCusorAdapter;
    private Cursor mCursor;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripture_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getResources().getString(R.string.share_your_faith), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        String[] selectionArgs = new String[] {getIntent().getStringExtra(ScriptureDetailFragment.ARG_ITEM_ID)};

        mCursor = getContentResolver().query(
                ScriptureContract.ScriptureEntry.CONTENT_URI,
                ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION,
                ScriptureContract.ScriptureEntry._ID+"=?",
                selectionArgs,
                null);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ScriptureDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ScriptureDetailFragment.ARG_ITEM_ID));
            ScriptureDetailFragment fragment = new ScriptureDetailFragment();
            fragment.setArguments(arguments);
            fragment.setMyCursor(mCursor);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.scripture_detail_container, fragment)
                    .commit();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ScriptureListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCusorAdapter.swapCursor(null);
    }
}
