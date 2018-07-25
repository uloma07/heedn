package com.example.android.heedn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.heedn.data.ScriptureDbHelper;
import com.example.android.heedn.dummy.DummyContent;
import com.example.android.heedn.models.Scripture;
import com.example.android.heedn.utils.ScriptureCard;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.Arrays;

public class ReviewScriptureActivity extends AppCompatActivity {

    ScriptureDbHelper dbHelper;
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    public Button EndReviewBtn;
    Scripture[] scriptures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_scripture);

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f));

        dbHelper = new ScriptureDbHelper(this);
        scriptures = dbHelper.getAllScriptures();
        for(Scripture scripture : scriptures){
            mSwipeView.addView(new ScriptureCard(mContext, scripture, mSwipeView));
        }

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

    }

    public void finish(View view){
        EndReview();
    }

    public void EndReview(){
        Log.v("HEEDn count test", String.valueOf(scriptures[0].getNumber_of_reviews()));
        if(dbHelper.updateScriptures(scriptures)) {
            Intent homeIntent = new Intent(this, ScriptureListActivity.class);
            this.startActivity(homeIntent);
        }
    }

}
