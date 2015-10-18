package com.yuan.hexgame.ui.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.yuan.hexgame.BuildConfig;
import com.yuan.hexgame.R;
import com.yuan.hexgame.game.GameSettings;

/**
 * Created by Yuan Sun on 2015/10/5.
 */
public class SettingsFragment extends PreferenceFragment {

    private ListPreference mBoardSize;
    private ListPreference mGameMode;
    private ListPreference mFirstPlayerMode;
    private Preference mVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mBoardSize = (ListPreference) findPreference(getString(R.string.settings_key_board_size));
        int boardNMax = GameSettings.getInstance().getBoardNMax();
        int entryNum = boardNMax - GameSettings.CHESS_BOARD_N_MIN + 1;
        String[] entries = new String[entryNum];
        String[] entryValues = new String[entryNum];
        for (int i = 0; i < entryNum; i++) {
            int size = GameSettings.CHESS_BOARD_N_MIN + entryNum - 1 - i;
            entries[i] = size + " X " + size;
            entryValues[i] = String.valueOf(size);
        }
        mBoardSize.setEntries(entries);
        mBoardSize.setEntryValues(entryValues);
        mBoardSize.setSummary(mBoardSize.getEntry());
        mBoardSize.setOnPreferenceChangeListener(mOnPreferenceChangeListener);

        mGameMode = (ListPreference) findPreference(getString(R.string.settings_key_game_mode));
        mGameMode.setSummary(mGameMode.getEntry());
        mGameMode.setOnPreferenceChangeListener(mOnPreferenceChangeListener);

        mFirstPlayerMode = (ListPreference) findPreference(getString(R.string.settings_key_first_player));
        mFirstPlayerMode.setSummary(mFirstPlayerMode.getEntry());
        mFirstPlayerMode.setOnPreferenceChangeListener(mOnPreferenceChangeListener);

        mVersion = findPreference(getString(R.string.settings_key_version));
        mVersion.setSummary(BuildConfig.VERSION_NAME);
    }

    Preference.OnPreferenceChangeListener mOnPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equals(getString(R.string.settings_key_board_size))) {
                mBoardSize.setValue((String) newValue);
                mBoardSize.setSummary(mBoardSize.getEntry());
            } else if (preference.getKey().equals(getString(R.string.settings_key_game_mode))) {
                mGameMode.setValue((String) newValue);
                mGameMode.setSummary(mGameMode.getEntry());
            } else if (preference.getKey().equals(getString(R.string.settings_key_first_player))) {
                mFirstPlayerMode.setValue((String) newValue);
                mFirstPlayerMode.setSummary(mFirstPlayerMode.getEntry());
            }
            return true;
        }
    };
}
