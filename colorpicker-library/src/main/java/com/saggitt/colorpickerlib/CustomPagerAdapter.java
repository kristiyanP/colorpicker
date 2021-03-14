/*
 *  This file is part of ColorPickerLib
 *  Copyright (c) 2021   Saul Henriquez
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.saggitt.colorpickerlib;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

public class CustomPagerAdapter extends PagerAdapter {
    private View []mLists;
    private String []mTitles;

    public CustomPagerAdapter(View []lists, String []titles){
        mLists = lists;
        mTitles = titles;
    }

    @Override
    public int getCount() {
        if(mLists!=null)
            return mLists.length;
        else
            return 0;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position<0||position>=mTitles.length){
            return "";
        }
        else{
            return mTitles[position];
        }
    }
}
