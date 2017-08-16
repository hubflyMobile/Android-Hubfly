package com.hubfly.ctq.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hubfly.ctq.Model.CustomerModel;
import com.hubfly.ctq.R;

import java.util.ArrayList;


/**
 * Created by vis-1328 on 8/20/2015.
 */
public class NewQacListAdapter extends BaseAdapter {
    //View Elements
    LayoutInflater inf;
    Context context;
    ArrayList<CustomerModel> resultArray = new ArrayList<CustomerModel>();


    public NewQacListAdapter(Context context, ArrayList<CustomerModel> dateList) {
        this.context = context;
        this.resultArray = dateList;
    }


    @Override
    public int getCount() {
        return resultArray.size();
    }

    @Override
    public Object getItem(int position) {
        return resultArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder ho;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            ho = new Holder();
            convertView = mInflater.inflate(R.layout.adapter_category, null);
            //Read view elements
            ho.name = (TextView) convertView.findViewById(R.id.catadapter);
            convertView.setTag(ho);
        }
        ho = (Holder) convertView.getTag();
        //Set value in list element
        ho.name.setText(resultArray.get(position).getClientName());
        return convertView;
    }
    class Holder {
        TextView name;
    }
}

