package com.example.android.heedn.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.heedn.data.ScriptureContract;

import java.util.Dictionary;
import java.util.HashMap;

public class Scripture implements Parcelable {

    long id;
    String reference;
    String translation;

    String book;
    String chapter;
    String verse;

    String text;
    int number_of_reviews;
    int number_of_correct_reviews;

    public Scripture(Parcel in ) {
        readFromParcel( in );
    }
    public Scripture(){}

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Scripture createFromParcel(Parcel in ) {
            return new Scripture( in );
        }
        public Scripture[] newArray(int size) {
            return new Scripture[size];
        }
    };


    @Override
    public String toString() {
        return reference;
    }

    public String getReviewsString() {
        return "Reviewed "+ number_of_reviews +" times ("+ number_of_correct_reviews +" correct)";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber_of_reviews() {
        return number_of_reviews;
    }

    public void setNumber_of_reviews(int number_of_reviews) {
        this.number_of_reviews = number_of_reviews;
    }

    public int getNumber_of_correct_reviews() {
        return number_of_correct_reviews;
    }

    public void setNumber_of_correct_reviews(int number_of_correct_reviews) {
        this.number_of_correct_reviews = number_of_correct_reviews;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public boolean isValidScripture(){
        return this != null
                && book != null && book.trim() != null
                && chapter != null && chapter.trim() != null
                && verse != null && verse.trim() != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reference);
        dest.writeString(translation);
        dest.writeString(book);
        dest.writeString(chapter);
        dest.writeString(verse);
        dest.writeString(text);
        dest.writeInt(number_of_reviews);
        dest.writeInt(number_of_correct_reviews);
    }

    private void readFromParcel(Parcel in) {
        reference = in.readString();
        text = in.readString();
        translation = in.readString();
        book = in.readString();
        chapter = in.readString();
        verse = in.readString();
        number_of_reviews = in.readInt();
        number_of_correct_reviews = in.readInt();
    }

    public ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        Long theId = null;
        if(this.getId()>0) {
            theId = this.getId();
        }
        result.put(ScriptureContract.ScriptureEntry._ID, theId);
        result.put(ScriptureContract.ScriptureEntry.COLUMN_VERSE, this.getVerse());
        result.put(ScriptureContract.ScriptureEntry.COLUMN_TRANSLATION, this.getTranslation());
        result.put(ScriptureContract.ScriptureEntry.COLUMN_TEXT, this.getText());
        result.put(ScriptureContract.ScriptureEntry.COLUMN_REFERENCE, this.getReference());
        result.put(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFREVIEWS, this.getNumber_of_reviews());
        result.put(ScriptureContract.ScriptureEntry.COLUMN_NUMBEROFCORRECTREVIEWS, this.getNumber_of_correct_reviews());
        result.put(ScriptureContract.ScriptureEntry.COLUMN_CHAPTER, this.getChapter());
        result.put(ScriptureContract.ScriptureEntry.COLUMN_BOOK, this.getBook());
        return  result;
    }
}
