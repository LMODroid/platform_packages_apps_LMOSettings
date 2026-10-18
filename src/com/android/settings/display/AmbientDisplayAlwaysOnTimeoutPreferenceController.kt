/*
 * SPDX-FileCopyrightText: The LibreMobileOS Foundation
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.settings.display

import android.content.Context
import android.hardware.display.AmbientDisplayConfiguration
import android.os.UserHandle
import android.provider.Settings
import androidx.preference.Preference
import com.android.settings.R
import com.android.settings.core.TogglePreferenceController
import com.libremobileos.providers.LMOSettings

/**
 * Always-on display timeout toggle for devices whose doze settings are managed externally.
 *
 * Otherwise this option lives in the always-on display page, which is hidden on such devices.
 */
class AmbientDisplayAlwaysOnTimeoutPreferenceController(
    context: Context,
    key: String
) : TogglePreferenceController(context, key) {

    private val config = AmbientDisplayConfiguration(context)

    override fun getAvailabilityStatus(): Int =
        AVAILABLE.takeIf {
            AmbientDisplaySettingsController.isExternallyManaged(mContext) &&
                config.alwaysOnAvailableForUser(UserHandle.myUserId())
        } ?: UNSUPPORTED_ON_DEVICE

    override fun getSliceHighlightMenuRes(): Int = R.string.menu_key_display

    override fun updateState(preference: Preference) {
        super.updateState(preference)
        // Only relevant while always-on display is enabled
        preference.isEnabled = config.alwaysOnEnabled(UserHandle.myUserId())
    }

    override fun isChecked(): Boolean =
        Settings.Secure.getInt(
            mContext.contentResolver,
            LMOSettings.Secure.DOZE_ALWAYS_ON_TIMEOUT,
            if (mContext.resources.getBoolean(
                    com.android.internal.R.bool.config_dozeAlwaysOnDisplayTimeoutByDefault))
                1 else 0
        ) != 0

    override fun setChecked(isChecked: Boolean): Boolean =
        Settings.Secure.putInt(
            mContext.contentResolver,
            LMOSettings.Secure.DOZE_ALWAYS_ON_TIMEOUT,
            if (isChecked) 1 else 0
        )
}
