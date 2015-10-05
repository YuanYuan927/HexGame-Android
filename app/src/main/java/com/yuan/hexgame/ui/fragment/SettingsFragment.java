package com.yuan.hexgame.ui.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.yuan.hexgame.BuildConfig;
import com.yuan.hexgame.R;

/**
 * Created by Yuan Sun on 2015/10/5.
 */
public class SettingsFragment extends PreferenceFragment {

    private ListPreference mGameMode;
    private Preference mVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mGameMode = (ListPreference) findPreference(getString(R.string.settings_key_game_mode));
        mGameMode.setSummary(mGameMode.getValue());
        mGameMode.setOnPreferenceChangeListener(mOnPreferenceChangeListener);

        mVersion = findPreference(getString(R.string.settings_key_version));
        mVersion.setSummary(BuildConfig.VERSION_NAME);
    }

    Preference.OnPreferenceChangeListener mOnPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equals(getString(R.string.settings_key_game_mode))) {
                mGameMode.setSummary((String) newValue);
            }
            return true;
        }
    };
}
