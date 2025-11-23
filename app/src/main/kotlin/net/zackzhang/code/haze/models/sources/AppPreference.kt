package net.zackzhang.code.haze.models.sources

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.zackzhang.code.haze.utils.appName
import net.zackzhang.code.util.AppContext

// preferencesDataStore 默认的 scope 参数就带有 Dispatchers.IO
private val Context.dataStore by preferencesDataStore(appName)

private val AppPreference = AppContext.dataStore

object AppPreferenceManager {

    suspend fun get(keyList: List<Preferences.Key<out Any>?>) =
        AppPreference.data.map {
            keyList.map { k ->
                if (k != null) it[k] else null
            }
        }.first()

    // TODO 取值
    suspend fun getBoolean(key: String, defValue: Boolean) =
        AppPreference.data.map {
            it[booleanPreferencesKey(key)] ?: defValue
        }.first()

    suspend fun putBoolean(key: String, value: Boolean) {
        AppPreference.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }
}