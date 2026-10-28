# ProxyNerds Privacy Policy

_Covering: WMTU Radio (Android app)_
_Last updated: October 28, 2026_

This is the repo's source copy. It's kept in sync with two other copies that need to match it word for word if it changes:

- `app/src/main/assets/privacy_policy.txt`, bundled in the app, shown on the Options screen's Privacy Policy page.
- The hosted page linked from `Constants.PRIVACY_POLICY_URL`, the canonical public URL for Google Play Console's Data safety / App content section. See [`PLAY_STORE_LISTING.md`](PLAY_STORE_LISTING.md) for the current URL and a note on making it permanent.

**This is ProxyNerds' privacy policy, not WMTU's or Michigan Tech's.** ProxyNerds built and owns this app, WMTU Radio. WMTU (the radio station) does not own or operate the app, is not affiliated with ProxyNerds, and is not the publisher of this policy. Keep that framing (ProxyNerds owns the app; ProxyNerds has no ownership of or affiliation with the WMTU radio station itself) if this doc is ever restructured. Things to watch for specifically, all corrected 2026-10-28 after user review: an earlier draft led with "WMTU Radio is..." as if the station owned the policy; a later draft said "ProxyNerds owns WMTU Radio" in a way that could misread as ProxyNerds owning the radio station rather than the app (say "owns this app, WMTU Radio," never bare "owns WMTU Radio"); and an earlier draft mentioned ads/no-ads in the policy text itself, which the user asked to remove entirely unless legally required, keeping that information in project docs only (see [`PLAY_STORE_LISTING.md`](PLAY_STORE_LISTING.md) and [`DATA_SAFETY_ANSWERS.md`](DATA_SAFETY_ANSWERS.md)). See [[wmtu-app-proxynerds-ownership-framing]] in the hivemind memory for the full correction history.

---

This is ProxyNerds' privacy policy. ProxyNerds, a Michigan Tech alum-founded, founder-owned and operated AI Solutions and IT Services company, built and owns this app, WMTU Radio. The app was made in support of Reggae Adjacent with DJ TomASS, the reggae show DJ TomASS hosts live on WMTU.fm every Monday at 6pm. WMTU Radio plays WMTU 91.9 FM's publicly available live stream, the way radio-aggregator apps (the kind that let you pick a station from around the country) stream the stations they cover; this app just focuses on one station and one show. ProxyNerds does not own, operate, or have any affiliation with WMTU.fm, WMTU 91.9 FM, or Michigan Technological University, and WMTU is not the publisher of this policy.

## The short version

ProxyNerds doesn't collect, store, or share any personal information through this app. There are no user accounts, no analytics, and no trackers.

## What the app does with data

- **Streaming audio.** When you press Play, your device connects directly to WMTU's streaming server to receive the live broadcast. That connection is between your device and WMTU's server. ProxyNerds does not see, log, or relay this traffic.
- **App preferences.** Your Playback option (resume where you left off vs. reload live) and your buffer-length setting are saved only on your own device, using Android's standard local app storage. They are never transmitted to ProxyNerds or anyone else, and are deleted if you uninstall the app.
- **Live buffer.** To let you briefly pause without losing your place, the app keeps a short rolling buffer of recently played audio in your device's memory (RAM) while the app is running. This buffer is never written to your device's storage and is cleared automatically when you reload the stream, close the app, or restart your device.
- **No accounts, no sign-in, no personal information is ever requested.**

## Permissions the app requests, and why

| Permission | Why |
|---|---|
| Internet / network state | Required to play the live audio stream. |
| Foreground service / media playback | Keeps the stream playing when the app is in the background or your screen is off. |
| Notifications | Shows the playback controls in your notification shade while listening, and the weekly reminder before Reggae Adjacent with DJ TomASS airs. |
| Bluetooth connect | Lets the app notice when you connect to or disconnect from a Bluetooth audio device (like a car stereo or headphones), so playback can pause/resume sensibly. |
| Schedule exact alarms / receive boot completed | Used only to fire the weekly show reminder at the right time, including after your device restarts. |
| Wake lock | Keeps audio streaming reliably while the screen is off. |

None of these permissions are used by ProxyNerds to collect or share data about you. They exist only to make live playback and the show reminder work correctly.

## Third parties

ProxyNerds does not use any third-party analytics or crash-reporting SDKs in this app. The only outside connection the app makes is the direct stream request to WMTU's own streaming server to play the broadcast.

## Children's privacy

This app is not directed at children under 13, and ProxyNerds does not knowingly collect any information from anyone through it, regardless of age.

## Changes to this policy

If this policy changes, the update will ship with a new app version and an updated "Last updated" date above. Keep all three copies listed at the top of this file in sync. If ProxyNerds publishes additional apps in the future, this same policy may be extended to cover them, clearly noting what each app does differently.

## Contact

ProxyNerds. Questions, feedback, or data requests: proxynerds911@gmail.com

ProxyNerds, AI Solutions & IT Services, Michigan Tech alum-founded, founder-owned and operated
