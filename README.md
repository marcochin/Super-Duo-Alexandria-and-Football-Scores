# Super-Duo-Alexandria-and-Football-Scores
Android: In this project we are given two unfinished apps with the intention of incorporating the suggested functionality based on Customer/Boss criticism.

---

### Notes:
API key has been removed from Football Scores! Please create your own API key by signing up at http://api.football-data.org.
Once you have your API key, please insert it in the `strings.xml` under the string: </br>
`<string name="api_key" translatable="false">ADD API KEY HERE</string>`

---
#### Alexandria
Alexandria is a barcode scanner app for books.
It uses the Google books api to fetch information about a certain book. You can then save the book to the
database and view its details.

* Fixed a customer complaint that said app is crashing when there is no internet connection.
* Fixed crashes when a user would rotate the screen.
* Implemented the barcode scanning functionality by using the ZBar code scanner library.
* Uses a `CursorLoader` to load data from database once a network query has been completed.

![](http://i.imgur.com/vc8TEbm.png)

---
#### Football(Soccer) Scores
This is an app that lets you view football scores of a certain day. This app comes built in with a widget too!
If you're a football fan this may come in handy.

* Provided a collection widget that can be displayed on the home screen to view scores without opening the app.
* Implemented accessibility for those without perfect vision in the form of content descriptions (talk-back) and navigable using the D-Pad.
* Activated and fixed issues with Right-To-Left-Layouts(RTL).

![](http://i.imgur.com/Nm9pQ8g.png)
