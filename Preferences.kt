import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Created by LeafyLappa
 * greentail@pm.me
 */
abstract class Preferences(context: Context, name: String? = null) {
    
    private val preferences: SharedPreferences by lazy {
        if (name == null) {
            PreferenceManager.getDefaultSharedPreferences(context)
        } else {
            context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }
    }

    inner class GenericPreferenceDelegate<T>(
        private val getFunction: () -> T,
        private val setFunction: (value: T) -> Unit
    ) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>) =
            getFunction()

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
            setFunction(value)
    }

    fun stringNullablePreference(key: String, defaultValue: String? = null) = with(preferences) {
        GenericPreferenceDelegate(
            { getString(key, defaultValue) },
            { value -> edit().putString(key, value).apply() }
        )
    }

    fun stringPreference(key: String, defaultValue: String = "") = with(preferences) {
        GenericPreferenceDelegate(
            { getString(key, defaultValue) as String },
            { value -> edit().putString(key, value).apply() }
        )
    }

    fun intPreference(key: String, defaultValue: Int = 0) = with(preferences) {
        GenericPreferenceDelegate(
            { getInt(key, defaultValue) },
            { value -> edit().putInt(key, value).apply() }
        )
    }

    fun floatPreference(key: String, defaultValue: Float = 0f) = with(preferences) {
        GenericPreferenceDelegate(
            { getFloat(key, defaultValue) },
            { value -> edit().putFloat(key, value).apply() }
        )
    }

    fun booleanPreference(key: String, defaultValue: Boolean = false) = with(preferences) {
        GenericPreferenceDelegate(
            { getBoolean(key, defaultValue) },
            { value -> edit().putBoolean(key, value).apply() }
        )
    }

    fun longPreference(key: String, defaultValue: Long = 0L) = with(preferences) {
        GenericPreferenceDelegate(
            { getLong(key, defaultValue) },
            { value -> edit().putLong(key, value).apply() }
        )
    }

    fun stringSetPreference(key: String, defaultValue: Set<String> = HashSet()) = with(preferences) {
        GenericPreferenceDelegate(
            { getStringSet(key, defaultValue) },
            { value -> edit().putStringSet(key, value).apply() }
        )
    }

    fun <T : Enum<T>> enumPreference(key: String, defaultValue: Enum<T>) = with(preferences) {
        GenericPreferenceDelegate(
            { getString(key, defaultValue.name).let { value -> defaultValue::class.java.enumConstants.find { it.name == value } } as T },
            { value -> edit().putString(key, value.name).apply() }
        )
    }
}
