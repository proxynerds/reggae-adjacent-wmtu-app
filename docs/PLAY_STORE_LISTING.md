# Google Play Store listing, draft

Draft copy and prep notes for publishing WMTU Radio to the Play Store. Nothing here has been submitted yet, treat it as a starting point to edit, not a final listing.

## Privacy policy URL

**Current (development) URL:** https://claude.ai/artifact/3js9ukEwabjbTWBRCRGWxx

This is a Claude Artifact page and is **private by default**, so before pointing Play Console at it, either:
1. Open the page and use its Share menu to make it publicly viewable, or
2. Move the content to a more permanent host you control.

**Note on option 2:** this repo is actually **private** (not public, despite an earlier draft of this doc saying otherwise), and a repo transfer from `github.com/epipenRX/reggae-adjacent-wmtu-app` to `github.com/proxynerds/reggae-adjacent-wmtu-app` was initiated 2026-10-28 and is pending acceptance by the proxynerds account. GitHub Pages needs either a public repo (free on GitHub Free) or a paid plan to publish from a private one -- so either flip this repo to public once it's settled under proxynerds, or host the privacy policy page somewhere else entirely (a small static site, Netlify/Vercel free tier, etc.). Whichever host you pick, the content to publish is in [`PRIVACY_POLICY.md`](PRIVACY_POLICY.md) (or reuse `app/src/main/assets/privacy_policy.txt`, same text). Option 2 (a host you own) is still the safer long-term choice over the Artifact link -- Play re-checks the URL periodically.

## App details

- **App name:** WMTU Radio
- **Package name:** `com.epipenrx.wmturadio`
- **Category:** Music & Audio
- **Content rating:** Expect PEGI 3 / Everyone. No user-generated content, no ads at this time, no in-app purchases, no objectionable content. Confirm by completing Play Console's content rating questionnaire (IARC) at submission time; this isn't a substitute for that.
- **Contact email:** proxynerds911@gmail.com
- **Developer:** ProxyNerds

## Short description (max 80 characters)

> Listen live to WMTU 91.9 FM and never miss Reggae Adjacent with DJ TomASS.

(75 characters)

## Full description (max 4000 characters)

> WMTU Radio is a simple, no-nonsense app built by ProxyNerds in support of Reggae Adjacent with DJ TomASS, the reggae show DJ TomASS hosts live on WMTU.fm every Monday at 6pm. It plays WMTU 91.9 FM's live stream so you never miss the show, and works great the rest of the week too.
>
> Press play and you're listening, live, with no sign-up and no clutter. Playback keeps running in the background and shows up in your notification shade, works with Bluetooth car stereos and headphones, and resumes sensibly when you reconnect.
>
> Built especially for fans of Reggae Adjacent with DJ TomASS, the app can remind you every week before the show starts, so you never miss a set.
>
> Features:
> • One-tap live streaming of WMTU 91.9 FM
> • Pull down to refresh and jump back to the live broadcast
> • Choose whether Play resumes from a short buffer or always reloads live, and how much buffer to keep
> • Background playback with media notification controls
> • Bluetooth-aware playback (handles car stereo / headphone connects and disconnects)
> • Weekly reminder before Reggae Adjacent with DJ TomASS
> • No accounts, no ads at this time, no analytics, no tracking (see our privacy policy for details)
>
> This app is built and owned by ProxyNerds, a Michigan Tech alum-founded, founder-owned and operated AI Solutions and IT Services company, in support of Reggae Adjacent with DJ TomASS, the way many radio-aggregator apps are built: by streaming a station's own publicly available broadcast. ProxyNerds does not own, operate, or have any affiliation with WMTU.fm or Michigan Technological University. Questions or feedback: proxynerds911@gmail.com
>
> ProxyNerds. We do the nerd so you don't have to.

(~1,050 characters, well under the 4000 limit; room to expand later, e.g. with a release-notes-style changelog section if desired.)

## Graphics checklist (not yet produced)

Play Console requires these; none exist yet as of 2026-10-28:
- [ ] App icon (512×512 PNG, 32-bit with alpha), can be exported from `app/src/main/res/drawable/ic_launcher_foreground.xml` plus background, or redrawn at that size for crispness.
- [ ] Feature graphic (1024×500 PNG/JPEG), a banner for the store listing. The `reggae_adjacent_logo.png` artwork or the WMTU gold/black palette would work as a starting point.
- [ ] At least 2 phone screenshots (recommend 4): Play screen (showing the show artwork + play button), Options screen (showing the playback/buffer settings), and the notification shade with playback controls.

## Store listing contact / about section

- **Website:** none yet, could point to a GitHub Pages privacy-policy host if one is set up (see above), or omit.
- **Email:** proxynerds911@gmail.com

## Before submitting

- [ ] Finalize and host the privacy policy at a permanent URL (see above); update `Constants.PRIVACY_POLICY_URL`.
- [ ] Complete Play Console's Data safety form using [`DATA_SAFETY_ANSWERS.md`](DATA_SAFETY_ANSWERS.md) as the draft source.
- [ ] Produce the graphics listed above.
- [ ] Complete the IARC content rating questionnaire.
- [ ] Decide on a release track (internal testing → closed testing → production) and who the initial testers are (Tom? a handful of listeners?).
- [ ] Generate a real release signing key if one doesn't exist yet (`keystore.properties` / `signingConfigs.release` are already wired up in `app/build.gradle` from a prior session; confirm a real keystore file is in place before the first release build).
