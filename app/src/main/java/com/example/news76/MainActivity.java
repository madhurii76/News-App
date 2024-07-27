package com.example.news76;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements  CategoryRVAdapter.CategoryClickInterface {


    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles>articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        newsRV=findViewById(R.id.idRVNews);
        categoryRV=findViewById(R.id.idRVCategories);
        loadingPB=findViewById(R.id.idPBLoading);
        articlesArrayList=new ArrayList<>();
        categoryRVModalArrayList=new ArrayList<>();
        newsRVAdapter=new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter=new CategoryRVAdapter(categoryRVModalArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }
    private void getCategories(){


        categoryRVModalArrayList.add(new CategoryRVModal("All","https://images.unsplash.com/photo-1585776245991-cf89dd7fc73a?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://cdn.pixabay.com/photo/2015/02/02/11/09/office-620822_1280.jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Science","https://images.unsplash.com/photo-1614935151651-0bea6508db6b?q=80&w=1525&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryRVModal("Sports","https://images.unsplash.com/photo-1579952363873-27f3bade9f55?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryRVModal("General","https://images.unsplash.com/photo-1512314889357-e157c22f938d?q=80&w=1471&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryRVModal("Business","https://images.unsplash.com/photo-1563986768711-b3bde3dc821e?q=80&w=1468&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryRVModal("Entertainment","https://images.unsplash.com/photo-1598899134739-24c46f58b8c0?q=80&w=1456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryRVModal("Health","https://images.unsplash.com/photo-1506126613408-eca07ce68773?q=80&w=1399&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        categoryRVAdapter.notifyDataSetChanged();
    }
        private void getNews(String category){

            loadingPB.setVisibility(View.VISIBLE);
            articlesArrayList.clear();

            String categoryURL="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apikey=303ac28c4c0e45ceb105187b9676e58e";
            String url="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apikey=303ac28c4c0e45ceb105187b9676e58e";
            String BASE_URL="https://newsapi.org/";
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);
            Call<NewsModal> call;
            if(category.equals("All")){
                call=retrofitAPI.getAllNews(url);

            }
            else{
                call=retrofitAPI.getNewsByCategory(categoryURL);
            }
            call.enqueue(new Callback<NewsModal>() {
                @Override
                public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                    NewsModal newsModal=response.body();
                    loadingPB.setVisibility(View.GONE);
                    ArrayList<Articles> articles=newsModal.getArticles();
                    for(int i=0;i<articles.size();i++){
                        articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));

                    }
                    newsRVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<NewsModal> call, Throwable t) {
                    Toast.makeText(MainActivity.this,"Fail to get News", Toast.LENGTH_SHORT).show();
                }
            });        }
    @Override
    public void onCategoryClick(int position) {
        String category=categoryRVModalArrayList.get(position).getCategory();
        getNews(category);
    }
}