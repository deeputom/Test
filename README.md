# WalmartLabsTest
This has two fragments/activities 
1) displaying list
2) displaying details

List fragment supports two features to get uptodate infromation from the server
1. Pull down swipe refresh -> gets the latest infromation from the server. 
2. Action bar refresh -> clears all the local cache and gets fresh download from server

// TODO's
SQLite internal persistent DB is tested and implemented, but not used

// Limitations when swiping between details
Swipe product details works only from the starting postion to the end where the list is loaded.
Swipe cannot go beyond the item where the swipe is started.
Also it cannot load the the items that is not yet downloaded
