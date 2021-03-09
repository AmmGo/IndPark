package com.hl.indpark.uis.adapters;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hl.indpark.uis.uihelpers.IPageContent;
import com.hl.indpark.uis.uihelpers.IPageTitle;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/20 17:57
 * Function：
 * Desc：
 */
public class TabPageAdapter<T extends IPageTitle> extends FragmentStatePagerAdapter {
    private List<T> items;
    private IPageContent pageContent;

    public TabPageAdapter(FragmentManager fm, List<T> items, IPageContent pageContent) {
        super(fm);
        this.items = items;
        this.pageContent = pageContent;
    }

    @Override
    public Fragment getItem(int i) {
        if (pageContent == null) {
            return null;
        }
        return pageContent.getItem(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (items == null || items.size() < position || position < 0) {
            return "";
        }
        return items.get(position).getTitle();
    }

    @Override
    public int getCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }
}
