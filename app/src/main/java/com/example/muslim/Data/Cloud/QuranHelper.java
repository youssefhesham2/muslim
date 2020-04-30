package com.example.muslim.Data.Cloud;

import com.example.muslim.Models.AzanModel;
import com.example.muslim.Models.QuranModel;
import com.example.muslim.Models.QuranModelapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuranHelper {

    @GET("quran-json@1.0.1/json/quran/en.json")
    Call<QuranModel[]> get();
}
