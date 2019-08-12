package com.myleads.android.custom;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class Loader {
    public LinearLayout layout;
    public ViewGroup otherView;
    public Loader(LinearLayout layout,ViewGroup otherView){

        this.layout = layout;
        this.otherView = otherView;
    }

    public void showLoader(){
        //The other view is the layout for all other views.we need to hide it so only progressbar will show
        otherView.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);

    }
    public void dismissLoader(){
        layout.setVisibility(View.GONE);
        otherView.setVisibility(View.VISIBLE);
    }
}
