package com.nanodegree.swatisingh.baking.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.nanodegree.swatisingh.baking.R;
import com.nanodegree.swatisingh.baking.adapter.RecipeAdapter;
import com.nanodegree.swatisingh.baking.model.Recipe;
import com.nanodegree.swatisingh.baking.widget.WidgetProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BakingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking);
        requestRecipeInfo();
    }

    private boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void requestRecipeInfo() {
        String recipeUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        final ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.clear();

        if (isOnline()){
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(recipeUrl).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (!response.isSuccessful()){
                        throw new IOException("That's not right..." + response);
                    }
                        try {
                            JSONArray jsonArray = new JSONArray(response.body().string());
                            for (int j=0; j <jsonArray.length(); j++){
                                int id;
                                String name;
                                ArrayList<String> ingredient = new ArrayList<>();
                                ArrayList<String> shortDesc = new ArrayList<>();
                                ArrayList<String> desc = new ArrayList<>();
                                ArrayList<String> media = new ArrayList<>();
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                id = jsonObject.getInt("id");
                                name = jsonObject.getString("name");

                                JSONArray ingredients = jsonObject.getJSONArray("ingredients");
                                for (int i=0; i< ingredients.length();i++){
                                    JSONObject ingredientsObject = ingredients.getJSONObject(i);
                                    ingredient.add(ingredientsObject.getInt("quantity") + " " + ingredientsObject.getString("measure") + " " + ingredientsObject.getString("ingredient"));
                                }

                                JSONArray steps = jsonObject.getJSONArray("steps");
                                for (int i = 0;i<steps.length(); i++){
                                    JSONObject step = steps.getJSONObject(i);
                                    shortDesc.add(step.getString("shortDescription"));
                                }

                                for (int i=0; i <steps.length(); i++){
                                    JSONObject step = steps.getJSONObject(i);
                                    desc.add(String.format("%d %s",step.getInt("id"),step.getString("description")));
                                }
                                for (int i=0; i< steps.length(); i++){
                                    JSONObject step = steps.getJSONObject(i);
                                    String tempUrl  = step.getString("videoURL");
                                    if (tempUrl.equals(""))
                                        media.add(step.getString("thumbnailURL"));
                                    else
                                        media.add(step.getString("videoURL"));
                                }

                                Recipe recipe = new Recipe(id, name, ingredient, shortDesc, desc, media);
                                recipes.add(recipe);
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e){
                            e.printStackTrace();
                        }

                        BakingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RecipeAdapter recipeAdapter = new RecipeAdapter(getApplicationContext(), recipes);

                                GridView gridView = findViewById(R.id.recipesGridView);
                                gridView.setAdapter(recipeAdapter);
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Recipe recipeClicked = recipes.get(i);
                                        WidgetProvider.sendFresh(getApplicationContext(), recipeClicked);
                                        Intent intent =new Intent(getApplicationContext(), RecipeActivity.class);
                                        intent.putExtra("parcel_data", recipeClicked);
                                        startActivity(intent);
                                    }
                                });


                            }
                        });

                    }

                });
            } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
        }
}

