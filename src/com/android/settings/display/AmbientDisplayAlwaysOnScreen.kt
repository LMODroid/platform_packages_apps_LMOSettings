/*
 * Copyright (C) 2024 The Android Open Source Project
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

package com.android.settings.display

import android.app.settings.SettingsEnums.ACTION_AMBIENT_DISPLAY_ALWAYS_ON
import android.content.Context
import android.hardware.display.AmbientDisplayConfiguration
import android.os.SystemProperties
import android.os.UserHandle
import android.os.UserManager
import android.provider.Settings.Secure.DOZE_ALWAYS_ON
import com.android.settings.R
import com.android.settings.flags.Flags
import com.android.settings.contract.KEY_AMBIENT_DISPLAY_ALWAYS_ON
import com.android.settings.display.AmbientDisplayAlwaysOnPreferenceController.isAodSuppressedByBedtime
import com.android.settings.metrics.PreferenceActionMetricsProvider
import com.android.settings.restriction.PreferenceRestrictionMixin
import com.android.settingslib.metadata.PreferenceAvailabilityProvider
import com.android.settingslib.metadata.PreferenceSummaryProvider
import com.android.settingslib.metadata.ProvidePreferenceScreen
import com.android.settingslib.metadata.preferenceHierarchy
import com.android.settingslib.preference.PreferenceScreenCreator

@ProvidePreferenceScreen(AmbientDisplayAlwaysOnScreen.KEY)
class AmbientDisplayAlwaysOnScreen :
    PreferenceScreenCreator,
    PreferenceActionMetricsProvider,
    PreferenceAvailabilityProvider,
    PreferenceSummaryProvider,
    PreferenceRestrictionMixin {

    override val key: String
        get() = KEY

    override val title: Int
        get() = R.string.doze_always_on_title

    override val keywords: Int
        get() = R.string.keywords_always_show_time_info

    override val preferenceActionMetrics: Int
        get() = ACTION_AMBIENT_DISPLAY_ALWAYS_ON

    override val restrictionKeys: Array<String>
        get() = arrayOf(UserManager.DISALLOW_AMBIENT_DISPLAY)

    override fun isEnabled(context: Context) = super<PreferenceRestrictionMixin>.isEnabled(context)

    override fun isAvailable(context: Context) =
        !SystemProperties.getBoolean(PROP_AWARE_AVAILABLE, false) &&
            AmbientDisplayConfiguration(context).alwaysOnAvailableForUser(UserHandle.myUserId())

    override fun getSummary(context: Context): CharSequence? =
        context.getText(
            when {
                isAodSuppressedByBedtime(context) -> R.string.aware_summary_when_bedtime_on
                else -> R.string.doze_always_on_summary
            }
        )

    override fun isFlagEnabled(context: Context) = Flags.catalystLockscreenFromDisplaySettings()

    override fun hasCompleteHierarchy() = false

    override fun fragmentClass() = AmbientDisplayAlwaysOnSettings::class.java

    override fun getPreferenceHierarchy(context: Context) =
        preferenceHierarchy(context, this) {
            // Nothing here
        }

    companion object {
        const val KEY = "ambient_display_always_on"
        private const val PROP_AWARE_AVAILABLE = "ro.vendor.aware_available"
    }
}
