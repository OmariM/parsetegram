package me.omarim.parstegram;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;


import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

import me.omarim.parstegram.models.Post;

public class PostAdapter extends PagedListAdapter<Post, PostAdapter.ViewHolder> {

    Context context;

    public PostAdapter(@NonNull DiffUtil.ItemCallback<Post> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

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
        Post post = getItem(i);
        viewHolder.tvBody.setText(post.getDescription());
        try {
            viewHolder.tvHandle.setText(post.getUser().fetchIfNeeded().getString("handle"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Glide.with(context).load(post.getImage().getUrl()).into(viewHolder.ivPostImage);
        String timestamp = getRelativeTimeAgo(post.getCreatedAt());
        viewHolder.tvTimestamp.setText(timestamp);
    }

    //TODO: fix this
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(Date parseDate) {

        String relativeDate;
        long dateMillis = parseDate.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
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
                Post post = getItem(position);
                //TODO: make the detailed post view intent
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("post", post);
                context.startActivity(i);
                Toast.makeText(context, "Haven't made the detailed view yet", Toast.LENGTH_SHORT).show();
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);

            // do the find view by ids

            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvHandle = itemView.findViewById(R.id.etHandle);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);

            // add this as the item view listener

            itemView.setOnClickListener(this);


        }


    }

}
