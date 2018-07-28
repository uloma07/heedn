package com.example.android.heedn;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.heedn.data.ScriptureDbHelper;
import com.example.android.heedn.models.Scripture;
import com.example.android.heedn.utils.ScriptureCard;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.Arrays;

import static com.example.android.heedn.dummy.Constants.SCRIPTURESTOREVIEW;

public class ReviewScriptureActivity extends AppCompatActivity {

    ScriptureDbHelper dbHelper;
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    public Button EndReviewBtn;
    Scripture[] scriptures;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(SCRIPTURESTOREVIEW,scriptures);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_scripture);

        if(savedInstanceState!= null && savedInstanceState.getParcelableArray(SCRIPTURESTOREVIEW) != null ){
            Parcelable[] parcelablescriptures = savedInstanceState.getParcelableArray(SCRIPTURESTOREVIEW);
            scriptures = Arrays.copyOf(parcelablescriptures, parcelablescriptures.length, Scripture[].class);

        }
        else{
            dbHelper = new ScriptureDbHelper(this);
            scriptures = dbHelper.getAllScriptures();
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

    public void EndReview(){
        Log.v("HEEDn count test", String.valueOf(scriptures[0].getNumber_of_reviews()));
        if(dbHelper.updateScriptures(scriptures)) {
            Intent homeIntent = new Intent(this, ScriptureListActivity.class);
            this.startActivity(homeIntent);
        }
    }

}
