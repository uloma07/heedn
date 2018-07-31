package com.example.android.heedn;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.heedn.data.ScriptureContract;
import com.example.android.heedn.dummy.Constants;
import com.example.android.heedn.models.Scripture;
import com.example.android.heedn.utils.CursorRecyclerAdapter;
import com.example.android.heedn.utils.MyNotificationManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.android.heedn.dummy.Constants.COUNT;
import static com.example.android.heedn.dummy.Constants.FIRSTTIMEKEY;
import static com.example.android.heedn.dummy.Constants.ID_HIDDEN_LOADER;

/**
 * An activity representing a list of Scriptures. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ScriptureDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ScriptureListActivity extends  AppCompatActivity
                                        implements  LoaderManager.LoaderCallbacks<Cursor>{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static List<Scripture> SItems = new ArrayList<Scripture>();
    private boolean mTwoPane;
    private CursorRecyclerAdapter mCusorAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private RecyclerView recyclerView;
    private TextView noScriptures;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripture_list);

        String[] uiBindFrom = { ScriptureContract.ScriptureEntry.COLUMN_REFERENCE,
                ScriptureContract.ScriptureEntry._ID };
        // View IDs which will have the respective column data inserted
        int[] uiBindTo = { R.id.content, R.id.id_text };

        AdView mAdView = (AdView) findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newScriptureIntent = new Intent(ScriptureListActivity.this,AddScriptureActivity.class);
                ScriptureListActivity.this.startActivity(newScriptureIntent);
            }
        });

        FloatingActionButton review = (FloatingActionButton) findViewById(R.id.review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewScriptureIntent = new Intent(ScriptureListActivity.this,ReviewScriptureActivity.class);
                ScriptureListActivity.this.startActivity(reviewScriptureIntent);
            }
        });

        if (findViewById(R.id.scripture_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        noScriptures = (TextView) findViewById(R.id.tv_no_scriptures);
        recyclerView = (RecyclerView)findViewById(R.id.scripture_list);
        mCursor = getContentResolver().query(
                ScriptureContract.ScriptureEntry.CONTENT_URI,
                ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION,
                null,
                null,
                null);

        setupRecyclerView((RecyclerView) recyclerView, mCursor, uiBindFrom, uiBindTo);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean(FIRSTTIMEKEY, false)) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
                mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.BLUE);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            }

            /*
             * Displaying a notification locally
             */
            MyNotificationManager
                    .getInstance(this)
                    .displayNotification(getResources().getString(R.string.welcome), getResources().getString(R.string.welcome_msg));
//
            // run your one time code
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(FIRSTTIMEKEY, true);
            editor.putInt(COUNT, mCursor.getCount());
            editor.commit();
        }

        getSupportLoaderManager().initLoader(ID_HIDDEN_LOADER, null, this);


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, Cursor c, String[] from, int[] to ) {

        Log.v("HEEDn Content Count", String.valueOf(c.getCount()));
        if (mCusorAdapter == null) {
            mCusorAdapter = new SimpleRecyclerViewAdapter(R.layout.scripture_list_content, c, from, to, this,mTwoPane);
        }
        recyclerView.setAdapter(mCusorAdapter);


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case ID_HIDDEN_LOADER:
                return new CursorLoader(this,
                        ScriptureContract.ScriptureEntry.CONTENT_URI,
                        ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION,
                        null,
                        null,
                        ScriptureContract.ScriptureEntry.DEFAULT_SORT_ORDER);
            default:
                throw new RuntimeException("Hidden Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.v("HEEDn Onload Finished", "1");

        mCusorAdapter.swapCursor(data);
//      COMPLETED (29) If mPosition equals RecyclerView.NO_POSITION, set it to 0
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//      COMPLETED (30) Smooth scroll the RecyclerView to mPosition
        recyclerView.smoothScrollToPosition(mPosition);

//      COMPLETED (31) If the Cursor's size is not equal to 0, call showWeatherDataView
        if(data != null) {
            assert recyclerView != null;
            noScriptures.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else{
            noScriptures.setVisibility(View.VISIBLE);
            assert recyclerView != null;
            recyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCusorAdapter.swapCursor(null);
    }


    public static class SimpleRecyclerViewAdapter
            extends CursorRecyclerAdapter<SimpleRecyclerViewAdapter.ViewHolder> {

        private int mLayout;
        private int[] mFrom;
        private int[] mTo;
        private String[] mOriginalFrom;

        private final ScriptureListActivity mParentActivity;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String item_id = String.valueOf(view.getTag());

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ScriptureDetailFragment.ARG_ITEM_ID, item_id);
                    ScriptureDetailFragment fragment = new ScriptureDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.scripture_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ScriptureDetailActivity.class);
                    intent.putExtra(ScriptureDetailFragment.ARG_ITEM_ID, item_id);

                    context.startActivity(intent);
                }
            }
        };

        public SimpleRecyclerViewAdapter (int layout, Cursor c,
                                          String[] from,
                                          int[] to,
                                          ScriptureListActivity parent,
                                          boolean twoPane) {
            super(c);
            mLayout = layout;
            mTo = to;
            mOriginalFrom = from;
            mParentActivity = parent;
            mTwoPane = twoPane;
            findColumns(c, from);
        }

        /**
         * Create a map from an array of strings to an array of column-id integers in cursor c.
         * If c is null, the array will be discarded.
         *
         * @param c the cursor to find the columns from
         * @param from the Strings naming the columns of interest
         */
        private void findColumns(Cursor c, String[] from) {
            if (c != null) {
                int i;
                int count = from.length;
                if (mFrom == null || mFrom.length != count) {
                    mFrom = new int[count];
                }
                for (i = 0; i < count; i++) {
                    mFrom[i] = c.getColumnIndexOrThrow(from[i]);
                }
            } else {
                mFrom = null;
            }
        }

        @Override
        public Cursor swapCursor(Cursor c) {
            findColumns(c, mOriginalFrom);
            return super.swapCursor(c);
        }

        @Override
        public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mLayout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder (ViewHolder holder, Cursor cursor) {
            final int count = mTo.length;
            final int[] from = mFrom;
            int position = cursor.getPosition();
            holder.mIdView.setText(String.valueOf(position+1));
            holder.mContentView.setText(cursor.getString(from[0]));

            holder.itemView.setTag(cursor.getString(from[1]));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
