package com.myleads.android.custom;

import android.text.Html;
import android.text.Spanned;


public class Util {

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static String getSomeWords(String text,int length){

        if(text.length() > length)
        {
            return text.substring(0,length)+ "...";
        }
        else{
            return text;
        }

    }



}
