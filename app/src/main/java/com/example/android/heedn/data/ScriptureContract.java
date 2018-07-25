package com.example.android.heedn.data;

import android.provider.BaseColumns;

public class ScriptureContract {

    public static final class ScriptureEntry implements BaseColumns {

        public static final String TABLE_NAME = "Scriptures";
        public static final String REFERENCE = "reference";
        public static final String BOOK = "book";
        public static final String CHAPTER = "chapter";
        public static final String VERSE = "verse";
        public static final String TEXT = "text";
        public static final String TRANSLATION = "translation";
        public static final String NUMBEROFREVIEWS = "reviews";
        public static final String NUMBEROFCORRECTREVIEWS = "correctreviews";

    }

}
