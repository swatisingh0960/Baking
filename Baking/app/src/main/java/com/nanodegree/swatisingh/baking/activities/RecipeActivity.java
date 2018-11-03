package com.nanodegree.swatisingh.baking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nanodegree.swatisingh.baking.R;
import com.nanodegree.swatisingh.baking.adapter.StepsAdapter;
import com.nanodegree.swatisingh.baking.model.Recipe;

public class RecipeActivity extends AppCompatActivity implements StepsAdapter.StepsOnClickistener{
    Boolean dualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Recipe recipe = getIntent().getParcelableExtra("parcel_data");
        setTitle(recipe.title);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        if (findViewById(R.id.recipe_activity_ll) != null) {
            dualPane = true;

            if (savedInstanceState == null) {
/* OLD one                 FragmentManager fragmentManager = getSupportFragmentManager();
                DetailFragment detailFragment = new DetailFragment();
                fragmentManager.beginTransaction().add(R.id.detail_container, detailFragment).commit();*/
                this.fiindOrCreFragm();
            }
        } else {
            dualPane = false;
        }

    }

    @Override
    public void onStepSelected(int position, Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelable("parcel_data", recipe);

        if (dualPane){
            DetailFragment newFragment = new DetailFragment();
            newFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.detail_container, newFragment).commit();
        } else {
            final Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    private Fragment fiindOrCreFragm(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("RECIPE_FRAG");
        if (fragment ==null){
            fragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, fragment,"RECIPE_FRAG").commit();
        }
        return fragment;
    }
}
