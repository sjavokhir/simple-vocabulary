apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreDesignSystem))
    "implementation"(project(Modules.coreNavigation))
    "implementation"(project(Modules.coreUi))
}