apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreData))
    "implementation"(project(Modules.coreModel))

    "implementation"(Coroutines.coroutines)
}