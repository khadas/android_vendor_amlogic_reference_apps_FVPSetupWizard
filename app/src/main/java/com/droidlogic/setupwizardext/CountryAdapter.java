package com.droidlogic.setupwizardext;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class CountryAdapter extends BaseAdapter {
    private Context mContext;
    private List<Pair<String, String>> countrys;
    private List<String> mList;

    public CountryAdapter(Context context, List<String> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.country_item, viewGroup, false);
            holder.countryName = view.findViewById(R.id.country_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.countryName.setText(mList.get(i));
        return view;
    }

    class ViewHolder {
        public Button countryName;
    }
}
