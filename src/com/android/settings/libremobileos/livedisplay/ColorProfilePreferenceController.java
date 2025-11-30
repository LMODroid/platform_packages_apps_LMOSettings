/*
 * SPDX-FileCopyrightText: 2015 The CyanogenMod Project
 * SPDX-FileCopyrightText: 2017-2025 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.settings.libremobileos.livedisplay;

import static com.libremobileos.hardware.LiveDisplayManager.FEATURE_DISPLAY_MODES;

import android.content.Context;

import com.android.settings.core.BasePreferenceController;
import com.android.settings.libremobileos.utils.ResourceUtils;

import com.libremobileos.hardware.DisplayMode;
import com.libremobileos.hardware.LineageHardwareManager;
import com.libremobileos.hardware.LiveDisplayConfig;
import com.libremobileos.hardware.LiveDisplayManager;

public class ColorProfilePreferenceController extends BasePreferenceController {

    public ColorProfilePreferenceController(Context context, String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        if (!mContext.getResources().getBoolean(
                com.android.internal.R.bool.config_enableLiveDisplay)) {
            return UNSUPPORTED_ON_DEVICE;
        }
        final LiveDisplayConfig config = LiveDisplayManager.getInstance(mContext).getConfig();
        return config.hasFeature(FEATURE_DISPLAY_MODES) ? AVAILABLE : UNSUPPORTED_ON_DEVICE;
    }

    @Override
    public CharSequence getSummary() {
        final LiveDisplayConfig config = LiveDisplayManager.getInstance(mContext).getConfig();
        if (config.hasFeature(FEATURE_DISPLAY_MODES)) {
            final LineageHardwareManager manager = LineageHardwareManager.getInstance(mContext);
            final DisplayMode[] modes = manager.getDisplayModes();
            if (modes != null && modes.length > 0) {
                final DisplayMode currentMode = manager.getCurrentDisplayMode() != null
                        ? manager.getCurrentDisplayMode() : manager.getDefaultDisplayMode();
                for (int i = 0; i < modes.length; i++) {
                    if (currentMode == null || modes[i].id != currentMode.id)
                        continue;
                    return ResourceUtils.getLocalizedString(mContext.getResources(),
                            modes[i].name, ColorProfileFragment.COLOR_PROFILE_TITLE);
                }
            }
        }
        return null;
    }
}
