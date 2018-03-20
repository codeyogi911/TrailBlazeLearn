package edu.nus.trailblazelearn.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.helper.RetriveImageBitmap;
import edu.nus.trailblazelearn.model.Post;
import edu.nus.trailblazelearn.utility.DateUtil;

/**
 * Created by e0267645 on 17/3/2018.
 */

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.ViewHolder>{

    /**
     * A list of posts
     */
    private List<Post> listOfPosts;
    /**
     * The current context
     */
    private Context postContext;

    private String username;

    public PostsListAdapter(List<Post> listOfPosts, Context mContext, String username) {
        this.listOfPosts = listOfPosts;
        this.postContext = mContext;
        this.username = username;
    }


    @Override
    public PostsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view.
        View viewObj = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);

        PostsListAdapter.ViewHolder viewHolderObj = new PostsListAdapter.ViewHolder(viewObj);
        return viewHolderObj;
    }


    @Override
    public void onBindViewHolder(PostsListAdapter.ViewHolder holder, int position) {

        holder.mUserName.setText(listOfPosts.get(position).getUserName());
        if(listOfPosts.get(position).getMessage() != null)
            holder.postImage.setVisibility(View.GONE);
            holder.mMessageText.setVisibility(View.VISIBLE);
            holder.mMessageText.setText(listOfPosts.get(position).getMessage());
        if(listOfPosts.get(position).getImageUri() != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            holder.mMessageText.setVisibility(View.GONE);
            RetriveImageBitmap retriveImageBitmap = new RetriveImageBitmap(postContext, holder.postImage);
            retriveImageBitmap.execute(listOfPosts.get(position).getImageUri());
        }
            //holder.postImage.setImageURI(Uri.parse(listOfPosts.get(position).getImageUri()));
        String createDateObj = DateUtil.constructDateToStringDate(listOfPosts.get(position).getCreatedDate());
        holder.mCreatedDate.setText(createDateObj);

    }

    @Override
    public int getItemCount() {
        return listOfPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mUserName;
        public TextView mMessageText;
        public TextView mCreatedDate;
        public ImageView postImage;


        public ViewHolder(View v) {
            super(v);
            mUserName = v.findViewById(R.id.text_message_username);
            mMessageText = v.findViewById(R.id.text_message_body);
            mCreatedDate = v.findViewById(R.id.text_message_time);
            postImage = v.findViewById(R.id.post_image);
        }


    }
}






