package com.example.muslim.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class QuranModel {


    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("surah_number")
    @Expose
    @ColumnInfo(name ="surah_number")
    private int surahNumber;
    @ColumnInfo(name ="verse_number")
    @SerializedName("verse_number")
    @Expose
    private int verseNumber;
    @ColumnInfo(name ="text")
    @SerializedName("text")
    @Expose
    private String text;
    @ColumnInfo(name ="translation")
    @SerializedName("translation")
    @Expose
    private String translation;

    public QuranModel(int surahNumber, int verseNumber, String text, String translation) {
        this.surahNumber = surahNumber;
        this.verseNumber = verseNumber;
        this.text = text;
        this.translation = translation;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public void setSurahNumber(int surahNumber) {
        this.surahNumber = surahNumber;
    }

    public int getVerseNumber() {
        return verseNumber;
    }

    public void setVerseNumber(int verseNumber) {
        this.verseNumber = verseNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /*

    @ColumnInfo(name ="aya_name")
    String aya_name;

    @ColumnInfo(name ="verse_number")
    int verse_number;

    @ColumnInfo(name ="text")
    String text;

    @ColumnInfo(name ="translation")
    String translation;



    public QuranModel(String aya_name, int verse_number, String text, String translation, int aya_saved_number) {
        this.aya_name = aya_name;
        this.verse_number = verse_number;
        this.text = text;
        this.translation = translation;
    }



    public int getVerse_number() {
        return verse_number;
    }

    public void setVerse_number(int verse_number) {
        this.verse_number = verse_number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }


    public String getAya_name() {
        return aya_name;
    }

    public void setAya_name(String aya_name) {
        this.aya_name = aya_name;
    }*/
}
