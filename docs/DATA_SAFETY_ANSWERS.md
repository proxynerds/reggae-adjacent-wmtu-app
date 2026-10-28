# Google Play Console Data Safety form answers

Draft answers for Play Console's **App content > Data safety** section, based on what the app actually does as of 2026-10-28 (see [`PRIVACY_POLICY.md`](PRIVACY_POLICY.md) for the full reasoning). Re-check this doc against the real form each time it changes, and update it the moment the app starts collecting anything new (e.g. if the planned FCM remote-trigger feature from the homelab to-do list ships, that would add Firebase/FCM as a data-collecting SDK and this whole doc needs revisiting first).

## Does your app collect or share any of the required user data types?

**No.**

Google's Data safety form, when you answer "No" to the top-level question, doesn't ask the detailed per-data-type questions below. This section just documents the reasoning in case that top-level answer is ever challenged in review, or needs revisiting after a feature change.

## Walkthrough of Play's data categories, for reference

| Category | Collected? | Notes |
|---|---|---|
| Location | No | Never requested or accessed. |
| Personal info (name, email, address, etc.) | No | No accounts, no forms, nothing submitted by the user. |
| Financial info | No | N/A |
| Health and fitness | No | N/A |
| Messages | No | N/A |
| Photos and videos | No | N/A |
| Audio files | No | The app *plays* a live broadcast stream but does not record, save, or transmit audio from the user's device. |
| Files and docs | No | N/A |
| Calendar | No | N/A |
| Contacts | No | N/A |
| App activity (app interactions, in-app search, installed apps, other actions) | No | No analytics SDK is present. Local preferences (resume mode, buffer length) stay in Android SharedPreferences on-device and are never transmitted. |
| Web browsing | No | N/A |
| App info and performance (crash logs, diagnostics) | No | No crash-reporting SDK (e.g. Crashlytics) is integrated. |
| Device or other IDs | No | No advertising ID, no device identifier is read or transmitted. |

## Security practices questions

- **Is data encrypted in transit?** Not applicable, no user data leaves the device. (The live stream request itself is a standard HTTPS connection to WMTU's server, which Play may ask about separately as "network requests"; that traffic carries no user data, just the stream itself.)
- **Can users request data deletion?** Not applicable, nothing is collected to delete. Uninstalling the app removes its local SharedPreferences.
- **Is data collection optional?** Not applicable.

## If this changes later

Revisit this doc before any of these ship, since each would require real Data Safety answers instead of a blanket "No":
- The FCM-based remote-trigger feature (DJ Tom skipping a week's reminder) scoped in the homelab to-do list. This would add Firebase Cloud Messaging, which Play's Data safety form treats as a data-collecting SDK (device/other IDs at minimum).
- Any crash-reporting or analytics SDK, if one is ever added.
- Any account/sign-in feature.
- Any ad SDK. The app shows no ads as of 2026-10-28, but that's a current-state fact, not a permanent promise (see [[wmtu-radio-app]]). An ad network would add advertising-ID collection and needs its own Data Safety entry before it ships.
