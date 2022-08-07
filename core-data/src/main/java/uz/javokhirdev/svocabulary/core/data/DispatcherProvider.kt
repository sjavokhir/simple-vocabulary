package uz.javokhirdev.svocabulary.core.data

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DispatcherProvider @Inject constructor() {

    fun main() = Dispatchers.Main
    fun io() = Dispatchers.IO
}