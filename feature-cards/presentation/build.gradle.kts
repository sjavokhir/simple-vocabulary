apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreData))
    "implementation"(project(Modules.coreDesignSystem))
    "implementation"(project(Modules.coreModel))
    "implementation"(project(Modules.coreNavigation))
    "implementation"(project(Modules.coreUi))

    "implementation"(project(Modules.setsDomain))
    "implementation"(project(Modules.cardsDomain))
}