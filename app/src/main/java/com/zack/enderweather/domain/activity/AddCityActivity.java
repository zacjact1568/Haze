package com.zack.enderweather.domain.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zack.enderweather.R;
import com.zack.enderweather.interactor.adapter.CitySearchResultAdapter;
import com.zack.enderweather.interactor.presenter.AddCityPresenter;
import com.zack.enderweather.domain.view.AddCityView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCityActivity extends BaseActivity implements AddCityView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.text_empty)
    TextView emptyText;

    private AddCityPresenter addCityPresenter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        ButterKnife.bind(this);

        addCityPresenter = new AddCityPresenter(this);

        addCityPresenter.setInitialView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addCityPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_city, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                addCityPresenter.notifySearchTextChanged(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            //说明搜索框完全展开
            searchView.setIconified(true);
        } else {
            //说明搜索框未展开
            super.onBackPressed();
        }
    }

    @Override
    public void showInitialView(CitySearchResultAdapter citySearchResultAdapter) {
        setSupportActionBar(toolbar);
        setupActionBar();

        listView.setAdapter(citySearchResultAdapter);
        listView.setEmptyView(emptyText);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addCityPresenter.notifyCityListItemClicked(position);
            }
        });

        //初始隐藏没搜索到城市的提示（必须在setEmptyView后设置才有效）
        emptyText.setVisibility(View.GONE);
    }

    @Override
    public void onCityAdded() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onDetectCityExists() {
        Toast.makeText(this, R.string.toast_city_exists, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchTextEmptied() {
        emptyText.setVisibility(View.GONE);
    }
}
