package me.omarim.parstegram;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import me.omarim.parstegram.models.ParseDataSourceFactory;
import me.omarim.parstegram.models.Post;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimelineFragment.Callback} interface
 * to handle interaction events.
 * Use the {@link TimelineFragment} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {


    private PostAdapter postAdapter;
    LiveData<PagedList<Post>> posts;
    RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    int itemCount;

    private Callback listener;

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder().setEnablePlaceholders(true)
                        .setPrefetchDistance(2)
                        .setInitialLoadSizeHint(2)
                        .setPageSize(2).build();

        postAdapter = new PostAdapter(new DiffUtil.ItemCallback<Post>() {
            @Override
            public boolean areItemsTheSame(@NonNull Post post, @NonNull Post t1) {
                return post.getObjectId() == t1.getObjectId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Post post, @NonNull Post t1) {
                return post.getUpdatedAt() == t1.getUpdatedAt();
            }
        }, view.getContext());


        ParseDataSourceFactory sourceFactory = new ParseDataSourceFactory();

        posts = new LivePagedListBuilder(sourceFactory, pagedListConfig).build();

        posts.observe(this, new Observer<PagedList<Post>>() {
            @Override
            public void onChanged(@Nullable PagedList<Post> posts) {
                postAdapter.submitList(posts);
            }
        });
        // populate the recycler view
        loadTopPosts();
        // recycler view set up (layout manager, use andapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(view.getContext()));
        // set the adapter
        rvPosts.setAdapter(postAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // set the item count
                itemCount = postAdapter.getItemCount();
                listener.onRefresh();
                loadTopPosts();

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            listener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnButtonPressListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
    public interface Callback {
        void onRefresh();
    }

    private void loadTopPosts() {
        postAdapter = new PostAdapter(new DiffUtil.ItemCallback<Post>() {
            @Override
            public boolean areItemsTheSame(@NonNull Post post, @NonNull Post t1) {
                return post.getObjectId() == t1.getObjectId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Post post, @NonNull Post t1) {
                return post.getUpdatedAt() == t1.getUpdatedAt();
            }
        }, this.getContext());

        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser().recentFirst();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    postAdapter.notifyDataSetChanged();
                    // stop refreshing
                    swipeContainer.setRefreshing(false);
                } else {
                    e.printStackTrace();
                }
            }
        });

    }
}
