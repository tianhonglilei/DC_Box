package com.zhang.box.utils;

import android.support.v4.view.ViewPager;
import android.view.View;

public class NonPageTransformer implements ViewPager.PageTransformer{
	
    @Override
    public void transformPage(View page, float position)
    {
        page.setScaleX(0.999f);
    }

    public static final ViewPager.PageTransformer INSTANCE = new NonPageTransformer();
}
