package ru.sysor.pricechecker.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

/**
 * Created by LeafyLappa
 * greentail@pm.me
 */

@Suppress("unused")
abstract class Preferences(context: Context, name: String? = null) {
    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(name ?: javaClass.simpleName, Context.MODE_PRIVATE)
    }

    private val listeners = mutableListOf<OnChangeListener>()

    abstract class PreferenceDelegate<T>(val key: String?) {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
        abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    data class Preference<T>(private val delegate: GenericPreferenceDelegate<T>) {
        val key = delegate.key
        var data by delegate
    }

    interface OnChangeListener {
        fun onSharedPreferenceChanged(property: KProperty<*>)
    }

    fun addListener(onChangeListener: OnChangeListener) {
        listeners.add(onChangeListener)
    }

    fun removeListener(onChangeListener: OnChangeListener) {
        listeners.remove(onChangeListener)
    }

    fun clearListeners() = listeners.clear()

    inner class GenericPreferenceDelegate<T>(
        key: String? = null,
        private val defaultValue: T,
        private val getFunction: (key: String) -> T,
        private val setFunction: (key: String, value: T) -> Unit,
    ) : PreferenceDelegate<T>(key) {

        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            getFunction(key ?: property.name)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
            setFunction(key ?: property.name, value).also { onPreferenceChanged(property) }
    }

    fun stringPreference(key: String? = null, defaultValue: String? = null) = with(preferences) {
        GenericPreferenceDelegate(key, defaultValue,
            { key -> getString(key, defaultValue) },
            { key, value -> edit().putString(key, value).apply() }
        )
    }

    fun intPreference(key: String? = null, defaultValue: Int = 0) = with(preferences) {
        GenericPreferenceDelegate(key, defaultValue,
            { key -> getInt(key, defaultValue) },
            { key, value -> edit().putInt(key, value).apply() }
        )
    }

    fun floatPreference(key: String? = null, defaultValue: Float = 0f) = with(preferences) {
        GenericPreferenceDelegate(key, defaultValue,
            { key -> getFloat(key, defaultValue) },
            { key, value -> edit().putFloat(key, value).apply() }
        )
    }

    fun booleanPreference(key: String? = null, defaultValue: Boolean = false) = with(preferences) {
        GenericPreferenceDelegate(key, defaultValue,
            { key -> getBoolean(key, defaultValue) },
            { key, value -> edit().putBoolean(key, value).apply() }
        )
    }

    fun longPreference(key: String? = null, defaultValue: Long = 0L) = with(preferences) {
        GenericPreferenceDelegate(key, defaultValue,
            { key -> getLong(key, defaultValue) },
            { key, value -> edit().putLong(key, value).apply() }
        )
    }

    fun stringSetPreference(key: String? = null, defaultValue: Set<String> = HashSet()) = with(preferences) {
        GenericPreferenceDelegate(key, defaultValue,
            { key -> getStringSet(key, defaultValue) },
            { key, value -> edit().putStringSet(key, value).apply() }
        )
    }

    private fun onPreferenceChanged(property: KProperty<*>) {
        listeners.forEach { it.onSharedPreferenceChanged(property) }
    }
}
