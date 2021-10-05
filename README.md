# android-shift-manager
The prototype application is intended for the delivery drivers and allows them to track their shifts and orders.

# Introduction
- The application is intended for the delivery drivers and allows them to track their shifts and orders.
- Each driver has the option of creating new an individual account and after logging in has access only to data that belong to him
- Through the application, driver can manage his orders and shifts.
- Application displays order list for shift and provide see them on map.
- Driver can notify client by sending SMS

# Architecture
![image](https://user-images.githubusercontent.com/28375942/136111734-5dbf53b5-70b6-4469-9ba5-195935eafb8e.png)

# API
- Google Maps
- Google Places
- Firebase Realtime and Auth
- SMS

# UI
- Login and registration
- Adding, editing and removing shifts and orders
- Preview of current orders on the google map
- Sending a text message to the customer
- Current shift time in the notification bar
- Auto-complete addresses using Google Places

# Implementation
- Drawer and toolbar menu
- Fragments
- Various orientation layouts
- Date and time picker
- Custom Listeners
- Recycler views
- Serialize data

# Technologies and patterns
- Services
- Notifications
- Broadcasting
- AsyncTasks
- MVC and Observers
- Singletons
- Interfaces
- Error handling and throwing own exceptions

# Evaluation
- The current prototype provides the required features
- Using the application is possible, keeping in mind application have some imperfections and simplifications. 
- Application design is underdeveloped
- Some „spaggeti” code and hard references in some places
- Tricks and shortcuts to provide the application on time
- There may be inappropriate use of observers 
- Mixed model with controller

# Known issues
- Bad map zooming for one order
- No title in toolbar for some fragments
- Wrong pin on map caused by bad or empty address
- Skipped validation and verification for inputs
- Logout not ends MainActivity
- Service show notification if application is run in background (no data access)
- Start / End shift button got delay (Firebase)
- Limited handle for Firebase Auth – not meaningfull message to user

# Preview
Start with button, end with notification
![image](https://user-images.githubusercontent.com/28375942/136113571-4b1d1f78-9882-4848-b2e6-6a59328a5651.png)

Register/Fail/Login
![image](https://user-images.githubusercontent.com/28375942/136113544-e5474f50-85c3-4026-ab28-8de61ee3fb83.png)



