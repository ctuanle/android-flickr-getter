package com.insacvlasl.projet_final.async_tasks;

import android.os.AsyncTask;
import android.util.Log;
import androidx.gridlayout.widget.GridLayout;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.insacvlasl.projet_final.modeles.CardViewHolder;
import com.insacvlasl.projet_final.modeles.ItemImageHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoadAPIResponse extends AsyncTask<String, Void, Void> {

    final String TAG = "LoadAPIResponse";

    private int nCol = 1;

    // Flick public API
    final String API_URL = "https://www.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=";

    private GridLayout gridLayout;

    public LoadAPIResponse(GridLayout gridLayout) {
        this.gridLayout = gridLayout;
    }

    @Override
    protected Void doInBackground(String... tags) {
        String url = API_URL + tags[0];

        RequestQueue queue = Volley.newRequestQueue(this.gridLayout.getContext());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (nCol == 2) {
                            gridLayout.setColumnCount(nCol);
                            gridLayout.setRowCount(10);
                        }
                        try {
                            JSONArray array = (JSONArray) response.get("items");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject img_detail = array.getJSONObject(i);
                                if (nCol == 1) {
                                    CardViewHolder card = new CardViewHolder(gridLayout, img_detail);
                                    gridLayout.addView(card.getCardView());
                                }
                                else if (nCol == 2) {
                                    ItemImageHolder item = new ItemImageHolder(img_detail, gridLayout);
                                    gridLayout.addView(item.getImageView());
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                        error.printStackTrace();
                    }
                });

        queue.add(jsonObjectRequest);
        return null;
    }
}
