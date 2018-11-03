package com.nanodegree.swatisingh.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nanodegree.swatisingh.baking.R;

import java.util.ArrayList;

public class WidgetFactoryRV implements RemoteViewsService.RemoteViewsFactory{
    private Context context;
    private ArrayList<String> factoryArrayList = new ArrayList<>();

    public WidgetFactoryRV(Context appContext, Intent intent, ArrayList<String> recipe){
        context = appContext;

        if (recipe != null)
            factoryArrayList = recipe;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long idToken = Binder.clearCallingIdentity();
        Binder.restoreCallingIdentity(idToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return factoryArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (i == AdapterView.INVALID_POSITION) {
            return null;
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item);
        remoteViews.setTextViewText(R.id.ingredient_item, factoryArrayList.get(i));
        return remoteViews;
    }



    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
