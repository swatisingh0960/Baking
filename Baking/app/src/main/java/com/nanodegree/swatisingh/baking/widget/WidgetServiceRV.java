package com.nanodegree.swatisingh.baking.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class WidgetServiceRV extends RemoteViewsService{


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        ArrayList<String> recipe = intent.getStringArrayListExtra("bundle");
        return new WidgetFactoryRV(this.getApplicationContext(),intent,recipe);
    }
}
