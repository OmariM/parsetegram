package me.omarim.parstegram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.omarim.parstegram.models.Post;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.ivDetailPhoto) ImageView ivDetailPhoto;
    @BindView(R.id.tvDetailHandle) TextView tvDetailHandle;
    @BindView(R.id.tvDetailBody) TextView tvDetailBody;
    @BindView(R.id.tvDetailTimestamp) TextView tvDetailTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Post post = (Post) getIntent().getParcelableExtra("post");

        Glide.with(this).load(post.getImage().getUrl()).into(ivDetailPhoto);
        tvDetailBody.setText(post.getDescription());
        tvDetailHandle.setText(post.getUser().get("handle").toString());
        tvDetailTimestamp.setText(post.getCreatedAt().toString());
    }


}
