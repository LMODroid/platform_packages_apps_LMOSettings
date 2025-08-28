/*
 * Copyright (C) 2025 The LibreMobileOS Foundation
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
package com.android.settings.display;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.preference.SwitchPreferenceCompat;

import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.R;

import com.android.settingslib.widget.MainSwitchPreference;

import com.libremobileos.providers.LMOSettings;

public class AmbientDisplayAlwaysOnSettings extends DashboardFragment {

    private static final String TAG = "AmbientDisplayAlwaysOnSettings";

    private static final String KEY_DOZE_ALWAYS_ON = "doze_always_on";
    private static final String KEY_DOZE_ALWAYS_ON_TIMEOUT = "doze_always_on_timeout";

    private SettingsObserver mSettingsObserver;

    private MainSwitchPreference mMainSwitchPref;
    private SwitchPreferenceCompat mTimeoutPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsObserver = new SettingsObserver(getContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainSwitchPref = findPreference(KEY_DOZE_ALWAYS_ON);
        mTimeoutPref = findPreference(KEY_DOZE_ALWAYS_ON_TIMEOUT);
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.ambient_display_always_on_settings;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSettingsObserver.register(() -> {
            updateUI();
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mSettingsObserver.unregister();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.LMODROID;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    private void updateUI() {
        boolean dozeEnabled = Settings.Secure.getInt(getContext().getContentResolver(),
                Settings.Secure.DOZE_ALWAYS_ON, 0) != 0;
        boolean timeoutEnabled = Settings.Secure.getInt(getContext().getContentResolver(),
                LMOSettings.Secure.DOZE_ALWAYS_ON_TIMEOUT, 0) != 0;
        getContext().getMainExecutor().execute(() -> {
            if (mMainSwitchPref != null && mMainSwitchPref.isChecked() != dozeEnabled) {
                mMainSwitchPref.setChecked(dozeEnabled);
            }
            if (mTimeoutPref != null && mTimeoutPref.isChecked() != timeoutEnabled) {
                mTimeoutPref.setChecked(timeoutEnabled);
            }
        });
    }

    public class SettingsObserver extends ContentObserver {
        private Context mContext;
        private ContentResolver mContentResolver;
        private Runnable mCallback;

        public SettingsObserver(Context context) {
            super(null);
            mContext = context;
            mContentResolver = context.getContentResolver();
        }

        public void register(Runnable callback) {
            mCallback = callback;
            mCallback.run();
            Uri dozeAlwaysOnUri =
                    Settings.Secure.getUriFor(
                            Settings.Secure.DOZE_ALWAYS_ON);
            Uri dozeAlwaysOnTimeoutUri =
                    Settings.Secure.getUriFor(
                            LMOSettings.Secure.DOZE_ALWAYS_ON_TIMEOUT);
            mContentResolver.registerContentObserver(
                    dozeAlwaysOnUri, false, this);
            mContentResolver.registerContentObserver(
                    dozeAlwaysOnTimeoutUri, false, this);
        }

        public void unregister() {
            mContentResolver.unregisterContentObserver(this);
            mCallback = null;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (mCallback != null) {
                mCallback.run();
            }
        }
    }

}
