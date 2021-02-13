# BandzoneAndroidUnofficial

![alt text](https://github.com/K0V0/BandzoneAndroidUnofficial/blob/obrazky/Screenshot_2021-01-04-01-49-35.png?raw=true)

## Development.log

| day | work done |
| --- | --------- |
| 29-dec-2020 | created bandzoneScraper RoR api |
| 30-dec-2020 | first work on app, searchfield, text events, JSON parsing |
| 04-jan-2021 | displaying list of bands, only first 20 results |
| 05-jan-2021 | loading of next results after scroll, UI tweaks, messages |
| 18-jan-2021 | modifying scraper server app, open action on bands list, adding layout for band and their songs, json requests for band data working |
| 19-jan-2021 | complete refactor of app, code cleanup, making adapter for filling bands activity with data, adding abstract classes and interfaces <br>Data and JSON deserialization adapter for Band and it's tracks |
| 20-jan-2021 | showing band basic details works, playing with animations and layouts, waiting screen when data for bandloaded from server |
| 21-jan-2021 | refactor of code handling opening band portfolio, first goal achieved - app is PLAYING SOUND. Insane level of pain how to synchronise the UI and state(s) of player in some nice user friendly way |
| 22-jan-2021 | player synchronized with UI ~~except case when You get out of band profile and back - but solution proposed~~, resolve recyclerView artifacts |
| 23-jan-2021 | pause and rewind and progressbar working now, get sober, 3.6 promile for me not terrible but for app not great |
| 24-jan-2021 | UI animations, bugs, icons and look, no band found text, moving player loading to background thread |
| 01-feb-2020 | Resolving deprecation warnings, design of player widget |
| 04-feb-2020 | Give "life" to player widget (player in bands list) |
| 05-feb-2020 | Resolved bug app not loading content on newer androids, adding time informations to player(s) |
| 08-feb-2020 | UI changes, click listeners on player, started studying android storage and implementing file download |
| 09-feb-2020 | File downloading and removing implemented, playing downloaded files from mem implemented, started studying and implementing SQL database, very basic core functionality implemented |
| 10-feb-2020 | Caching (saving) of images of visited band profile, basic operations on databases done (adding band profile if at least one track downloaded, removing if all downloads deleted) |
| 11-feb-2020 | Started implementing adapters for running app from local dbs and stored files, tried to offload recycler views from unnecessary ofline assets chekcs |
| 13-feb-2020 | Offline mode fully implemented, implemented changing mode based on phone connection state, 0.3-a release |

## TODO, ideas

- [x] Show user that band has no published tracks  
- [x] When search deleted, load random bands  
- [x] Notify user somehow that search is being performed (maybe little spinner in search field)  
- [ ] Maybe apply gestures for some actions (like back from Band profile to bands search)  
- [x] Rework band profile, find out how to put band info into list so it does not occupy space on screen and can be scrolled out  
- [ ] Make appearing band search results animated  
- [x] Pre Kristovu nohu if no bands found according to user request TELL HIM about it, no blank screen  
- [x] ~Resolve recyclerView artifacts - sometimes wiew with hidden buttons appearing for a wile can be seen (recycled view)~ they dissappeared, but I did nothing  
- [x] Put some small player widget on the bottom when in bands index/other band profile to give a user possibility to switch/stop currently playing track and give him clue what is he listening to  
- [x] Few bands unable to load, inpect this cases - [RESOLVED] - bands with at least one song that is less than miute long - bug in a Ruby on Rails scraper, not app itself  
- [x] Get rid of some HANDROid ugly stock icons  
- [x] [SERIOUS] Run player on background thread, loading track freezes UI  
- [x] [SERIOUS] Spaces in searchfield causes exception, scraper working OK, mistake here  
- [x] [SERIOUS] (in 0.1-alpha) If list of the track longer than screen and last track ends, app will crash  
- [x] [SERIOUS] on newer androids, content fetching not working 
- [ ] develop some mechanism to try to repeat lost request  
- [ ] [SERIOUS] Bandzone.cz has maintenance shutdown from 3:00 to 3:30 am, track this event, if I cannot do anything with it at least inform user about it.  
- [ ] provide actual screenshots  
- [ ] lockscreen player widget  
- [ ] notification area player widget  
- [ ] text on widget in bands list not changeing when song changed automatically  
- [x] change click listeners on track - missclick on free space in player causes track play again  
- [x] ability to download tracks and in gerenal ability to run offline with downloaded content - bands info and tracklist must be downloaded if at least one track is downloaded from band  
- [ ] add incremental loading (pagination) for offline bands too (if for example 1000 results is issue for recycler view and sqlite, do research)  
- [x] reload bands activity on step back if connection changed during time spent inside band profile  
- [x] when playback paused in bands list and back into band profile or opposite, player state (in bands list) or player at all (in band profile) not restored  
- [ ] add icon next to searchfield (in lace when loading wheel occurs) in bands list signalizing player mode (offline/online)  
- [ ] ^ use that icon as basic settings menu to toggle player mode by user  
- [ ] ^ in that menu, give user the possibility to work in offline mode even if data on (architecture done, just do menu)  
- [x] [IMPORTANT] in offline mode, grey out and remove click listeners on tracks that has not been downloaded  
- [x] ^ change download icon on that tracks to something that will inform user that he must download it first when online  
- [x] [SERIOUS] when going into band profile in offline mode, something black occupy half of the screen until track played, then content jumps  
- [x] [IMPORTANT] reload activities when connection on phone changed
- [x] bug somewhere in db queries, removein all downloaded songs but band remains there
- [x] sometimes when turning off data and paused player in band profile does not dissapear from not downloaded track
- [x] maybe add bands that have downloaded tracks as first when app opened/searchfiled cleared
- [x] [SERIOUS] bug resuming band profile activity while playing track - player missing, if clicked, app crashes

## Devices tested & issues

```
[runs/dont] [Android version] (App version tested) Phone brand and model - state/issues
```

- [x] [6.0 + EMUI 4.1] (v0.1.1-alpha) Honor 7 lite - OK  
- [ ] [10.0] (v0.1.1-alpha) Samsung Galaxy S10 - App runs, unable to connect and fetch data, test again after deprecations removed  
- [x] [5.1.1] (v0.1.1-alpha) Lenovo A6020 - OK  
- [ ] ~[9.0] (v0.1.1-alpha) BlackView GBW9100 - same problem as on Samsung Galaxy S10~ [tested v0.2-alpha working] 
- [x] [9.0] (v0.2-alpha) BlackView GBW9100 - OK  

