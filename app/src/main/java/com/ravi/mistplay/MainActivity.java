package com.ravi.mistplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    List<SearchData> searchDataList;
    SearchDataAdapter adapter;
    EditText searchET;
    String gameJSONData;

    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        searchET = findViewById(R.id.edit_text_search);
        recyclerView = (RecyclerView) findViewById(R.id.search_data_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.refresh_layout);
        //initializing the searchDataList
        searchDataList = new ArrayList<>();

        try {
            System.out.println("game json: " + Utility.loadJSONFromAsset(getApplicationContext()));
            //displayDataToRecyclerView(loadJSONFromAsset(getApplicationContext()));
            gameJSONData = Utility.loadJSONFromAsset(getApplicationContext());
        }catch (IOException e){
            e.printStackTrace();
        }


        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String searchText=null;
                if(s.length() != 0)
                    searchText = searchET.getText().toString();
                    System.out.println("search Text: "+searchText);

                    if(searchText!=null) {
                        try {
                            gameJSONData = Utility.getSearchedGameJSONdata(searchText, getApplicationContext());
                            if (gameJSONData != null) {
                                searchDataList = new ArrayList<>();
                                populateData(gameJSONData);
                                initAdapter();
                                initScrollListener();

                            } else {
                                Toast.makeText(getApplicationContext(), "No game found", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            gameJSONData = Utility.loadJSONFromAsset(getApplicationContext());
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

            }
        });
        try{
            populateData(gameJSONData);
            initAdapter();
            initScrollListener();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    /*public void displayDataToRecyclerView(String result){
        try {
            if(result != null) {
                JSONArray gameData = new JSONArray(result);
                if (gameData.length() != 0) {

                    System.out.println("game111: " + gameData.get(0));

                    JSONObject gameJSONObject;
                    for (int i = 0; i < gameData.length(); i++) {
                        gameJSONObject = gameData.getJSONObject(i);

                        searchDataList.add(new SearchData(gameJSONObject.getString("genre"),
                                gameJSONObject.getString("imgURL"),
                                gameJSONObject.getString("subgenre"),
                                gameJSONObject.getString("title"),
                                gameJSONObject.getString("pid"),
                                gameJSONObject.getString("rating"),
                                gameJSONObject.getString("rCount")));
                    }
                    adapter = new SearchDataAdapter(searchDataList);
                    recyclerView.setAdapter(adapter);
                    refreshLayout.setRefreshing(false);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not available any Game data.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


    private void populateData(String gameJSONData) throws  IOException{
        int j = 0;
        try {

            JSONArray gameData = new JSONArray(gameJSONData);

                JSONObject gameJSONObject;
                while (j < 5 && j<gameData.length()) {

                    gameJSONObject = gameData.getJSONObject(j);

                    searchDataList.add(new SearchData(gameJSONObject.getString("genre"),
                            gameJSONObject.getString("imgURL"),
                            gameJSONObject.getString("subgenre"),
                            gameJSONObject.getString("title"),
                            gameJSONObject.getString("pid"),
                            gameJSONObject.getString("rating"),
                            gameJSONObject.getString("rCount")));

                    j++;
                }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        adapter = new SearchDataAdapter(searchDataList);
        recyclerView.setAdapter(adapter);
    }

    private void initScrollListener() throws  IOException{
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == searchDataList.size() - 1) {
                        //bottom of list!
                        try{
                            loadMore();
                            isLoading = true;
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }
                }
            }
        });


    }

    private void loadMore() throws  IOException{
        searchDataList.add(null);

        recyclerView.post(new Runnable() {
            public void run() {
                adapter.notifyItemInserted(searchDataList.size() - 1);
            }
        });
        //adapter.notifyItemInserted(searchDataList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchDataList.remove(searchDataList.size() - 1);
                int scrollPosition = searchDataList.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 5 ;

                while (currentSize - 1 < nextLimit) {
                    try {
                        searchDataList.add(fetchCurrentData(gameJSONData, currentSize));
                        currentSize++;
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }

    public SearchData fetchCurrentData(String gameJSONData, int currentIndex) throws IOException{
        SearchData searchData = null;
       try {
            //String gameJSONData = Utility.loadJSONFromAsset(getApplicationContext());
            JSONArray gameData = new JSONArray(gameJSONData);
            JSONObject gameJSONObject;

                gameJSONObject = gameData.getJSONObject(currentIndex);
                searchData = new SearchData(gameJSONObject.getString("genre"),
                        gameJSONObject.getString("imgURL"),
                        gameJSONObject.getString("subgenre"),
                        gameJSONObject.getString("title"),
                        gameJSONObject.getString("pid"),
                        gameJSONObject.getString("rating"),
                        gameJSONObject.getString("rCount"));


        }catch (JSONException e) {
            e.printStackTrace();
        }

        return searchData;

    }
}
