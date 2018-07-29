package com.example.android.heedn.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.heedn.models.Scripture;

import java.util.HashMap;

public class HeednContentProvider extends ContentProvider {
    private ScriptureDbHelper dbHelper;
    private static final int SCRIPTURE = 1;
    private static final int SCRIPTURE_ID = 2;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> sScripturesProjectionMap;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ScriptureContract.ScriptureEntry.AUTHORITY, "scriptures", SCRIPTURE);
        sUriMatcher.addURI(ScriptureContract.ScriptureEntry.AUTHORITY, "scriptures/#", SCRIPTURE_ID);

        sScripturesProjectionMap = new HashMap<String, String>();
        for(int i=0; i < ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION.length; i++) {
            sScripturesProjectionMap.put(
                    ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION[i],
                    ScriptureContract.ScriptureEntry.DEFAULT_PROJECTION[i]);
        }
    }


    public static class ScriptureDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "scriptures.db";

        private static final int DATABASE_VERSION = 1;

        SQLiteDatabase mDb;

        public ScriptureDbHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mDb = getWritableDatabase();
        }


        public long addNewScripture(Scripture scripture) {

            //check if item exists
            if( getScripturebyreference(scripture.getReference())) return -1;
            ContentValues cv = new ContentValues();
            cv.put(ScriptureContract.ScriptureEntry.COLUMN_REFERENCE, scripture.getReference());
            cv.put(ScriptureContract.ScriptureEntry.COLUMN_TEXT, scripture.getText());
            cv.put(ScriptureContract.ScriptureEntry.COLUMN_TRANSLATION, scripture.getTranslation());
            cv.put(ScriptureContract.ScriptureEntry.COLUMN_CHAPTER, scripture.getChapter());
            cv.put(ScriptureContract.ScriptureEntry.COLUMN_BOOK, scripture.getBook());
            cv.put(ScriptureContract.ScriptureEntry.COLUMN_VERSE, scripture.getVerse());
            cv.put(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFCORRECTREVIEWS, scripture.getNumber_of_correct_reviews());
            cv.put(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFREVIEWS, scripture.getNumber_of_reviews());
            return mDb.insert(ScriptureContract.ScriptureEntry.TABLE_NAME, null, cv);
        }

        private boolean DeleteScriptureFromDb(long id) {
            if( id < 0 || !getScripture(id)) return false;
            // Inside, call mDb.delete to pass in the TABLE_NAME and the condition that ScriptureEntry._ID equals id
            return mDb.delete(ScriptureContract.ScriptureEntry.TABLE_NAME, ScriptureContract.ScriptureEntry._ID + "=" + id, null) > 0;
        }

        private boolean getScripture(long id) {
            Cursor cursor = mDb.rawQuery("select * from " + ScriptureContract.ScriptureEntry.TABLE_NAME + " where " + ScriptureContract.ScriptureEntry._ID + "='" + String.valueOf(id) + "'" , null);
            if (cursor != null && cursor.getCount()> 0) {
                cursor.close();
                return true;
            }
            return false;
        }

        private boolean getScripturebyreference(String ref) {
            Cursor cursor = mDb.rawQuery("select * from " + ScriptureContract.ScriptureEntry.TABLE_NAME + " where " + ScriptureContract.ScriptureEntry.COLUMN_REFERENCE + "='" + ref+ "'" , null);
            if (cursor != null && cursor.getCount()> 0) {
                cursor.close();
                return true;
            }
            return false;
        }

        public Scripture retrieveScripture(long id) {
            Scripture s = null;
            Cursor cursor = mDb.rawQuery("select * from " + ScriptureContract.ScriptureEntry.TABLE_NAME + " where " + ScriptureContract.ScriptureEntry._ID + "='" + String.valueOf(id) + "'" , null);
            if (cursor != null && cursor.getCount()> 0) {
                s = new Scripture();
                if(cursor.moveToNext()){
                    s.setId(cursor.getLong(cursor.getColumnIndex(ScriptureContract.ScriptureEntry._ID)));
                    s.setVerse(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_VERSE)));
                    s.setBook(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_BOOK)));
                    s.setChapter(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_CHAPTER)));
                    s.setReference(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_REFERENCE)));
                    s.setText(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_TEXT)));
                    s.setTranslation(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_TRANSLATION)));
                    s.setNumber_of_correct_reviews(cursor.getInt(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFCORRECTREVIEWS)));
                    s.setNumber_of_reviews(cursor.getInt(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFREVIEWS)));
                }
                cursor.close();
            }
            return s;
        }

        public Scripture[] getAllScriptures() {
            Scripture[] s_array = null;
            Cursor cursor = mDb.rawQuery("select * from " + ScriptureContract.ScriptureEntry.TABLE_NAME  , null);
            if (cursor != null && cursor.getCount()> 0) {
                s_array = new Scripture[cursor.getCount()];
                for (int i = 0; i < cursor.getCount(); i++) {
                    Scripture s = new Scripture();
                    if (cursor.moveToNext()) {
                        s.setId(cursor.getLong(cursor.getColumnIndex(ScriptureContract.ScriptureEntry._ID)));
                        s.setVerse(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_VERSE)));
                        s.setBook(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_BOOK)));
                        s.setChapter(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_CHAPTER)));
                        s.setReference(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_REFERENCE)));
                        s.setText(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_TEXT)));
                        s.setTranslation(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_TRANSLATION)));
                        s.setNumber_of_correct_reviews(cursor.getInt(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFCORRECTREVIEWS)));
                        s.setNumber_of_reviews(cursor.getInt(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFREVIEWS)));
                        s_array[i] = s;
                    }
                }
                cursor.close();
            }
            return s_array;
        }

        public Cursor getAllScripturesCursor() {
            Cursor cursor = mDb.rawQuery("select * from " + ScriptureContract.ScriptureEntry.TABLE_NAME  , null);
            return cursor;
        }

        public boolean delete(long id)
        {
            return mDb.delete(ScriptureContract.ScriptureEntry.TABLE_NAME, ScriptureContract.ScriptureEntry._ID + "=" + id, null) > 0;
        }

        public boolean updateScriptures(Scripture[] scriptures){
            try {
                for (Scripture s : scriptures) {
                    ContentValues cv = new ContentValues();
                    cv.put(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFREVIEWS, s.getNumber_of_reviews()); //These Fields should be your String values of actual column names
                    cv.put(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFCORRECTREVIEWS, s.getNumber_of_correct_reviews());
                    mDb.update(ScriptureContract.ScriptureEntry.TABLE_NAME, cv, ScriptureContract.ScriptureEntry._ID + "=" + s.getId(), null);
                }
                return true;
            }
            catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String SQL_CREATE_SCRIPTURES_TABLE = "CREATE TABLE " + ScriptureContract.ScriptureEntry.TABLE_NAME + " (" +
                    ScriptureContract.ScriptureEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFREVIEWS + " INTEGER NOT NULL, " +
                    ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFCORRECTREVIEWS + " INTEGER NOT NULL, " +
                    ScriptureContract.ScriptureEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                    ScriptureContract.ScriptureEntry.COLUMN_BOOK + " TEXT NOT NULL, " +
                    ScriptureContract.ScriptureEntry.COLUMN_CHAPTER + " TEXT NOT NULL, " +
                    ScriptureContract.ScriptureEntry.COLUMN_VERSE + " TEXT NOT NULL, " +
                    ScriptureContract.ScriptureEntry.COLUMN_REFERENCE + " TEXT NOT NULL, " +
                    ScriptureContract.ScriptureEntry.COLUMN_TRANSLATION + " TEXT NOT NULL " +
                    "); ";

            db.execSQL(SQL_CREATE_SCRIPTURES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + ScriptureContract.ScriptureEntry.TABLE_NAME);
            onCreate(db);

        }


    }

    @Override
    public boolean onCreate() {
        dbHelper = new ScriptureDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String orderBy = null;
        switch (sUriMatcher.match(uri)) {
            case SCRIPTURE:
                qb.setTables(ScriptureContract.ScriptureEntry.TABLE_NAME);
                qb.setProjectionMap(sScripturesProjectionMap);
                orderBy = ScriptureContract.ScriptureEntry.DEFAULT_SORT_ORDER;
                break;
            case SCRIPTURE_ID:
                qb.setTables(ScriptureContract.ScriptureEntry.TABLE_NAME);
                qb.setProjectionMap(sScripturesProjectionMap);
                qb.appendWhere(ScriptureContract.ScriptureEntry._ID + "=" + uri.getPathSegments().get(ScriptureContract.ScriptureEntry.SCRIPTURES_ID_PATH_POSITION));
                orderBy = ScriptureContract.ScriptureEntry.DEFAULT_SORT_ORDER;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SCRIPTURE:
                return ScriptureContract.ScriptureEntry.CONTENT_TYPE;
            case SCRIPTURE_ID:
                return ScriptureContract.ScriptureEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues initialValues) {
        if (
                sUriMatcher.match(uri) != SCRIPTURE) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        }
        else {
            values = new ContentValues();
        }
        long rowId = -1;
        Uri rowUri = Uri.EMPTY;
        switch (sUriMatcher.match(uri)) {
            case SCRIPTURE:
                if (values.containsKey(ScriptureContract.ScriptureEntry._ID) == false) {
                    values.put(ScriptureContract.ScriptureEntry._ID, "");
                }
                if (values.containsKey(ScriptureContract.ScriptureEntry.COLUMN_BOOK) == false) {
                    values.put(ScriptureContract.ScriptureEntry.COLUMN_BOOK, "");
                }
                if (values.containsKey(ScriptureContract.ScriptureEntry.COLUMN_CHAPTER) == false) {
                    values.put(ScriptureContract.ScriptureEntry.COLUMN_CHAPTER, "");
                }
                if (values.containsKey(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFCORRECTREVIEWS) == false) {
                    values.put(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFCORRECTREVIEWS, "");
                }
                if (values.containsKey(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFREVIEWS) == false) {
                    values.put(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFREVIEWS, "");
                }
                if (values.containsKey(ScriptureContract.ScriptureEntry.COLUMN_REFERENCE) == false) {
                    values.put(ScriptureContract.ScriptureEntry.COLUMN_REFERENCE, "");
                }
                if (values.containsKey(ScriptureContract.ScriptureEntry.COLUMN_TEXT) == false) {
                    values.put(ScriptureContract.ScriptureEntry.COLUMN_TEXT, "");
                }
                if (values.containsKey(ScriptureContract.ScriptureEntry.COLUMN_TRANSLATION) == false) {
                    values.put(ScriptureContract.ScriptureEntry.COLUMN_TRANSLATION, "");
                }
                if (values.containsKey(ScriptureContract.ScriptureEntry.COLUMN_VERSE) == false) {
                    values.put(ScriptureContract.ScriptureEntry.COLUMN_VERSE, "");
                }

                rowId = db.insert(ScriptureContract.ScriptureEntry.TABLE_NAME,
                        ScriptureContract.ScriptureEntry._ID,
                        values);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(ScriptureContract.ScriptureEntry.CONTENT_ID_URI_BASE, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
        }
        return rowUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String finalWhere;
        int count;
        switch (sUriMatcher.match(uri)) {
            case SCRIPTURE:
                count = db.delete(ScriptureContract.ScriptureEntry.TABLE_NAME, where, whereArgs);
                break;
            case SCRIPTURE_ID:
                finalWhere = ScriptureContract.ScriptureEntry._ID + " = " + uri.getPathSegments().get(ScriptureContract.ScriptureEntry.SCRIPTURES_ID_PATH_POSITION);
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.delete(ScriptureContract.ScriptureEntry.TABLE_NAME, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @NonNull ContentValues values, @NonNull String where, @NonNull String[] whereArgs) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int count;
            String finalWhere;
            String id;
            switch (sUriMatcher.match(uri)) {
                case SCRIPTURE:
                    count = db.update(ScriptureContract.ScriptureEntry.TABLE_NAME, values, where, whereArgs);
                    break;
                case SCRIPTURE_ID:
                    id = uri.getPathSegments().get(ScriptureContract.ScriptureEntry.SCRIPTURES_ID_PATH_POSITION);
                    finalWhere = ScriptureContract.ScriptureEntry._ID + " = " + id;
                    if (where !=null) {
                        finalWhere = finalWhere + " AND " + where;
                    }
                    count = db.update(ScriptureContract.ScriptureEntry.TABLE_NAME, values, finalWhere, whereArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
    }


}
