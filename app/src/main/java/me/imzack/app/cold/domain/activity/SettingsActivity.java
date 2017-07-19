package me.imzack.app.cold.domain.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.imzack.app.cold.R;
import me.imzack.app.cold.model.preference.PreferenceDispatcher;
import me.imzack.app.cold.interactor.presenter.SettingsPresenter;
import me.imzack.app.cold.util.LogUtil;
import me.imzack.app.cold.domain.view.SettingsView;

import java.util.Locale;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setupActionBar();

        getFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).commit();
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

    public static class SettingsFragment extends PreferenceFragment
            implements SettingsView, SharedPreferences.OnSharedPreferenceChangeListener {

        private SettingsPresenter settingsPresenter;
        private SwitchPreference locationServicePref;
        private ListPreference languagePref, nightModePref;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            settingsPresenter = new SettingsPresenter(this);

            settingsPresenter.setInitialView();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void showInitialView() {
            locationServicePref = (SwitchPreference) findPreference(PreferenceDispatcher.KEY_PREF_LOCATION_SERVICE);
            languagePref = (ListPreference) findPreference(PreferenceDispatcher.KEY_PREF_LANGUAGE);
            nightModePref = (ListPreference) findPreference(PreferenceDispatcher.KEY_PREF_NIGHT_MODE);

            initPreferenceSummary();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case PreferenceDispatcher.KEY_PREF_LOCATION_SERVICE:
                    if (sharedPreferences.getBoolean(PreferenceDispatcher.KEY_PREF_LOCATION_SERVICE, false)) {
                        //已打开定位服务
                        LogUtil.d("已打开定位服务");
                    } else {
                        //已关闭定位服务
                        LogUtil.d("已关闭定位服务");
                    }
                    break;
                case PreferenceDispatcher.KEY_PREF_LANGUAGE:
                    String languageValue = sharedPreferences.getString(PreferenceDispatcher.KEY_PREF_LANGUAGE, "");
                    languagePref.setSummary(languagePref.getEntries()[languagePref.findIndexOfValue(languageValue)]);

                    Configuration config = getResources().getConfiguration();
                    switch (languageValue) {
                        case "en":
                            config.locale = Locale.ENGLISH;
                            break;
                        case "zh":
                            config.locale = Locale.CHINESE;
                            break;
                        default:
                            config.locale = Locale.getDefault();
                            break;
                    }
                    getResources().updateConfiguration(config, null);
                    recreate();
                    break;
                case PreferenceDispatcher.KEY_PREF_NIGHT_MODE:
                    String nightModeValue = sharedPreferences.getString(PreferenceDispatcher.KEY_PREF_NIGHT_MODE, "");
                    nightModePref.setSummary(nightModePref.getEntries()[nightModePref.findIndexOfValue(nightModeValue)]);

                    int mode = AppCompatDelegate.MODE_NIGHT_NO;
                    switch (nightModeValue) {
                        case "off":
                            mode = AppCompatDelegate.MODE_NIGHT_NO;
                            break;
                        case "on":
                            mode = AppCompatDelegate.MODE_NIGHT_YES;
                            break;
                        case "auto":
                            mode = AppCompatDelegate.MODE_NIGHT_AUTO;
                            break;
                        case "def":
                            mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                            break;
                        default:
                            break;
                    }
                    AppCompatDelegate.setDefaultNightMode(mode);
                    recreate();
                    break;
                default:
                    break;
            }
        }

        private void initPreferenceSummary() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            //Location Service
            //No need to set summary
            //Language
            languagePref.setSummary(languagePref.getEntries()[languagePref.findIndexOfValue(sharedPreferences.getString(PreferenceDispatcher.KEY_PREF_LANGUAGE, ""))]);
            //Night Mode
            nightModePref.setSummary(nightModePref.getEntries()[nightModePref.findIndexOfValue(sharedPreferences.getString(PreferenceDispatcher.KEY_PREF_NIGHT_MODE, ""))]);
        }

        /** 返回并重新创建HomeActivity */
        private void recreate() {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
