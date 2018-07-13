package me.omarim.parstegram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import me.omarim.parstegram.models.Post;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateFragment#create} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment {

    EditText etDescription;
    ImageView ivPhoto;
    Button btPost;
    String photoPath;
    public Activity activity;

    private static final String ARG_PHOTOPATH = "ARG_PHOTOPATH";


    private OnFragmentInteractionListener mListener;

    public CreateFragment() {
        // Required empty public constructor
    }

    public static CreateFragment create(String photoPath) {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTOPATH, photoPath);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photoPath = getArguments().getString(ARG_PHOTOPATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        etDescription = view.findViewById(R.id.etDescription);
        ivPhoto = view.findViewById(R.id.ivPhoto);
        btPost = view.findViewById(R.id.btPost);

        Glide.with(view.getContext()).load(photoPath).into(ivPhoto);


        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String description = etDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final File file = new File(photoPath);
                final ParseFile parseFile = new ParseFile(file);

                createPost(description, user, parseFile);

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onButtonPressed();
    }

    private void createPost(String description, ParseUser user, ParseFile imageFile) {
        final Post newPost = new Post();
        newPost.setDectiprion(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Home Activity", "Create Post Success!");
                    mListener.onButtonPressed();
                }
                else e.printStackTrace();
            }
        });
    }

    public void onCreateButtonPressed() {
        if (mListener != null) {
            mListener.onButtonPressed();
        }
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
    }
}
