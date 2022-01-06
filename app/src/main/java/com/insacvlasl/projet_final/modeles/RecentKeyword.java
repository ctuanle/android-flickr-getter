package com.insacvlasl.projet_final.modeles;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.gridlayout.widget.GridLayout;

import com.insacvlasl.projet_final.R;
import com.insacvlasl.projet_final.async_tasks.LoadAPIResponse;

public class RecentKeyword {

    final String TAG = this.getClass().getName();

    private TextView textView;
    private String text;

    public RecentKeyword(String text, GridLayout gridKeyword, GridLayout gridImages, DBSQlite db) {
        this.text = text;

        this.textView = (TextView) LayoutInflater.from(gridKeyword.getContext()).inflate(
                R.layout.keyword,
                gridKeyword,
                false
        );
        this.textView.setText(text);
        setOnClickListener(gridImages, db);
    }

    private void setOnClickListener(GridLayout grid, DBSQlite db) {
        this.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid.removeAllViewsInLayout();
                db.insertKeyword(text);
                new LoadAPIResponse(grid).execute(text);
            }
        });
    }

    public TextView getTextView() {
        return textView;
    }
}
