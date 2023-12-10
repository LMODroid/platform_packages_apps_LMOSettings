/*
 * SPDX-FileCopyrightText: 2017-2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.settings.libremobileos.notificationlight;

import android.content.ContentResolver;
import android.content.Context;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.AttributeSet;

import com.libremobileos.providers.LMOSettings;

public class BatteryBrightnessZenPreference extends BrightnessPreference {
    private static final String TAG = "BatteryBrightnessZenPreference";

    private final ContentResolver mResolver;

    public BatteryBrightnessZenPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mResolver = context.getContentResolver();
    }

    @Override
    protected int getBrightnessSetting() {
        return Settings.System.getIntForUser(mResolver,
                LMOSettings.System.BATTERY_LIGHT_BRIGHTNESS_LEVEL_ZEN,
                LIGHT_BRIGHTNESS_MAXIMUM, UserHandle.USER_CURRENT);
    }

    @Override
    protected void setBrightnessSetting(int brightness) {
        Settings.System.putIntForUser(mResolver,
                LMOSettings.System.BATTERY_LIGHT_BRIGHTNESS_LEVEL_ZEN,
                brightness, UserHandle.USER_CURRENT);
    }
}
