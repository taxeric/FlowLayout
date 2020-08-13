package com.eric.flowlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * create by Eric
 * on 20-8-11
 */
public class FlowAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> data;

    public FlowAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    public void notifyDataSet(){
        BaseAdapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public TextView getView(int position){
        TextView show = (TextView) LayoutInflater.from(context).inflate(R.layout.text_layout, null);
        show.setText(data.get(position));
        return show;
    }
}
