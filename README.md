# Virtual Camera Test Studio (safe local preview)

This is a small Android Studio project for selecting an image or prerecorded video and previewing it inside the app. It does **not** replace the Android system camera or inject a feed into Chrome or other apps.

## Build
1. Install Android Studio with Android SDK Platform 36 and a compatible JDK (Android Studio's bundled JDK is fine).
2. Open this folder in Android Studio.
3. Allow Gradle sync to finish.
4. Use **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. Find the APK under `app/build/outputs/apk/debug/app-debug.apk`.

The project uses Android's document picker, so it does not need broad storage permissions.


## Build from a phone using GitHub Actions
1. Create a new GitHub repository.
2. Upload the contents of this folder to the repository root (including `.github/workflows/android.yml`).
3. Open the repository's **Actions** tab and enable workflows if prompted.
4. Run **Build Android APK** using **Run workflow**, or push a commit to `main`.
5. Open the completed workflow run, find **Artifacts**, and download `virtual-camera-test-studio-debug-apk`.
6. Extract the artifact ZIP on your phone and install the APK if Android allows installation from that source.

This builds the local-preview app only. It does not provide a system-wide virtual camera.
