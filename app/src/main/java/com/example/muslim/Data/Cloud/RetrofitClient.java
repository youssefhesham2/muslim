package com.example.muslim.Data.Cloud;

import com.example.muslim.Models.AzanModel;
import com.example.muslim.Models.QuranModel;
import com.example.muslim.Models.QuranModelapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public final static String base_url="https://unpkg.com/";
    static RetrofitClient retrofitclit;
    QuranHelper azanHelper;
    private RetrofitClient() {
        Retrofit retrofit=new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        azanHelper=retrofit.create(QuranHelper.class);
    }

    public static RetrofitClient getInstance(){

        if (retrofitclit==null)
        {
            retrofitclit=new RetrofitClient();
        }
        return retrofitclit;
    }
    public Call<QuranModel[]> get(){
       return azanHelper.get();

    }

}
