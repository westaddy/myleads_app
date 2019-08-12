package com.roughike.bottombar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/*
 * BottomBar library for Android
 * Copyright (c) 2016 Iiro Krankka (http://github.com/roughike).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class BottomBarBadge extends AppCompatTextView{
    private int count;
    private boolean isVisible = false;
    private Context context;

    BottomBarBadge(Context context) {
        super(context);
        this.context = context;
        this.setIncludeFontPadding(false);
    }

    /**
     * Set the unread / new item / whatever count for this Badge.
     *
     * @param count the value this Badge should show.
     */
    void setCount(int count) {
        this.count = count;
        setText(String.valueOf(count));
    }

    /**
     * Get the currently showing count for this Badge.
     *
     * @return current count for the Badge.
     */
    int getCount() {
        return count;
    }

    /**
     * Shows the badge with a neat little scale animation.
     */
    void show() {
        isVisible = true;
        ViewCompat.animate(this)
                .setDuration(150)
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .start();
    }

    /**
     * Hides the badge with a neat little scale animation.
     */
    void hide() {
        isVisible = false;
        ViewCompat.animate(this)
                .setDuration(150)
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .start();
    }

    /**
     * Is this badge currently visible?
     *
     * @return true is this badge is visible, otherwise false.
     */
    boolean isVisible() {
        return isVisible;
    }

    void attachToTab(BottomBarTab tab, int backgroundColor) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params.setMargins(0,0,MiscUtils.dpToPixel(context, 20),0);

        setLayoutParams(params);
        setGravity(Gravity.CENTER);
        MiscUtils.setTextAppearance(this, R.style.BB_BottomBarBadge_Text);

        setColoredBackground(backgroundColor);
        wrapTabAndBadgeInSameContainer(tab);
    }

    void setColoredBackground(int circleColor) {
        int innerPadding = MiscUtils.dpToPixel(getContext(), 1);
        ShapeDrawable backgroundCircle;
        backgroundCircle = BadgeRoundedRectangle.make(20f,20f,20f,20f, circleColor);
        backgroundCircle.setPadding(MiscUtils.dpToPixel(getContext(), 5),MiscUtils.dpToPixel(getContext(), 2),MiscUtils.dpToPixel(getContext(), 5),MiscUtils.dpToPixel(getContext(), 2));
        /*else{
            backgroundCircle = BadgeCircle.make(innerPadding * 3, circleColor);
            setPadding(innerPadding, innerPadding, innerPadding, innerPadding);
        }*/
        backgroundCircle.getPaint().setAntiAlias(true);
        setBackgroundCompat(backgroundCircle);
    }

    private void wrapTabAndBadgeInSameContainer(final BottomBarTab tab) {
        ViewGroup tabContainer = (ViewGroup) tab.getParent();
        tabContainer.removeView(tab);

        final BadgeContainer badgeContainer = new BadgeContainer(getContext());
        badgeContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        badgeContainer.addView(tab);
        badgeContainer.addView(this);

        tabContainer.addView(badgeContainer, tab.getIndexInTabContainer());

        badgeContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                badgeContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                adjustPositionAndSize(tab);
            }
        });
    }

    void removeFromTab(BottomBarTab tab) {
        BadgeContainer badgeAndTabContainer = (BadgeContainer) getParent();
        ViewGroup originalTabContainer = (ViewGroup) badgeAndTabContainer.getParent();

        badgeAndTabContainer.removeView(tab);
        originalTabContainer.removeView(badgeAndTabContainer);
        originalTabContainer.addView(tab, tab.getIndexInTabContainer());
    }

    void adjustPositionAndSize(BottomBarTab tab) {
        AppCompatImageView iconView = tab.getIconView();
        ViewGroup.LayoutParams params = getLayoutParams();

        int size;
        float xOffset;

        if(getScreenWidth() <= 320){

            size = Math.max(getWidth(), getHeight());
            xOffset = (float) (iconView.getWidth() / 4.25);

            setX(iconView.getX() + xOffset);
            setTranslationY(6);
        }
        else{
            size = Math.max(getWidth(), getHeight());
            xOffset = (float) (iconView.getWidth() / 3.25);

            setX(iconView.getX() + xOffset);
            setTranslationY(10);
        }

        /*if (params.width != size || params.height != size) {
            params.width = size;
            params.height = size;
            setLayoutParams(params);
        }*/
    }

    private int getScreenWidth(){

        return Resources.getSystem().getDisplayMetrics().widthPixels;

    }


    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(background);
        } else {
            setBackgroundDrawable(background);
        }
    }


}