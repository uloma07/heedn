package com.example.android.heedn.utils;

import com.example.android.heedn.models.Scripture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

    public static Scripture parseScriptureJson(Scripture scripture ,String json) {

        if(json != null && !json.isEmpty()){
            try {

                JSONObject scripture_data = new JSONObject(json);

                if(scripture == null){
                    scripture = new Scripture();
                }

                scripture.setReference(scripture_data.getString("reference"));
                scripture.setText(scripture_data.getString("text"));
                scripture.setTranslation(scripture_data.getString("translation_name"));

                return scripture;
            }
            catch (JSONException j){
                j.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
