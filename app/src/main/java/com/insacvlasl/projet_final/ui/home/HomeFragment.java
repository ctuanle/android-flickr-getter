package com.insacvlasl.projet_final.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.insacvlasl.projet_final.R;
import com.insacvlasl.projet_final.async_tasks.LoadAPIResponse;
import com.insacvlasl.projet_final.databinding.FragmentHomeBinding;
import com.insacvlasl.projet_final.modeles.DBSQlite;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final SearchView searchView = binding.searchbar;
        final GridLayout gridLayout = binding.gridImages;

        if (savedInstanceState == null) {
            //here we will retreive the recents tags later
            new LoadAPIResponse(gridLayout).execute("cats");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 0) return false;

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

        DBSQlite db = DBSQlite.getInstance(this.getContext());
        System.out.println(db.getAllPostItems());


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}