package dev.wazapps.timetodo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dev.wazapps.timetodo.data.models.Priority
import dev.wazapps.timetodo.utils.Constants.PREFS_KEY_SORT
import dev.wazapps.timetodo.utils.Constants.PREFS_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_NAME)

@ViewModelScoped
class TodoDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PrefsKeys {
        val sortKey = stringPreferencesKey(PREFS_KEY_SORT)
    }

    private val dataStore = context.dataStore

    suspend fun persistSortState(priority: Priority){
        dataStore.edit { prefs ->
            prefs[PrefsKeys.sortKey] = priority.name
        }
    }

    val readSortState: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { prefs ->
            val sortState = prefs[PrefsKeys.sortKey] ?: Priority.NONE.name
            sortState
        }
}