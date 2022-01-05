package com.insacvlasl.projet_final.modeles;

import android.view.LayoutInflater;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.insacvlasl.projet_final.R;
import com.insacvlasl.projet_final.async_tasks.LoadImageFromUrl;

import org.json.JSONException;
import org.json.JSONObject;

public class CardViewHolder {

    final String TAG = this.getClass().getName();

    private JSONObject detail;
    private CardView cardView;
    private ImageView imageView;
    private TextView cardTitle;
    private ImageView icon;
    private PostItem postItem;

    public CardViewHolder(GridLayout grid, JSONObject detail) {
        // Build the card
        this.detail = detail;
        this.cardView = (CardView) LayoutInflater.from(grid.getContext()).inflate(
                R.layout.card_container,
                grid,
                false
        );
        this.imageView = this.cardView.findViewById(R.id.card_image);
        this.cardTitle = this.cardView.findViewById(R.id.card_image_title);
        this.icon = this.cardView.findViewById(R.id.card_icon);

        // Load its contents
        try {
            this.loadImage(grid, detail.getJSONObject("media").getString("m"));
            this.loadCardTitle(detail.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.setImageViewOnClickListener();
        this.setIconOnClickListener();
        this.setTitleOnClickListener();
    }


    // For the case load favorites page
    public CardViewHolder(GridLayout grid, PostItem postItem) {
        this.cardView = (CardView) LayoutInflater.from(grid.getContext()).inflate(
                R.layout.card_container,
                grid,
                false
        );
        this.imageView = this.cardView.findViewById(R.id.card_image);
        this.cardTitle = this.cardView.findViewById(R.id.card_image_title);
        this.icon = this.cardView.findViewById(R.id.card_icon);
        this.postItem = postItem;

        this.loadImage(grid, postItem.media);
        this.loadCardTitle(postItem.title);

        /*this.setImageViewOnClickListener();
        this.setIconOnClickListener();*/
        this.setSavedPostIconOnClickListener();
    }

    private void loadImage(GridLayout grid, String url) {
        int size = (int) (grid.getWidth() * 0.45);

        /*Glide.with(grid.getContext())
                .load(url)
                .into(this.imageView);*/
        new LoadImageFromUrl(this.imageView).execute(url);

    }

    private void loadCardTitle(String title) {
        if (title.length() == 0) {
            title = "No title";
        }
        else if (title.length() > 25) {
            title = title.substring(0, 24) + "...";
        }
        this.cardTitle.setText(title);
    }

    private void setImageViewOnClickListener() {
        this.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ImageView Clicked");
            }
        });
    }

    private void setIconOnClickListener() {
        this.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Icon Clicked");
                try {
                    PostItem post = new PostItem();
                    post.title = detail.getString("title");
                    post.media = detail.getJSONObject("media").getString("m");

                    DBSQlite.getInstance(cardView.getContext()).insertPost(post);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setSavedPostIconOnClickListener() {
        this.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("SavedPostIcon Clicked");
                PostItem post = new PostItem();
                post.title = postItem.title;
                post.media = postItem.media;
                DBSQlite.getInstance(cardView.getContext()).deletePost(post);
            }
        });
    }

    private void setTitleOnClickListener() {
        this.cardTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Title Clicked");
            }
        });
    }

    public JSONObject getDetail() {
        return detail;
    }

    public CardView getCardView() {
        return cardView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public ImageView getIcon() {
        return icon;
    }
}
