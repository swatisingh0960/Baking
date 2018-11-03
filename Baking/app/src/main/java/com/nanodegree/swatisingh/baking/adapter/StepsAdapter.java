package com.nanodegree.swatisingh.baking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.swatisingh.baking.R;
import com.nanodegree.swatisingh.baking.model.Recipe;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> steps;
    private Recipe recipe;
    private StepsOnClickistener stepsOnClickistener;


    public interface StepsOnClickistener {
        void onStepSelected(int position, Recipe recipe);
    }

    public StepsAdapter (Context context, ArrayList<String> steps, Recipe recipe){
        this.context = context;
        this.steps = steps;
        this.recipe = recipe;
        stepsOnClickistener = (StepsOnClickistener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_steps, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String step = steps.get(i);
        viewHolder.textView.setText(step);
        viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepsOnClickistener.onStepSelected(viewHolder.getAdapterPosition(), recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private View parentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.parentView = itemView;
            this.textView = itemView.findViewById(R.id.step_item);
        }
    }
}
