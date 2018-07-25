package com.example.android.heedn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.heedn.data.ScriptureDbHelper;
import com.example.android.heedn.dummy.DummyContent;
import com.example.android.heedn.models.Scripture;

/**
 * A fragment representing a single Scripture detail screen.
 * This fragment is either contained in a {@link ScriptureListActivity}
 * in two-pane mode (on tablets) or a {@link ScriptureDetailActivity}
 * on handsets.
 */
public class ScriptureDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    private Scripture scripture;
    private ScriptureDbHelper dbHelper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScriptureDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new ScriptureDbHelper(this.getContext());

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get();

            scripture = dbHelper.retrieveScripture(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));

            Log.v("HEEDn Scripture", scripture.toString());

            final Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && scripture != null) {
                appBarLayout.setTitle(scripture.getReference());
            }


        }
    }

//    public void Deleted(View view){
//        boolean res = new ScriptureDbHelper(this).delete(Integer.parseInt(getIntent().getStringExtra(ScriptureDetailFragment.ARG_ITEM_ID)));
//        if(res){
//            Intent homeIntent = new Intent(this,ScriptureListActivity.class);
//            this.startActivity(homeIntent);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scripture_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (scripture != null) {
            ((TextView) rootView.findViewById(R.id.scripture_detail)).setText(scripture.getText());
            final Context c = rootView.getContext();

            rootView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean res = new ScriptureDbHelper(c).delete(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));
                    if(res){
                        Intent homeIntent = new Intent(c,ScriptureListActivity.class);
                        c.startActivity(homeIntent);
                    }
                }
            });
        }

        return rootView;
    }
}
