# WMTU Radio

A free Android app for listening live to **WMTU 91.9 FM**, Michigan Tech's student radio station, built in support of **Reggae Adjacent with DJ TomASS** (live on WMTU.fm every Monday at 6pm).

## Download

Grab the latest APK from the [Releases page](../../releases/latest) — no Play Store account needed. You'll need to allow "install unknown apps" for your browser or file manager the first time.

This app isn't on the Play Store yet (see `docs/PLAY_STORE_LISTING.md` for that prep work in progress).

## Features

- One-tap live streaming of WMTU 91.9 FM
- Pull down to refresh and jump back to the live broadcast
- Choose whether Play resumes from a short buffer or always reloads live, with control over how much is buffered
- Background playback with notification-shade media controls
- Bluetooth-aware playback (handles car stereo / headphone connects and disconnects sensibly)
- A weekly reminder before Reggae Adjacent with DJ TomASS airs
- No accounts, no ads at this time, no analytics, no tracking

## Privacy

This app doesn't collect, store, or share any personal information. Full policy: `docs/PRIVACY_POLICY.md` (same text is also in-app, under Options → Privacy Policy).

## Building from source

Requires Android Studio (or the command line with a configured Android SDK) and JDK 17.

```
git clone https://github.com/proxynerds/reggae-adjacent-wmtu-app.git
cd reggae-adjacent-wmtu-app
./gradlew assembleDebug
```

The debug APK lands at `app/build/outputs/apk/debug/app-debug.apk`.

## About

This app is owned and built by **ProxyNerds**, a Michigan Tech (MTU) alum-founded, founder-owned and operated AI Solutions and IT Services company. ProxyNerds does not own, operate, or have any affiliation with WMTU.fm or Michigan Technological University.

ProxyNerds. We do the nerd so you don't have to.

Questions or feedback: proxynerds911@gmail.com
