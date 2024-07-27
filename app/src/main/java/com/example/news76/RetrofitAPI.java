package com.example.news76;
import retrofit2.http.GET;
import retrofit2.http.Url;
import retrofit2.Call;

public interface RetrofitAPI {
    @GET
    Call<NewsModal> getAllNews(@Url String url);
    @GET
    Call<NewsModal> getNewsByCategory(@Url String url);
}
