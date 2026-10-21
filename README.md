# MotoVoice Recorder — prototype 0.1.0

Native Kotlin Android starter prototype for testing the FreedConn T-COM microphone.

## Included
- Bluetooth and microphone permission requests
- Input/output device diagnostic screen
- M4A (AAC) recording
- Foreground microphone service for background recording
- Attempts to prefer a Bluetooth headset input when Android reports one

## Important
This is source code, not an installed APK. Open the folder in Android Studio, sync Gradle, then Build > Build APK(s). It has not been built or tested on your Samsung yet.

Android may ignore a preferred input route or report a Bluetooth device without actually routing captured audio through it. Confirm with a controlled test: connect T-COM, speak near the helmet mic while keeping the phone away, and compare with Bluetooth off. A live level meter and stronger routing diagnostics should be added after the first device test.

Files are saved in the app-private external Music directory under Android/data/com.edverything.motovoicerecorder/files/Music. Version 1 does not yet include a file browser/export button.

Start recording while the app is visible and grant requested permissions. Test while parked, not while riding.
