package com.example.android.heedn.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.heedn.models.Scripture;

public class ScriptureDbHelper extends SQLiteOpenHelper {

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
        cv.put(ScriptureContract.ScriptureEntry.REFERENCE, scripture.getReference());
        cv.put(ScriptureContract.ScriptureEntry.TEXT, scripture.getText());
        cv.put(ScriptureContract.ScriptureEntry.TRANSLATION, scripture.getTranslation());
        cv.put(ScriptureContract.ScriptureEntry.CHAPTER, scripture.getChapter());
        cv.put(ScriptureContract.ScriptureEntry.BOOK, scripture.getBook());
        cv.put(ScriptureContract.ScriptureEntry.VERSE, scripture.getVerse());
        cv.put(ScriptureContract.ScriptureEntry.NUMBEROFCORRECTREVIEWS, scripture.getNumber_of_correct_reviews());
        cv.put(ScriptureContract.ScriptureEntry.NUMBEROFREVIEWS, scripture.getNumber_of_reviews());
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
        Cursor cursor = mDb.rawQuery("select * from " + ScriptureContract.ScriptureEntry.TABLE_NAME + " where " + ScriptureContract.ScriptureEntry.REFERENCE + "='" + ref+ "'" , null);
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
                s.setVerse(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.VERSE)));
                s.setBook(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.BOOK)));
                s.setChapter(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.CHAPTER)));
                s.setReference(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.REFERENCE)));
                s.setText(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.TEXT)));
                s.setTranslation(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.TRANSLATION)));
                s.setNumber_of_correct_reviews(cursor.getInt(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.NUMBEROFCORRECTREVIEWS)));
                s.setNumber_of_reviews(cursor.getInt(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.NUMBEROFREVIEWS)));
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
                    s.setVerse(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.VERSE)));
                    s.setBook(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.BOOK)));
                    s.setChapter(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.CHAPTER)));
                    s.setReference(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.REFERENCE)));
                    s.setText(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.TEXT)));
                    s.setTranslation(cursor.getString(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.TRANSLATION)));
                    s.setNumber_of_correct_reviews(cursor.getInt(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.NUMBEROFCORRECTREVIEWS)));
                    s.setNumber_of_reviews(cursor.getInt(cursor.getColumnIndex(ScriptureContract.ScriptureEntry.NUMBEROFREVIEWS)));
                    s_array[i] = s;
                }
            }
            cursor.close();
        }
        return s_array;
    }

    public boolean delete(long id)
    {
        return mDb.delete(ScriptureContract.ScriptureEntry.TABLE_NAME, ScriptureContract.ScriptureEntry._ID + "=" + id, null) > 0;
    }

    public boolean updateScriptures(Scripture[] scriptures){
        try {
            for (Scripture s : scriptures) {
                ContentValues cv = new ContentValues();
                cv.put(ScriptureContract.ScriptureEntry.NUMBEROFREVIEWS, s.getNumber_of_reviews()); //These Fields should be your String values of actual column names
                cv.put(ScriptureContract.ScriptureEntry.NUMBEROFCORRECTREVIEWS, s.getNumber_of_correct_reviews());
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
                ScriptureContract.ScriptureEntry.NUMBEROFREVIEWS + " INTEGER NOT NULL, " +
                ScriptureContract.ScriptureEntry.NUMBEROFCORRECTREVIEWS + " INTEGER NOT NULL, " +
                ScriptureContract.ScriptureEntry.TEXT + " TEXT NOT NULL, " +
                ScriptureContract.ScriptureEntry.BOOK + " TEXT NOT NULL, " +
                ScriptureContract.ScriptureEntry.CHAPTER + " TEXT NOT NULL, " +
                ScriptureContract.ScriptureEntry.VERSE + " TEXT NOT NULL, " +
                ScriptureContract.ScriptureEntry.REFERENCE + " TEXT NOT NULL, " +
                ScriptureContract.ScriptureEntry.TRANSLATION + " TEXT NOT NULL " +
                "); ";

        db.execSQL(SQL_CREATE_SCRIPTURES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ScriptureContract.ScriptureEntry.TABLE_NAME);
        onCreate(db);

    }
}
