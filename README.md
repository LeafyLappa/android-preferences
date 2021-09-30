This repo is inspired by and based on [this gist](https://gist.github.com/davidwhitman/b83e1744e8435a2c8cba262c1179f1a8).

# Usage

Declare preferences using the available delegates:
- stringPreference
- intPreference
- floatPreference
- booleanPreference
- longPreference
- stringSetPreference

Depending on whether you want to store the preference key you can do it in two ways.

## Seamless preference access

Use delegates directly:

```kotlin
class SyncPreferences(context: Context) : Preferences(context) {
    val enableSync by booleanPreference(defaultValue = true)
    val syncOnWifiOnly by booleanPreference(defaultValue = true)
    val showSyncNotification by booleanPreference(defaultValue = true)
}
```

Preferences are accessible as instance properties:

```kotlin
syncPreferences.enableSync = false
```

## With preference key

Instantiate Preference data class passing a delegate:

```kotlin
class AppearancePreferences(context: Context) : Preferences(context) {
    val fullscreen = Preference(booleanPreference(context.getString(R.string.pref_key_fullscreen)))
    val enableDarkTheme = Preference(booleanPreference(context.getString(R.string.pref_key_enable_dark_theme)))
    val fontSize = Preference(intPreference(context.getString(R.string.pref_key_font_size), 14))
}
```

Usage:

```kotlin
with (appearancePreferences.fontSize) {
    findPreference<Preference?>(key!!)?.apply {
        summary = data.toString()
    }
}
```
