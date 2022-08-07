package uz.javokhirdev.svocabulary.feature.settings.presentation

sealed class SettingsEvent {
    object ImportClick : SettingsEvent()
    object ExportClick : SettingsEvent()
    object ContactClick : SettingsEvent()
    object ResetClick : SettingsEvent()
    object ShareClick : SettingsEvent()

    data class OnResetDialog(val isOpen: Boolean) : SettingsEvent()
}