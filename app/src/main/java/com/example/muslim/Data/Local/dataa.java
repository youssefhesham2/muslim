package com.example.muslim.Data.Local;
import com.example.muslim.Models.EntintyModel;
import com.example.muslim.Models.QuranModel;
import com.example.muslim.Models.QuranModelapi;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface dataa
{
    @Insert
   void insert(EntintyModel...entintyModels);
    @Query("SELECT * FROM EntintyModel")
    List<EntintyModel> getall();
    @Query("DELETE FROM EntintyModel")
    void delete();


    @Insert(entity = QuranModel.class)
    void insertquran(QuranModel...quranmodel);

    @Query("SELECT * FROM quranmodel")
    List<QuranModel> getallquran();

    @Query("DELETE FROM quranmodel")
    void deletequran();

    @Query("SELECT * FROM quranmodel " + "WHERE text LIKE '%' || :text  || '%' " + "OR translation LIKE '%' || :text  || '%'")
    List<QuranModel> searchInQuran(String text);

}
