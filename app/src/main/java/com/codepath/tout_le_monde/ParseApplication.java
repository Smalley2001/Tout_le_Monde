package com.codepath.tout_le_monde;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Event.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("RYrBvOCUPFvQXi5JxnBQ7z4mpBJNy0yReliQPdRL")
                .clientKey("6ZRj5SLYbMqRu5RXySiUCDMw71Tl9G90ZNfygLtd")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}