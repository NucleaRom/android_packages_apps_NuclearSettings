/*Copyright (C) 2018 NucleaRom
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
*/
package com.nr.settings.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import android.provider.Settings;

import com.nr.settings.preferences.CustomSeekBarPreference;

public class QsPanel extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "QsPanel";

private static final String QS_PANEL_ALPHA = "qs_panel_alpha";
private static final String PREF_SYSUI_QQS_COUNT = "sysui_qqs_count_key";

private CustomSeekBarPreference mQsPanelAlpha;
private CustomSeekBarPreference mSysuiQqsCount;

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.APPLICATION;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.nr_qs_panel);

        ContentResolver resolver = getActivity().getContentResolver();

        mQsPanelAlpha = (CustomSeekBarPreference) findPreference(QS_PANEL_ALPHA);
        int qsPanelAlpha = Settings.System.getIntForUser(resolver,
                Settings.System.QS_PANEL_BG_ALPHA, 255, UserHandle.USER_CURRENT);
        mQsPanelAlpha.setValue(qsPanelAlpha);
        mQsPanelAlpha.setOnPreferenceChangeListener(this);

        mSysuiQqsCount = (CustomSeekBarPreference) findPreference(PREF_SYSUI_QQS_COUNT);
        int SysuiQqsCount = Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.QQS_COUNT, 6);
        mSysuiQqsCount.setValue(SysuiQqsCount / 1);
        mSysuiQqsCount.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
    
    ContentResolver resolver = getActivity().getContentResolver();
    
    if (preference == mQsPanelAlpha) {
        int bgAlpha = (Integer) newValue;
        Settings.System.putIntForUser(getContentResolver(),
                Settings.System.QS_PANEL_BG_ALPHA, bgAlpha,
                UserHandle.USER_CURRENT);
        return true;
    } else if (preference == mSysuiQqsCount) {
            int SysuiQqsCount = (Integer) newValue;
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.QQS_COUNT, SysuiQqsCount * 1);
            return true;
        }
        return false;
    }
}
