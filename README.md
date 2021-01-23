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

## TODO, ideas

- [ ] Show user that band has no published tracks  
- [x] When search deleted, load random bands  
- [x] Notify user somehow that search is being performed (maybe little spinner in search field)  
- [ ] Maybe apply gestures for some actions (like back from Band profile to bands search)  
- [x] Rework band profile, find out how to put band info into list so it does not occupy space on screen and can be scrolled out  
- [ ] Make appearing band search results animated  
- [ ] Pre Kristovu nohu if no bands found according to user request TELL HIM about it, no blank screen  
- [ ] Resolve recyclerView artifacts - sometimes wiew with hidden buttons appearing for a wile can be seen (recycled view)  
