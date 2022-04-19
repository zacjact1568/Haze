package net.zackzhang.code.haze.settings.model.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.zackzhang.code.haze.base.util.appName
import net.zackzhang.code.haze.base.util.context

object SettingsLocalRepository {

    /**
     * preferencesDataStore 默认的 scope 参数就带有 Dispatchers.IO
     */
    private val Context.dataStore by preferencesDataStore(appName)

    private val dataStore = context.dataStore

    suspend fun getPreferences(keyList: List<Preferences.Key<out Any>?>) =
        dataStore.data.map {
            keyList.map { k ->
                if (k != null) it[k] else null
            }
        }.first()

    // TODO 取值
    suspend fun getBooleanPreference(key: String, defValue: Boolean) =
        dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: defValue
        }.first()

    suspend fun putBooleanPreference(key: String, value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }
}