package me.omarim.parstegram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.omarim.parstegram.models.Post;

public class ParseApp extends Application {

    public void onCreate() {
        super.onCreate();

        // let the app know that we have custom objects
        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration config = new Parse.Configuration.Builder(this)
                .applicationId("spongebob")
                .clientKey("cookin-up-dope-with-a-uzi")
                .server("http://omari-fbu-parstegram.herokuapp.com/parse")
                .build();

        Parse.initialize(config);
    }

}
