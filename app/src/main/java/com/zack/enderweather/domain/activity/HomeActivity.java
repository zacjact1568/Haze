package com.zack.enderweather.domain.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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

import com.zack.enderweather.R;
import com.zack.enderweather.domain.fragment.MyCitiesFragment;
import com.zack.enderweather.interactor.adapter.WeatherPagerAdapter;
import com.zack.enderweather.location.LocationHelper;
import com.zack.enderweather.interactor.presenter.HomePresenter;
import com.zack.enderweather.util.LogUtil;
import com.zack.enderweather.util.Util;
import com.zack.enderweather.domain.view.HomeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomeView,
        NavigationView.OnNavigationItemSelectedListener, LocationHelper.PermissionDelegate {

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

        homePresenter.setPermissionDelegate(this);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_PERMISSIONS:
                if (grantResults.length > 0) {
                    int grantedPermissionCount = 0;
                    for (int result : grantResults) {
                        if (result == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissionCount++;
                        }
                    }
                    if (grantedPermissionCount == permissions.length) {
                        //说明全部权限都已授予
                        homePresenter.notifyPermissionsGranted();
                    } else {
                        //存在权限未授予
                        homePresenter.notifyPermissionsDenied();
                    }
                } else {
                    LogUtil.i(LOG_TAG, "授权被打断");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
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
    public void showPreviouslyRequestPermissionsDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog_pre_rps)
                .setMessage(R.string.msg_dialog_pre_rps)
                .setPositiveButton(R.string.btn_pos_pre_rps, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homePresenter.notifyPermissionsPreviouslyGranted();
                    }
                })
                .setNegativeButton(R.string.btn_neg_pre_rps, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homePresenter.notifyPermissionsDenied();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissions() {
        if (Util.isPermissionsGranted()) {
            //开启app前已给了所有权限（打开app之前手动开启的）
            homePresenter.notifyPermissionsGranted();
        } else {
            //开启app前没给权限（默认）
            //NOTE：此处不需要shouldShowRequestPermissionRationale，因为引导窗口只会弹出一次
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            requestPermissions(permissions, REQ_CODE_PERMISSIONS);
        }
    }

    @Override
    public void showAddCityRequestDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog_ac_req)
                .setMessage(R.string.msg_dialog_ac_req)
                .setPositiveButton(R.string.btn_pos_ac_req, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAddCityActivity();
                    }
                })
                .setNegativeButton(R.string.btn_neg_ac_req, null)
                .setCancelable(false)
                .show();
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

    @OnClick({R.id.fab, R.id.btn_add_city})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startAddCityActivity();
                break;
            case R.id.btn_add_city:
                startAddCityActivity();
                break;
            default:
                break;
        }
    }

    private void startAddCityActivity() {
        Intent intent = new Intent(this, AddCityActivity.class);
        startActivityForResult(intent, REQ_CODE_ADD_CITY);
    }
}
