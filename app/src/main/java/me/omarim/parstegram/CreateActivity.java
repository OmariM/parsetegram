package me.omarim.parstegram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import me.omarim.parstegram.models.Post;

public class CreateActivity extends AppCompatActivity {


    EditText etDescription;
    ImageView ivPhoto;
    Button btPost;
    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        etDescription = findViewById(R.id.etDescription);
        ivPhoto = findViewById(R.id.ivPhoto);
        btPost = findViewById(R.id.btPost);

        Bundle extras = getIntent().getExtras();
        photoPath = (String) extras.get("photoPath");
        final Bitmap takenImage = BitmapFactory.decodeFile(photoPath);
        ImageView ivPhoto = findViewById(R.id.ivPhoto);
        ivPhoto.setImageBitmap(takenImage);

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO:  RESIZE BITMAP

                final String description = etDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final File file = new File(photoPath);
                final ParseFile parseFile = new ParseFile(file);

                createPost(description, parseFile ,user);

                Intent i = new Intent(CreateActivity.this, HomeActivity.class);
                startActivityForResult(i, HomeActivity.BACK_TO_TIMELINE);
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
