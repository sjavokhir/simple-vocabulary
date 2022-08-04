apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreData))
    "implementation"(project(Modules.coreDatabase))
    "implementation"(project(Modules.coreModel))
    "implementation"(project(Modules.cardsDomain))

    "kapt"(Room.roomCompiler)
    "implementation"(Room.roomKtx)
    "implementation"(Room.roomRuntime)
}
