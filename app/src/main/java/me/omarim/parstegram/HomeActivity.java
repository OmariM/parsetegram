package me.omarim.parstegram;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

import me.omarim.parstegram.models.Post;

public class HomeActivity extends AppCompatActivity {

    EditText etDescription;
    Button btRefresh;
    Button btCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadTopPosts();

        etDescription = findViewById(R.id.etDescription);
        btRefresh = findViewById(R.id.btRefresh);
        btCreate = findViewById(R.id.btCreate);

        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();
            }
        });

    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        // TODO - create post
    }

    private void loadTopPosts() {

        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) Log.d("Home Activity", "Post[" + i + "] = "
                            + objects.get(i).getDescription()
                            + "\nusername = " + objects.get(i).getUser().getUsername());
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

}
