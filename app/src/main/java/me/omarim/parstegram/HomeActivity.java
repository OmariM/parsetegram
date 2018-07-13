package me.omarim.parstegram;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements TimelineFragment.Callback, CreateFragment.OnButtonPressListener, ProfileFragment.OnFragmentInteractionListener {

    public final static int FROM_CAMERA_REQUEST_CODE = 1;
    public final static int BACK_TO_TIMELINE = 2;
    String mCurrentPhotoPath;


    FragmentTransaction fragmentTransaction;
    BottomNavigationView bottomNavigationView;

    Fragment timelineFragment = new TimelineFragment();
    Fragment profileFragment = new ProfileFragment();

    String APP_TAG = "PARSTEGRAM";
    File photoFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        // handle navigation selection
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentTransaction.replace(R.id.fragmentContainer, timelineFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        fragmentTransaction = fragmentManager.beginTransaction();

                        switch (item.getItemId()) {
                            case R.id.action_timeline:
                                fragmentTransaction.replace(R.id.fragmentContainer, timelineFragment).commit();
                                return true;
                            case R.id.action_camera:
                                if ( Build.VERSION.SDK_INT >= 23 &&
                                        ContextCompat.checkSelfPermission( HomeActivity.this, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
                                    Toast.makeText(HomeActivity.this, "Make sure you have permissions enabled!", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                dispatchTakePictureIntent();
                                fragmentTransaction.replace(R.id.fragmentContainer, CreateFragment.create(mCurrentPhotoPath)).commit();
                                return true;
                            case R.id.action_profile:
                                fragmentTransaction.replace(R.id.fragmentContainer, profileFragment).commit();
                                return true;
                        }

                        return false;
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == FROM_CAMERA_REQUEST_CODE) {
//            Intent i = new Intent(this, CreateActivity.class);
//            i.putExtra("photoPath", mCurrentPhotoPath);
//            startActivity(i);

            CreateFragment.create(mCurrentPhotoPath);


        }
//
//        if (resultCode == RESULT_OK && requestCode == BACK_TO_TIMELINE) {
//            Toast.makeText(this, "Back to time line received", Toast.LENGTH_LONG).show();
////            postAdapter.clear();
//            loadTopPosts();
//        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "me.omarim.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, FROM_CAMERA_REQUEST_CODE);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Make sure you have permissions enabled!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"me.omarim.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, FROM_CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onButtonPressed() {
        bottomNavigationView.setSelectedItemId(R.id.action_timeline);
    }


    @Override
    public void onRefresh() {
        bottomNavigationView.setSelectedItemId(R.id.action_timeline);
    }

    @Override
    public void onFragmentInteraction() {
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
