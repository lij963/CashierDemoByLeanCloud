package com.etam.cashier.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Author:  admin
 * Date:    2017/2/9.
 * Description:
 */

public class CashierPageAdapter extends PagerAdapter{
    ArrayList<View> viewList;
    public CashierPageAdapter(ArrayList<View> viewList) {
        this.viewList=viewList;
    }

    @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {

            return Integer.MAX_VALUE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position % viewList.size()));

        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "";
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position % viewList.size()));
            return viewList.get(position % viewList.size());
        }

}
