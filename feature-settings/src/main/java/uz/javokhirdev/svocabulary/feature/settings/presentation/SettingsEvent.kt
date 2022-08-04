package uz.javokhirdev.svocabulary.feature.settings.presentation

sealed class SettingsEvent {
    object OnImportClick : SettingsEvent()
    object OnExportClick : SettingsEvent()
    object OnContactClick : SettingsEvent()
    object OnResetClick : SettingsEvent()
    object OnShareClick : SettingsEvent()
}