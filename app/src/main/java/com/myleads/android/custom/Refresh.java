package com.myleads.android.custom;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.myleads.android.R;


public class Refresh {
    private LinearLayout layout;
    ViewGroup otherViews;
    public Refresh(LinearLayout layout,ViewGroup otherViews){

        this.layout = layout;
        this.otherViews = otherViews;
    }

    public void showRefreshLayout(){
        //The other view is the layout for all other views.we need to hide it so only progressbar will show
        layout.setVisibility(View.VISIBLE);
        otherViews.setVisibility(View.GONE);
    }
    public void dismissRefreshLayout(){
        layout.setVisibility(View.GONE);
        otherViews.setVisibility(View.VISIBLE);
    }

    public ImageButton getRefreshButton(){
        ImageButton refresh = layout.findViewById(R.id.refresh);
        return refresh;
    }
}
