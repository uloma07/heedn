package com.example.android.heedn.utils;


import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.heedn.R;
import com.example.android.heedn.models.Scripture;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

@Layout(R.layout.scripture_review_card_layout)
public class ScriptureCard {

    @View(R.id.tv_text_holder)
    public TextView ScriptTxt;

    @View(R.id.tv_reviews_holder)
    public TextView ReviewsTxt;

    @View(R.id.btn_reveal_toggle)
    public Button RevealBtn;


    private Scripture mScripture;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public ScriptureCard(Context context, Scripture scripture, SwipePlaceHolderView swipeView) {
        mContext = context;
        mScripture = scripture;
        mSwipeView = swipeView;
    }


    @Resolve
    public void onResolved(){
        ScriptTxt.setText(mScripture.getReference());
        ReviewsTxt.setText(mScripture.getReviewsString());
        RevealBtn.setText(mContext.getResources().getString(R.string.show_text));
        RevealBtn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(android.view.View v) {

                if(RevealBtn.getText() == mContext.getResources().getString(R.string.show_text)){
                    //show the text and switch button text
                    ScriptTxt.setText(mScripture.getText());
                    RevealBtn.setText(mContext.getResources().getString(R.string.hide_text));
                }
                else{
                    //show reference and switch button text
                    ScriptTxt.setText(mScripture.getReference());
                    RevealBtn.setText(mContext.getResources().getString(R.string.show_text));
                }
            }

        });
    }



    @SwipeOut
    public void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        int reviews = mScripture.getNumber_of_reviews();
        mScripture.setNumber_of_reviews(reviews+1);
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    public void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    public void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        int reviews = mScripture.getNumber_of_reviews();
        int correct = mScripture.getNumber_of_correct_reviews();
        mScripture.setNumber_of_reviews(reviews+1);
        mScripture.setNumber_of_correct_reviews(correct+1);
    }

    @SwipeInState
    public void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}
