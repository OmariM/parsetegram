package me.omarim.parstegram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.omarim.parstegram.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    List<Post> posts;
    Context context;

    // pass in the tweets array from the constructor
    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    // for each row inflate the layout and cache them into a viewholder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);
        viewHolder.tvBody.setText(post.getDescription());
        viewHolder.tvHandle.setText(post.getUser().get("handle").toString());
        Glide.with(context).load(post.getImage().getUrl()).into(viewHolder.ivPostImage);
        // TODO: timestamp stuff
//        viewHolder.tvTimestamp.setText(getRelativeTimeAgo(post.createdAt));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    //TODO: fix this
//    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
//    public String getRelativeTimeAgo(String rawJsonDate) {
//        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
//        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
//        sf.setLenient(true);
//
//        String relativeDate = "";
//        try {
//            long dateMillis = sf.parse(rawJsonDate).getTime();
//            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
//                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return relativeDate;
//    }

    public void clear() {
        posts.clear();;
        notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivPostImage;
        public TextView tvBody;
        public TextView tvHandle;
        public TextView tvTimestamp;

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            // ensure position validity
            if(position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                //TODO: make the detailed post view intent
//                Intent i = new Intent(context, DetailActivity.class);
//                i.putExtra("post", Parcels.wrap(post));
//                context.startActivity(i);
                Toast.makeText(context, "Haven't made the detailed view yet", Toast.LENGTH_SHORT).show();
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);

            // do the find view by ids

            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);

            // add this as the item view listener

            itemView.setOnClickListener(this);


        }


    }

}
