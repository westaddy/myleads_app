package com.myleads.android.custom;

import android.app.DialogFragment;
import android.app.FragmentManager;

import com.myleads.android.activities.dialog.LoaderDialogFragment;

public class LoaderDialog {
    private  DialogFragment progressDialog;

    public LoaderDialog(){
        progressDialog = new LoaderDialogFragment();
    }
    public void show(FragmentManager manager){
        progressDialog.show(manager,"loader");
    }

    public void dismiss(){
        progressDialog.dismiss();
    }



}
