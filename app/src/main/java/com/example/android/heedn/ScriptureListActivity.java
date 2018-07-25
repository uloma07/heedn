package com.example.android.heedn;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Script;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.heedn.data.ScriptureDbHelper;
import com.example.android.heedn.dummy.Constants;
import com.example.android.heedn.dummy.DummyContent;
import com.example.android.heedn.models.Scripture;
import com.example.android.heedn.utils.MyNotificationManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.example.android.heedn.dummy.Constants.COUNT;
import static com.example.android.heedn.dummy.Constants.FIRSTTIMEKEY;

/**
 * An activity representing a list of Scriptures. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ScriptureDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ScriptureListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ScriptureDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripture_list);

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

        TextView noScriptures = (TextView) findViewById(R.id.tv_no_scriptures);
        View recyclerView = findViewById(R.id.scripture_list);

        dbHelper = new ScriptureDbHelper(this);
        Scripture [] sitems = dbHelper.getAllScriptures();
        int numberofScriptures = 0;
        if(sitems != null) {
            DummyContent.SItems = Arrays.asList(sitems);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
            noScriptures.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            numberofScriptures = sitems.length;
        }
        else{
            noScriptures.setVisibility(View.VISIBLE);
            assert recyclerView != null;
            recyclerView.setVisibility(View.GONE);
        }


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
            MyNotificationManager.getInstance(this).displayNotification("Welcome", "How many scriptures have you HEEDn in your heart?");

            // run your one time code
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(FIRSTTIMEKEY, true);
            editor.putInt(COUNT, numberofScriptures);
            editor.commit();
        }


    }

        private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
            Log.v("HEEDn Content", String.valueOf(DummyContent.SItems.size()));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.SItems, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ScriptureListActivity mParentActivity;
        //private final List<DummyContent.DummyItem> mValues;
        private final List<Scripture> mScriptureValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();

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

        SimpleItemRecyclerViewAdapter(ScriptureListActivity parent,
                                      List<Scripture> items,
                                      boolean twoPane) {
            mScriptureValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scripture_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(position+1));
            holder.mContentView.setText(mScriptureValues.get(position).toString());

            holder.itemView.setTag(mScriptureValues.get(position).getId());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mScriptureValues.size();
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
