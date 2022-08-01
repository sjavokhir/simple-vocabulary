apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(Coroutines.coroutines)
}