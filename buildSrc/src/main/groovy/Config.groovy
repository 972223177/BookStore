class Config {

    static compileSdk = 33
    static minSdk = 21
    static targetSdk = 33

    static composeVersion = "1.2.1"
    static composeKtCompileVersion = "1.3.0"
    static immersionBarVersion = "3.2.2"
    static glideVersion = "4.13.2"
    static okDownloadVersion = "1.0.7"
    static okhttpVersion = "4.10.0"
    static tencentTbsVersion = "44226"
    static coroutineAndroidVersion = "1.6.4"
    static theRouterVersion = "1.1.0"
    static dataStoreVersion = "1.0.0"
    static protobufVersion = "0.8.19"
    static protobufJavaUtilVersion = "3.21.1"
    static navVersion = "2.5.2"
    static hiltVersion = "2.42"
    static roomVersion = "2.4.3"
    static accompanistPagerVersion = "0.19.0"

    static deps = [
            composeUi                : "androidx.compose.ui:ui:$composeVersion",
            composeToolingPre        : "androidx.compose.ui:ui-tooling-preview:$composeVersion",
            composeMaterial          : "androidx.compose.material:material:$composeVersion",
            coreKtx                  : "androidx.core:core-ktx:1.8.0",
            lifecycleRuntimeKtx      : "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1",
            activityCompose          : "androidx.activity:activity-compose:1.5.1",
            appcompat                : "androidx.core:core-ktx:1.8.0",
            material                 : "com.google.android.material:material:1.6.1",
            navCompose               : "androidx.navigation:navigation-compose:$navVersion",
            hilt                     : "com.google.dagger:hilt-android:$hiltVersion",

            accompanistPager         : "com.google.accompanist:accompanist-pager:$accompanistPagerVersion",
            accompanistPagerIndicator: "com.google.accompanist:accompanist-pager-indicators:$accompanistPagerVersion",
            roomRuntime              : "androidx.room:room-runtime:$roomVersion",
            roomKtx                  : "androidx.room:room-ktx:$roomVersion",
            roomPaging               : "androidx.room:room-paging:$roomVersion",

            immersionbar             : "com.geyifeng.immersionbar:immersionbar:$immersionBarVersion",
            immersionbarKtx          : "com.geyifeng.immersionbar:immersionbar-ktx:$immersionBarVersion",
            systemUiController       : "com.google.accompanist:accompanist-systemuicontroller:0.26.3-beta",
            viewModelCompose         : "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1",
            hiltNavCompose           : "androidx.hilt:hilt-navigation-compose:1.0.0",
            glide                    : "com.github.bumptech.glide:glide:$glideVersion",

            okDownload               : "com.liulishuo.okdownload:okdownload:$okDownloadVersion",
            okDownloadSqlite         : "com.liulishuo.okdownload:sqlite:$okDownloadVersion",
            okDownloadOkhttp         : "com.liulishuo.okdownload:okhttp:$okDownloadVersion",
            okDownloadFileLoader     : "com.liulishuo.okdownload:filedownloader:$okDownloadVersion",
            okDownloadKtx            : "com.liulishuo.okdownload:ktx:$okDownloadVersion",

            okhttp                   : "com.squareup.okhttp3:okhttp:$okhttpVersion",

            tencentTbs               : "com.tencent.tbs:tbssdk:$tencentTbsVersion",

            splashScreen             : "androidx.core:core-splashscreen:1.0.0-beta02",

            coroutineAndroid         : "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineAndroidVersion",

            theRouter                : "cn.therouter:router:$theRouterVersion",

//            dataStoreCore       : "androidx.datastore:datastore-core:$dataStoreVersion",
            dataStore                : "androidx.datastore:datastore:$dataStoreVersion",

            protobufJavaUtil         : "com.google.protobuf:protobuf-java-util:$protobufJavaUtilVersion",

            composeUiTestJunit       : "androidx.compose.ui:ui-test-junit4:$composeVersion", //androidTest
            testJunit                : "androidx.test.ext:junit:1.1.3", //androidTest
            espressoCore             : "androidx.test.espresso:espresso-core:3.4.0", //androidTest
            junit                    : "junit:junit:4.13.2",//test
            composeUiTooling         : "androidx.compose.ui:ui-tooling:$composeVersion",//debug
            composeTestManifest      : "androidx.compose.ui:ui-test-manifest:$composeVersion"//debug
    ]

    static kapts = [
            glide    : "com.github.bumptech.glide:compiler:$glideVersion",
            theRouter: "cn.therouter:apt:$theRouterVersion",
            hilt     : "com.google.dagger:hilt-android-compiler:$hiltVersion"
    ]

    static ksps = [
            room: "androidx.room:room-compiler:$roomVersion"
    ]

    static classpaths = [
            theRouter: "cn.therouter:plugin:$theRouterVersion", //therouter
            protobuf : "com.google.protobuf:protobuf-gradle-plugin:$protobufVersion", //com.google.protobuf
            hilt     : "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
    ]

}