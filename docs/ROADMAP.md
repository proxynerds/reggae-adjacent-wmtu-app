# WMTU Radio, future ideas

Enhancements requested but not yet built. Scope/design notes only, nothing started.

## Now-playing song display

Added 2026-10-28 at the user's request: show the song currently playing, if that's possible.

Feasibility depends entirely on what WMTU's streaming setup exposes:
- **ICY metadata in the stream itself.** The live stream already responded with `icy-metaint: 16000` when probed (see the buffer memory estimate work in this same session), meaning the Icecast/nginx relay does embed inline ICY metadata blocks (title/artist text) every 16000 bytes of audio. ExoPlayer can expose this via `Player.Listener.onMediaMetadataChanged` or by listening for `IcyInfo`/`IcyHeaders` metadata frames (media3-extractor's Icy support) without any extra network call, since it's already part of the same audio connection.
- If that metadata turns out to be empty or unreliable (some station automation systems don't populate it consistently), a fallback would be polling WMTU's own "now playing" API/page if one exists (not checked yet).

Rough shape if pursued: PlaybackService reads the ICY metadata already flowing through ExoPlayer, publishes it on the MediaSession (so it also shows in the notification/lock screen, which Media3 handles for free via `MediaMetadata`), and PlayFragment displays it near the status text.

## Discord link

Added 2026-10-28 at the user's request: a link to a Discord page (presumably WMTU's or Reggae Adjacent's own server, not specified yet).

Needs from the user before building: the actual Discord invite URL. Once provided, this is a trivial addition, same pattern as the existing `wmtu_website_link`/`wmtu_archives_link` rows on the Play screen (a `Constants.DISCORD_URL` + a clickable TextView opening it via `ACTION_VIEW`).

## ~~Separate About tab~~ (done)

Originally logged 2026-10-28 as a later idea, then the user asked for it the same day. Shipped as a third bottom-nav tab (`AboutFragment` / `fragment_about.xml`), but scoped differently than first drafted here: it holds a short WMTU blurb (with the broadcast-rings mark) and a short Michigan Tech blurb (with the MTU seal), not the ProxyNerds credits block -- the user said the ProxyNerds section is good where it is, in Options. Note: the MTU seal is the university's *official* seal; flagged to the user as worth checking against MTU's trademark/brand guidelines before any public release, not yet resolved.
