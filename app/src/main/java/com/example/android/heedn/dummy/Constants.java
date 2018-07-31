package com.example.android.heedn.dummy;

import android.content.UriMatcher;

import com.example.android.heedn.data.ScriptureContract;
import com.example.android.heedn.models.Scripture;

import java.util.HashMap;

public class Constants {

    public static final String CHANNEL_ID = "heedn";
    public static final String CHANNEL_NAME = "HEEDn Notification";
    public static final String CHANNEL_DESCRIPTION = "HEEDn Notifications";

    public static final String FIRSTTIMEKEY = "firsttime";
    public static final String COUNT = "count";

    public static final String ADDEDSCRIPTURE = "added_scripture";
    public static final String SCRIPTURESTOREVIEW = "review_scripture";

    private static final int SCRIPTURE = 1;
    private static final int SCRIPTURE_ID = 2;

    public static final int ID_HIDDEN_LOADER = 440;

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ScriptureContract.ScriptureEntry.AUTHORITY, "Scriptures", SCRIPTURE);
        sUriMatcher.addURI(ScriptureContract.ScriptureEntry.AUTHORITY, "Scriptures/#", SCRIPTURE_ID);
    }

    private static HashMap sScriptureProjection;

    static {
        sScriptureProjection = new HashMap();
        for(int i=0; i < ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION.length; i++) {
            sScriptureProjection.put(
                    ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION[i],
                    ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION[i]);
        }

    }
}
