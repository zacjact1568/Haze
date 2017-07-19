package me.imzack.app.cold.interactor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import me.imzack.app.cold.R;
import me.imzack.app.cold.model.bean.City;

import java.util.List;

public class CitySearchResultAdapter extends BaseAdapter {

    private List<City> cityList;

    public CitySearchResultAdapter(List<City> cityList) {
        this.cityList = cityList;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public City getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_search_result, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cityText = (TextView) view.findViewById(R.id.text_city);
            viewHolder.prefAndProvText = (TextView) view.findViewById(R.id.text_pref_and_prov);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.cityText.setText(city.getCityName());
        viewHolder.prefAndProvText.setText(String.format("%s, %s", city.getPrefName(), city.getProvName()));
        return view;
    }

    class ViewHolder {
        TextView cityText, prefAndProvText;
    }
}
