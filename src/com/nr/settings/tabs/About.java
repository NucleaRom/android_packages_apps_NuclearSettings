/*
 * Copyright (C) 2018 NucleaRom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nr.settings.tabs;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.IWindowManager;
import android.view.Display;
import android.view.Window;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

    
public class About extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
            
    public static final String TAG = "About";
    
    private static final String NR_ROM_SHARE = "share";
    private static final String KEY_MOD_VERSION = "mod_version";
    
    Preference mSourceUrl;
    Preference mDonateUrl;
    Preference mSourcebaseUrl;
    Preference mNuclearWebUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.nuclear_about);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getContentResolver();

        mSourceUrl = findPreference("nr_source");
        mDonateUrl = findPreference("nr_donate");
        mSourcebaseUrl = findPreference("nr_sourcebase");
        mNuclearWebUrl = findPreference("nuclear_web");

        setValueSummary(KEY_MOD_VERSION, "ro.nr.display.version");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mSourceUrl) {
            launchUrl("https://github.com/NucleaRom");
        } else if (preference == mDonateUrl) {
            launchUrl("http://paypal.me/TeamNuclear");
        } else if (preference == mNuclearWebUrl) {
            launchUrl("http://nuclearom.com/");
        } else if (preference == mSourcebaseUrl) {
            launchUrl("https://github.com/LineageOS");
        }  else if (preference.getKey().equals(NR_ROM_SHARE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            //intent.putExtra(Intent.EXTRA_TEXT, String.format(
            //     getActivity().getString(R.string.share_message)));
            //startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share_chooser_title)));
        }  else {
            // If not handled, let preferences handle it.
            return super.onPreferenceTreeClick(preference);
    }
         return true; 
    }
    private void launchUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent donate = new Intent(Intent.ACTION_VIEW, uriUrl);
        getActivity().startActivity(donate);
    }


    private void setValueSummary(String preference, String property) {
        try {
            findPreference(preference).setSummary(
                    SystemProperties.get(property,
                            getResources().getString(R.string.device_info_default)));
        } catch (RuntimeException e) {
            // No recovery
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.NR_SETTINGS;
    }
}