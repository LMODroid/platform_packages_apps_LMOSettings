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

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.libremobileos.support.preferences.SecureSettingMainSwitchPreference;

public class AmbientDisplayAlwaysOnSettings extends DashboardFragment {

    private static final String TAG = "AmbientDisplayAlwaysOnSettings";

    private final ContentObserver mAlwaysOnObserver = new ContentObserver(
            new Handler(Looper.getMainLooper())) {
        @Override
        public void onChange(boolean selfChange) {
            updateAlwaysOnSwitch();
        }
    };

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.ambient_display_always_on_settings;
    }

    @Override
    public @Nullable String getPreferenceScreenBindingKey(@NonNull Context context) {
        return AmbientDisplayAlwaysOnScreen.KEY;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.LMODROID;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().getContentResolver().registerContentObserver(
                Settings.Secure.getUriFor(Settings.Secure.DOZE_ALWAYS_ON),
                false, mAlwaysOnObserver);
        updateAlwaysOnSwitch();
    }

    @Override
    public void onStop() {
        getContext().getContentResolver().unregisterContentObserver(mAlwaysOnObserver);
        super.onStop();
    }

    private void updateAlwaysOnSwitch() {
        final SecureSettingMainSwitchPreference preference =
                findPreference(Settings.Secure.DOZE_ALWAYS_ON);
        if (preference != null) {
            preference.setChecked(Settings.Secure.getInt(getContext().getContentResolver(),
                    Settings.Secure.DOZE_ALWAYS_ON, 0) != 0);
        }
    }

}
