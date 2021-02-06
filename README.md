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
- [ ] [SERIOUS] Bandzone.cz has maintenance shutdown from 3:00 to 3:15 am, track this event, if I cannot do anything with it at least inform user about it.  
- [ ] provide actual screenshots

## Devices tested & issues

```
[runs/dont] [Android version] (App version tested) Phone brand and model - state/issues
```

- [x] [6.0 + EMUI 4.1] (v0.1.1-alpha) Honor 7 lite - OK  
- [ ] [10.0] (v0.1.1-alpha) Samsung Galaxy S10 - App runs, unable to connect and fetch data, test again after deprecations removed  
- [x] [5.1.1] (v0.1.1-alpha) Lenovo A6020 - OK  
- [ ] ~[9.0] (v0.1.1-alpha) BlackView GBW9100 - same problem as on Samsung Galaxy S10~  
- [x] [9.0] (v0.2-alpha) BlackView GBW9100 - OK 

