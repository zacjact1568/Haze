package me.imzack.app.cold.domain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import me.imzack.app.cold.R;
import me.imzack.app.cold.domain.fragment.MyCitiesFragment;
import me.imzack.app.cold.interactor.adapter.WeatherPagerAdapter;
import me.imzack.app.cold.interactor.presenter.HomePresenter;
import me.imzack.app.cold.domain.view.HomeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomeView,
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.pager_weather)
    ViewPager weatherPager;

    private static final String LOG_TAG = "HomeActivity";

    private HomePresenter homePresenter;

    private static final String TAG_MY_CITIES = "my_cities";
    private static final int REQ_CODE_ADD_CITY = 0;
    private static final int REQ_CODE_GUIDE = 1;
    private static final int REQ_CODE_PERMISSIONS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        homePresenter = new HomePresenter(this);

        homePresenter.setInitialView(getSupportFragmentManager());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        homePresenter.notifyStartingUpCompleted();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homePresenter.detachView();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case REQ_CODE_ADD_CITY:
                homePresenter.notifyCityAdded();
                break;
            case REQ_CODE_GUIDE:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.action_about:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_weather:
                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                    getSupportFragmentManager().popBackStack();
                }
                break;
            case R.id.nav_my_cities:
                if (getSupportFragmentManager().findFragmentByTag(TAG_MY_CITIES) == null) {
                    fab.setVisibility(View.VISIBLE);
                    toolbar.setTitle(R.string.title_fragment_my_cities);
                    MyCitiesFragment myCitiesFragment = new MyCitiesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, myCitiesFragment, TAG_MY_CITIES).addToBackStack(null).commit();
                    getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                        @Override
                        public void onBackStackChanged() {
                            if (getSupportFragmentManager().findFragmentByTag(TAG_MY_CITIES) == null) {
                                fab.setVisibility(View.GONE);
                                toolbar.setTitle(" ");
                                navView.setCheckedItem(R.id.nav_weather);
                                getSupportFragmentManager().removeOnBackStackChangedListener(this);
                            }
                        }
                    });
                }
                break;
            case R.id.nav_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.nav_about:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;
            default:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showInitialView(WeatherPagerAdapter weatherPagerAdapter) {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        weatherPager.setAdapter(weatherPagerAdapter);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetectNetworkNotAvailable() {
        Snackbar snackbar = Snackbar.make(frameLayout, R.string.text_network_not_available, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.action_network_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        snackbar.show();
    }

    @Override
    public void onSwitchPage(int position) {
        weatherPager.setCurrentItem(position);
    }

    @Override
    public void showGuide() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivityForResult(intent, REQ_CODE_GUIDE);
    }

    @Override
    public void onAddCity() {
        Intent intent = new Intent(this, AddCityActivity.class);
        startActivityForResult(intent, REQ_CODE_ADD_CITY);
    }

    @OnClick({R.id.fab, R.id.btn_add_city})
    public void onClick(View view) {
        homePresenter.notifyViewClicked(view.getId());
    }
}
