package com.nanodegree.swatisingh.baking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nanodegree.swatisingh.baking.R;
import com.nanodegree.swatisingh.baking.model.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    public RecipeAdapter(Context context, ArrayList<Recipe> recipes){
        super(context,0, recipes);
    }

    public View getView(final int position, View changeView, ViewGroup parent){
        Recipe recipe = getItem(position);
        TextView textView;
        if (changeView == null){
            changeView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_layout,parent, false);
        }

        textView = changeView.findViewById(R.id.recipe_item);
        textView.setText(recipe.title);
        textView.setTextColor(Color.BLUE);
        return textView;
    }
}
