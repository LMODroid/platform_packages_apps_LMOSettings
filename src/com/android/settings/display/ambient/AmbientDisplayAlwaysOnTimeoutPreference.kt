/*
 * SPDX-FileCopyrightText: The LibreMobileOS Foundation
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.settings.display.ambient

import android.content.Context
import com.android.settings.R
import com.android.settingslib.datastore.KeyValueStore
import com.android.settingslib.datastore.KeyValueStoreDelegate
import com.android.settingslib.datastore.SettingsSecureStore
import com.android.settingslib.metadata.SwitchPreference
import com.libremobileos.providers.LMOSettings

/** Turns off the always-on display after a period of inactivity. */
class AmbientDisplayAlwaysOnTimeoutPreference :
    SwitchPreference(
        KEY,
        R.string.doze_always_on_timeout_switch_title,
        R.string.doze_always_on_timeout_switch_summary,
    ) {

    override fun isEnabled(context: Context) =
        AmbientDisplayStorage(context)
            .getBoolean(AmbientDisplayMainSwitchPreference.KEY) == true

    override fun dependencies(context: Context) =
        arrayOf(AmbientDisplayMainSwitchPreference.KEY)

    override fun storage(context: Context): KeyValueStore =
        TimeoutStorage(context, SettingsSecureStore.get(context))

    @Suppress("UNCHECKED_CAST")
    private class TimeoutStorage(
        private val context: Context,
        private val settingsStore: KeyValueStore,
    ) : KeyValueStoreDelegate {

        override val keyValueStoreDelegate
            get() = settingsStore

        override fun <T : Any> getDefaultValue(key: String, valueType: Class<T>) =
            context.resources.getBoolean(
                com.android.internal.R.bool.config_dozeAlwaysOnDisplayTimeoutByDefault
            ) as T
    }

    companion object {
        const val KEY = LMOSettings.Secure.DOZE_ALWAYS_ON_TIMEOUT
    }
}
