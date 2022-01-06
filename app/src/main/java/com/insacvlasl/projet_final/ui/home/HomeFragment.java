package com.insacvlasl.projet_final.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.insacvlasl.projet_final.R;
import com.insacvlasl.projet_final.async_tasks.LoadAPIResponse;
import com.insacvlasl.projet_final.databinding.FragmentHomeBinding;
import com.insacvlasl.projet_final.modeles.DBSQlite;
import com.insacvlasl.projet_final.modeles.RecentKeyword;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String KEY_WORD;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final SearchView searchView = binding.searchbar;
        final GridLayout gridLayout = binding.gridImages;
        final GridLayout gridKeyword = binding.gridKeywords;

        /*if (savedInstanceState == null) {
            new LoadAPIResponse(gridLayout).execute("cats");
        }
        else {
            new LoadAPIResponse(gridLayout).execute(savedInstanceState.getString(KEY_WORD));
        }*/

        DBSQlite db = DBSQlite.getInstance(this.getContext());
        new LoadAPIResponse(gridLayout).execute(db.getLastKeyword());

        loadRecentKeyword(gridKeyword, gridLayout, db);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 0) return false;
                KEY_WORD = query;
                db.insertKeyword(query);
                String[] arr = query.split(" ");
                if (arr.length > 1) {
                    query = "";
                    for (String str : arr) {
                        query = query + str + ",";
                    }
                }
                gridLayout.removeAllViewsInLayout();
                new LoadAPIResponse(gridLayout).execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_WORD, KEY_WORD);
        super.onSaveInstanceState(outState);
    }

    public void loadRecentKeyword(GridLayout gridKeyword, GridLayout gridImages, DBSQlite db) {
        List<String> list = db.get10LastKeywords();
        for (String keyword: list) {
            RecentKeyword key = new RecentKeyword(keyword, gridKeyword, gridImages, db);
            gridKeyword.addView(key.getTextView());
        }
    }
}