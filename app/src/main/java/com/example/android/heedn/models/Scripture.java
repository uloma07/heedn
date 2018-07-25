package com.example.android.heedn.models;

public class Scripture {

    long id;
    String reference;
    String translation;

    String book;
    String chapter;
    String verse;

    String text;
    int number_of_reviews;
    int number_of_correct_reviews;

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
}
