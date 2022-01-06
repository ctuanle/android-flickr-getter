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
    private Boolean isSaved = false;

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
            this.loadImage(detail.getJSONObject("media").getString("m"));
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

        this.loadImage(postItem.media);
        this.loadCardTitle(postItem.title);

        // this.setImageViewOnClickListener();
        // this.setIconOnClickListener();
        this.isSaved = true;
        icon.setImageResource(R.drawable.ic_baseline_favorite_24);
        this.setSavedPostIconOnClickListener();
    }

    private void loadImage(String url) {
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
                System.out.println("ImageView Clicked. Have nothing to do for now");
            }
        });
    }

    private void setIconOnClickListener() {
        this.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PostItem post = new PostItem();
                    post.title = detail.getString("title");
                    post.media = detail.getJSONObject("media").getString("m");

                    if (isSaved) {
                        DBSQlite.getInstance(cardView.getContext()).deletePost(post);
                        isSaved = !isSaved;
                        icon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    } else {
                        DBSQlite.getInstance(cardView.getContext()).insertPost(post);
                        isSaved = !isSaved;
                        icon.setImageResource(R.drawable.ic_baseline_favorite_24);
                    }

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
                PostItem post = new PostItem();
                post.title = postItem.title;
                post.media = postItem.media;
                if (isSaved) {
                    DBSQlite.getInstance(cardView.getContext()).deletePost(post);
                    isSaved = !isSaved;
                    icon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                } else {
                    DBSQlite.getInstance(cardView.getContext()).insertPost(post);
                    isSaved = !isSaved;
                    icon.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            }
        });
    }

    private void setTitleOnClickListener() {
        this.cardTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Title Clicked! Just this log.");
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
