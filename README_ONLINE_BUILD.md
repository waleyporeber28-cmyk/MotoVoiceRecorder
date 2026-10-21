# Build MotoVoice Recorder on your phone using GitHub Actions

This project includes `.github/workflows/build-apk.yml`, which builds a debug APK on GitHub's hosted runner and uploads it as a workflow artifact.

## Phone-only steps
1. Download this ZIP and extract it using your phone's Files app.
2. On GitHub, create a **new public repository** named `MotoVoiceRecorder` (public repositories can use standard GitHub-hosted Actions without private-repository minute limits; do not add secrets or personal files).
3. In the repository in your mobile browser, use **Add file → Upload files**. Upload the *contents of the extracted project folder*, preserving folder paths, including the hidden `.github/workflows/build-apk.yml` file. If your phone's file picker hides `.github`, use GitHub's web editor to create `.github/workflows/build-apk.yml` and paste the workflow contents from that file, then upload the remaining project files.
4. Commit the files to the `main` branch.
5. Open the repository's **Actions** tab, select **Build MotoVoice Recorder APK**, tap **Run workflow**, then open the running/completed job.
6. When it succeeds, scroll to **Artifacts** and download `MotoVoiceRecorder-debug-apk`. Extract that ZIP; the APK inside can be installed on your Android phone.

Note: This only builds the prototype. The APK has not yet been compiled or tested, and Bluetooth microphone routing may vary by phone and Android version. Install only the APK built from the source you trust.
