package com.example.android.heedn.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.heedn.models.Scripture;

import java.util.List;

public class ScriptureContract {

    public static final class ScriptureEntry implements BaseColumns {
        public static final String AUTHORITY = "com.example.android.heedn.data.ScriptureContract";
        private static final String SCHEME = "content://";

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.scriptures";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.scriptures";

        public static final String DEFAULT_SORT_ORDER = " _id ASC";
        public static final int SCRIPTURES_ID_PATH_POSITION = 1;


        private static final String PATH_SCRIPTURES = "/scriptures";
        private static final String PATH_SCRIPTURES_ID = "/scriptures/";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_SCRIPTURES);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_SCRIPTURES_ID);
        public static final String TABLE_NAME = "Scriptures";
        public static final String COLUMN_REFERENCE = "reference";
        public static final String COLUMN_BOOK = "book";
        public static final String COLUMN_CHAPTER = "chapter";
        public static final String COLUMN_VERSE = "verse";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_TRANSLATION = "translation";
        public static final String COLUMN_NUMBEROFREVIEWS = "reviews";
        public static final String COLUMN_NUMBEROFCORRECTREVIEWS = "correctreviews";



        public static final String[] DEFAULT_PROJECTION = new String[] {
                ScriptureEntry._ID,
                ScriptureEntry.COLUMN_REFERENCE,
                ScriptureEntry.COLUMN_BOOK,
                ScriptureEntry.COLUMN_CHAPTER,
                ScriptureEntry.COLUMN_VERSE,
                ScriptureEntry.COLUMN_TEXT,
                ScriptureEntry.COLUMN_TRANSLATION,
                ScriptureEntry.COLUMN_NUMBEROFREVIEWS,
                ScriptureEntry.COLUMN_NUMBEROFCORRECTREVIEWS
        };

        public  static final Scripture[] readFromCursor(Cursor mCursor) {
            Scripture[] results = new Scripture[mCursor.getCount()];

            if(mCursor != null) {
                if(mCursor.moveToFirst()) {
                    int i=0;
                    do {
                        Scripture entry = new Scripture();
                        entry.setId(Long.parseLong(mCursor.getString(mCursor.getColumnIndex(_ID))));
                        entry.setReference(mCursor.getString(mCursor.getColumnIndex(COLUMN_REFERENCE)));
                        entry.setBook(mCursor.getString(mCursor.getColumnIndex(COLUMN_BOOK)));
                        entry.setChapter(mCursor.getString(mCursor.getColumnIndex(COLUMN_CHAPTER)));
                        entry.setVerse(mCursor.getString(mCursor.getColumnIndex(COLUMN_VERSE)));
                        entry.setText(mCursor.getString(mCursor.getColumnIndex(COLUMN_TEXT)));
                        entry.setTranslation(mCursor.getString(mCursor.getColumnIndex(COLUMN_TRANSLATION)));
                        entry.setNumber_of_correct_reviews(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(COLUMN_NUMBEROFREVIEWS))));
                        entry.setNumber_of_reviews(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(COLUMN_NUMBEROFCORRECTREVIEWS))));

                        results[i] = entry;
                        i++;
                    } while (mCursor.moveToNext());
                }
                mCursor.close();
            }
            return results;
        }

//        @Override
//        public String getType(Uri uri) {
//            switch (sUriMatcher.match(uri)) {
//                case STUDENTS:
//                    return ContractClass.Students.CONTENT_TYPE;
//                case STUDENTS_ID:
//                    return ContractClass.Students.CONTENT_ITEM_TYPE;
//                case CLASSES:
//                    return ContractClass.Classes.CONTENT_TYPE;
//                case CLASSES_ID:
//                    return ContractClass.Classes.CONTENT_ITEM_TYPE;
//                default:
//                    throw new IllegalArgumentException("Unknown URI " + uri);
//            }
//        }
    }



}
