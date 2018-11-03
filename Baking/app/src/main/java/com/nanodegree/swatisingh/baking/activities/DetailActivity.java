package com.nanodegree.swatisingh.baking.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nanodegree.swatisingh.baking.R;
import com.nanodegree.swatisingh.baking.model.Recipe;

public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //add up navigation
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Recipe recipe = getIntent().getParcelableExtra("parcel_data");
        setTitle(recipe.title);
        int position = getIntent().getIntExtra("position", 0);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelable("parcel_data", recipe);

        if (savedInstanceState == null){
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.detail_container, detailFragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
