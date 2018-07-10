package me.omarim.parstegram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import me.omarim.parstegram.models.Post;

public class CreateActivity extends AppCompatActivity {


    EditText etDescription;
    Button btPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        etDescription = findViewById(R.id.etDescription);
        btPost = findViewById(R.id.btPost);

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = etDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
//
//                // TODO: get an image from the camera or upload it from photos
//
//                parseFile.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        Log.i("Home Activity", "image save success!");
//                        createPost(description, parseFile, user);
//                    }
//                });

                createSimplePost(description, user);

                Intent i = new Intent(CreateActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDectiprion(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) Log.d("Home Activity", "Create Post Success!");
                else e.printStackTrace();
            }
        });
    }

    private void createSimplePost(String description,  ParseUser user) {
        final Post newPost = new Post();
        newPost.setDectiprion(description);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) Log.d("Home Activity", "Create Simple Post Success!");
                else e.printStackTrace();
            }
        });
    }
}
