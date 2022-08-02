plugins {
    id("com.android.application")
    kotlin("android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
}

dependencies {
    implementation(project(Modules.coreData))
    implementation(project(Modules.coreDatabase))
    implementation(project(Modules.coreDesignSystem))
    implementation(project(Modules.coreModel))
    implementation(project(Modules.coreNavigation))
    implementation(project(Modules.coreUi))

    implementation(project(Modules.setsData))
    implementation(project(Modules.setsDomain))
    implementation(project(Modules.setsPresentation))

    implementation(project(Modules.setDetailPresentation))

    implementation(project(Modules.cardsData))
    implementation(project(Modules.cardsDomain))
    implementation(project(Modules.cardsPresentation))

    implementation(project(Modules.cardDetailPresentation))

    implementation(Base.coreKtx)
    implementation(Base.appCompat)

    implementation(Compose.runtime)
    implementation(Compose.compiler)
    implementation(Compose.ui)
    implementation(Compose.uiToolingPreview)
    implementation(Compose.material3)
    implementation(Compose.material3WindowSizeClass)
    implementation(Compose.icons)
    implementation(Compose.navigation)
    implementation(Compose.hiltNavigationCompose)
    implementation(Compose.viewModelCompose)
    implementation(Compose.activityCompose)

    implementation(Google.material)

    implementation(DaggerHilt.hiltAndroid)
    kapt(DaggerHilt.hiltCompiler)
}