package com.eric.flowlayout;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * create by Eric
 * on 20-8-13
 */
public abstract class BaseAdapter {

    private static List<OnRefresh> onRefreshes = new ArrayList<>();

    public static void addReObj(OnRefresh refresh){
        if (!onRefreshes.contains(refresh)){
            onRefreshes.add(refresh);
        }
    }

    public static void removeReObj(OnRefresh refresh){
        onRefreshes.remove(refresh);
    }

    public static void notifyDataSetChanged(){
        for (OnRefresh onRefresh : onRefreshes){
            onRefresh.refresh();
        }
    }

    public static void clearReList(){
        onRefreshes.clear();
    }

    public interface OnRefresh{
        void refresh();
    }

//    public interface ViewAdapter {

        abstract int getCount();

        abstract <T extends View> T getView(int position);
//    }
}
