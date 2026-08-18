# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Unofficial Android client for bandzone.cz — search bands, stream tracks, download them for
offline playback. Java, no Kotlin, no Compose, no DI. Plain Android SDK plus Volley / Glide /
Room / PRDownloader.

`minSdk 16`, `targetSdk 30`, `compileSdk 36`, AGP 9.1.1, Gradle 9.3.1, Java toolchain 21.
The low `targetSdk` is deliberate — the storage code predates scoped storage, and lint's
`ExpiredTargetSdkVersion` is disabled for that reason. Raising it means auditing storage and
permissions first.

## Commands

```bash
./gradlew assembleDebug            # -> app/build/outputs/apk/debug/app-debug.apk
./gradlew assembleRelease          # unsigned unless keystore.properties exists
./gradlew lint
```

There are **no meaningful tests** — both test classes are untouched templates. Verify changes on
a device, and don't describe a change as test-verified.

Release signing is opt-in: copy `keystore.properties.example` to `keystore.properties`. Without
it the release build stays unsigned so a fresh clone still builds. CI
(`.github/workflows/release-android-app.yml`) is `workflow_dispatch` only and delegates to a
shared reusable workflow.

## Verifying on a device

`adb` is **not on PATH**: use `/home/kovo/Android/Sdk/platform-tools/adb`.

```bash
ADB=/home/kovo/Android/Sdk/platform-tools/adb
$ADB install -r -t app/build/outputs/apk/debug/app-debug.apk   # -t required, package is testOnly
$ADB shell monkey -p com.kovospace.bandzoneplayerunofficial -c android.intent.category.LAUNCHER 1
```

Debug and release share one `applicationId`, so a debug install updates in place and preserves
downloaded music and the Room DBs. **Never uninstall to clean up** — that wipes the user's
offline library under `Android/data`.

`input tap` + `uiautomator dump` drive the UI fine. For timing-sensitive things (a loading
flash, an animation) use `screenrecord` and extract frames — a `uiautomator dump` costs the
better part of a second and will miss the window.

## Architecture

`Main extends Application` initialises the static singletons (`Settings`, `DbHelper`) before any
activity runs.

Two activities over an abstract `Activity` base: **`BandsActivity`** (search + results) and
**`SongsActivity`** (one band's tracks). The base owns a `Connection` and a `CONNECTIVITY_ACTION`
receiver driving the abstract `onNetworkChanged()`. In `BandsActivity` that calls
`refreshActivity()`, which **recreates the activity** and replays the search text via an intent
extra — remember this when a screen appears to reset itself.

### Online/offline wrapper pattern (the core idea)

Each data-bearing screen has an abstract wrapper with a Net and an Offline subclass, chosen at
runtime from `Connection`:

```
DataWrapper (interface: constants + setDataSourceType())
├── BandsWrapper   → BandsWrapperNet / BandsWrapperOffline   (bands list)
└── BandWrapper    → BandWrapperNet  / BandWrapperOffline    (one band's tracks)
```

The wrappers own the RecyclerView, adapter and empty-state text — the activities do not.
`BandsSearch` (a debounced `OnFinishTypingHelper`, 1 s) picks the wrapper and forwards searches.

**`BandsWrapperNet` composes a `BandsWrapperOffline`.** A search paints local Room results
*synchronously* first, then fires the network request and merges. So an empty list mid-search
means "not loaded yet", not "nothing found" — `loadingInProgress` guards the empty-state message
and must be cleared on every exit path (success, empty response, JSON error, `onFailure`).

### Player

`Player` is an **all-static singleton** shared by both activities and `PlayerWidget` in the bands
list — one `MediaPlayer` for the whole app, holding a `Context` and view references, reached into
statically by `TracksAdapter` and `PlayerAnimations`. Changing playback state from a new place
means checking what else already observes it.

### Storage

`FileStorage` (abstract) → `Mp3File` (`music/<bandSlug>/`) and `ImageFile` (`covers/<bandSlug>/`).

`checkStorage()` prefers `getExternalFilesDirs()[1]` — **the removable SD card** — when present.
That card is normally FAT32/exFAT, so on-disk names must go through `Misc.sanitizeFileName()`;
`Track.getFileName()` is the single source of a track's filename and both the downloader and
every `fileExists()` lookup must use it.

### Persistence and cross-screen signalling

Two *separate* Room databases (`OfflineBandsRoomDatabase`, `OfflineTracksRoomDatabase`) behind
static helpers. A band row exists only while at least one of its tracks is downloaded —
`DbHelper.rememberBandAndTracksForOffline()` inserts or deletes the whole band accordingly and
runs after every download and delete.

Screens signal each other through **SharedPreferences flags**, not callbacks or an event bus
(`eventBus/` holds one unused class): `Settings.triggerBandTrackDowloaded()` /
`sendBandDowloadsRemoved()` write a band slug that `BandsWrapper.onResumeChecks()` picks up on
resume. Unusual, but it is the established pattern here.

### Networking

`JsonRequest` (abstract, Volley) with `doStuff()` on success and an overridable `onFailure()`.
Subclasses are inner classes of the wrapper that owns them, so they touch wrapper state directly.

The backend is a **separate private SpringBoot service**, not in this repo:

- `https://music-pages-scraper.matejkovac.sk/bandzone/bands?q=<query>&p=<page>`
- `https://music-pages-scraper.matejkovac.sk/bandzone/band?q=<bandSlug>`
- Local checkout `/home/kovo/IdeaProjects/music-pages-scraper-backend`
  (`git@github.com:Kovospace/music-pages-scraper-backend.git`), which has its own
  `springboot-developer` agent.

Track JSON carries `title`, `href`, `slugRef`, `hrefHash`, `albumTitle`, `albumReleaseYear`,
`albumLabel` — and no duration.

## Conventions

- Comments and TODOs are often in **Slovak/Czech**; user-facing strings are Slovak. Match the
  surrounding language, and add new strings in Slovak.
- Layouts are hand-written XML leaning on `layout_weight`. `activity_main.xml` is a
  `LinearLayout`, so the "no bands found" `TextView` is a *sibling* of the list, not an overlay.
- Git: **never push to `main`.** Branch as `feature/<issue_title>`, `bugfix/<issue_title>`, or
  `other/<issue_title>` when unclear (from `.claude/agents/android-developer.md`).
- `README.md` keeps a dated development log and TODO list — usually the fastest way to find when
  and why something was done.
