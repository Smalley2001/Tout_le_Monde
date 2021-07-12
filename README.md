Original App Design Project - README Template
===

# Tout le Monde

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This is an app that allows users to create protest events to
advocate for social change an enables others to get information
on where the events are relative to their location.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category: Social advocacy**
- **Mobile: Android Mobile App**
- **Market: Everyone**

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Allow the user to sign up/login to the app using Parse
* User can create an event and allow others to sign up
* Event location is visible using Google Maps API (potentially difficult)
* Users can search for events to sign up for. 
* Users can see the events that they signed up for
* User can cancel an event that they created and the event must be removed for all individuals who signed up
* User can cancel their sign up for an event
* User can filter events based on the distance away from their actual location



**Optional Nice-to-have Stories**

* The user can contact the host if they have questions possibly using the Twilio API
* The user can create an event and the event will be saved in a database
* User can sign in using Google Sign-in SDK or Facebook SDK
* User can filter events based on the distance away from their actual location
* User can filter events based on the dates the events will take place
* User can filter events based on social advocacy campaign (Black Lives Matter, LGBTQ+, Stop Asian Hate)
* Create a social advocacy details page to for each group to explain the purpose of the movements
* Display the number of people who signed up for an event and update the number when a new person signs up or when a person cancels their sign up
* Create sliding images for UI customizations 
* User receives a notification when they sign up for an event and when the user cancels sign up.

### 2. Screen Archetypes

* Welcome/Login screen
   * This page will have animations as pictures appear like a slideshow. The pictures will be rendered using the external library Glide.
   * Required user story: Your app uses at least one animation (e.g. fade in/out, e.g. animating a view growing and shrinking)
   * Required user story: Your app incorporates at least external library to add visual polish
   * The login functionality will either be creating our login database or using the Google Sign in SDK
   * Required User Story: You can log in/log out of your app as a user
   * At the bottom of the UI there will be a clickable link for users who need to register  to go  otthe Registration page
   * Required User Story: * You can sign up with a new user profile
  
* Registration screen
   * This page will handle the logic for registration of a new user. If we use our own database, then the user's information will simply be added the database to authenticate so when the user returns, they won't have to register again.
   * Required User Story: You can sign up with a new user profile
   * Required User Story:  Your app interacts with a database (e.g. Parse)

* Home Screen
   * This screen will provide the menu options for what the user would like to do (such as make an event or look at the recent events they signed up for.) This would be implement using a GalleryView
   * Required Story: Your app has multiple views
 
* Create Event Page
   * This page will have a form for the user to enter their information. (Needs additional research) The form will more than likely have to be saved in a database so it can appear not only for the user but for other users to see as well.
   * The location of the event will be showed on a map using the Google Maps API 
   * Required user story: Your app integrates with at least one SDK (e.g. Google Maps SDK, Facebook SDK) or API (that you didn’t learn about in CodePath)
   * Required User Story: Your app provides opportunities for you to overcome difficult/ambiguous technical problems
  
* [Find event Page]
   * User can find nearby events using the Google Maps API and click the event to sign up. The events would be in a Recycler View
   * Required user story: Your app has multiple views
   * Required user story: Your app integrates with at least one SDK (e.g. Google Maps SDK, Facebook SDK) or API (that you didn’t learn about in CodePath)

* [Event page]
   * This page will showcase a specific event and allow users to sign up for the event and update the database 
   * Research which gesture to use in order to confirm sign up
   * Required User story:  Your app uses at least one gesture (e.g. double tap to like, e.g. pinch to scale)
   
* [My events page]
   * This page will show all the events a user created and allow them to cancel and event (more research needed)

* [Signed up events page] 
   * This page will show all the events the user signed up for and allow them to cancel their sign up (more research needed)

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* On signed up events page, there will be an icon to cancel sign up which would update the database and other corresponding pages
* On created events page, there will be an icon to cancel event and update the database accordingly.

**Flow Navigation** (Screen to Screen)

* Login --> Home
   * After user has logged in, redirect them to home using intent
   
* Login --> registration
   * If it is a new user, send them to registration page using intent
   
   
* Registration --> Home
   * Now that user is registered, send them to home
   * ...

* Home --> Create an Event
   * user chose to make an event so send them to create an event page using intent

* Home --> Find an event
   * User chose to search for an event so send them to find an event page using intent

* Home -- > My events
   * User chose to see the events they created so send them to my events page

* Home -- > Signed up Events
   * User chose to see the events they signed up for so send send them to Signed up Events page

* These activities can all redirect back to home if necessary

* Create an event -- > My events page
   * Now that user created an event, redirect to my events so user can see their new event

* My events page -- > Home
   * If user clicks back button, send back to home using intent

* My events page -- > Event page
   * If user clicks on an event, send them to the actual event page using intent

* Event page --> Home
   * user can return back to home page after viewing event


## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
User class has following attributes:
    * Object ID (String)
    * updatedAt (Date)
    * createdAt (Date)
    * username (String)
    * password (String)
    
Event class has following attributes:
    * Object ID (String)
    * updatedAt (Date)
    * createdAt (Date)
    * Host      (Pointer to User object)
    * Campaign  (String)
    * Description (String)
    * Location    (String)
    * Max_Participants (Number)
    * Image            (File)
    * Canceled         (boolean)
    
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
