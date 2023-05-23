Seamless and Kotlin-idiomatic SharedPreferences access - now works with enums!

This repo is inspired by and based on [this gist](https://gist.github.com/davidwhitman/b83e1744e8435a2c8cba262c1179f1a8).

# Usage

Declare preferences using the available delegates:
- stringNullablePreference
- stringPreference
- intPreference
- floatPreference
- booleanPreference
- longPreference
- stringSetPreference
- enumPreference

## Seamless preference access

Use delegates directly:

```kotlin
class SyncPreferences(context: Context) : Preferences(context) {
    val enableSync by booleanPreference(defaultValue = true)
    val syncOnWifiOnly by booleanPreference(defaultValue = true)
    val showSyncNotification by booleanPreference(defaultValue = true)
}

class ThemePreferences(context: Context) : Preferences(context) {
    val appIcon by enumPreference(defaultValue = AppIcons.DEFAULT)
}
```

Preferences are accessible as instance properties:

```kotlin
syncPreferences.enableSync = false
themePreferences.appIcon = AppIcons.VINTAGE
```
