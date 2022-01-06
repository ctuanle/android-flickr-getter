package com.insacvlasl.projet_final.modeles;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.gridlayout.widget.GridLayout;

import com.bumptech.glide.Glide;
import com.insacvlasl.projet_final.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemImageHolder {

    final String TAG = this.getClass().getName();

    private ImageView imageView;

    private PostItem postItem;
    private JSONObject detail;

    public ItemImageHolder(JSONObject detail, GridLayout gridLayout) {
        this.detail = detail;
        this.imageView = (ImageView) LayoutInflater.from(gridLayout.getContext()).inflate(
                R.layout.image_item_container,
                gridLayout,
                false
        );

        try {
            this.loadImage(gridLayout, detail.getJSONObject("media").getString("m"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.setImageViewOnClickListener();
    }

    public ItemImageHolder(PostItem postItem, GridLayout gridLayout) {
        this.postItem = postItem;
        this.imageView = (ImageView) LayoutInflater.from(gridLayout.getContext()).inflate(
                R.layout.image_item_container,
                gridLayout,
                false
        );

        this.loadImage(gridLayout, postItem.media);
        this.setImageViewOnClickListener();
    }

    public void loadImage(GridLayout grid, String url) {
        int size = (grid.getWidth() - 30) / 2 ;

        Glide.with(grid.getContext())
                .load(url)
                .override(size,size)
                .centerCrop()
                .into(this.imageView);
    }

    private void setImageViewOnClickListener() {
        this.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ImageView Clicked");
            }
        });
    }

    public ImageView getImageView() {
        return imageView;
    }
}
