package com.insacvlasl.projet_final.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.insacvlasl.projet_final.databinding.FragmentFavoriteBinding;
import com.insacvlasl.projet_final.modeles.CardViewHolder;
import com.insacvlasl.projet_final.modeles.DBSQlite;
import com.insacvlasl.projet_final.modeles.PostItem;

import java.util.List;

public class FavoriteFragment extends Fragment {

    private FavoriteViewModel favoriteViewModel;
    private FragmentFavoriteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoriteViewModel =
                new ViewModelProvider(this).get(FavoriteViewModel.class);

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final GridLayout gridLayout = binding.gridImages;
        List<PostItem> posts = DBSQlite.getInstance(this.getContext()).getAllPostItems();

        if (posts.size() > 0) {
            for (PostItem post: posts) {
                CardViewHolder card = new CardViewHolder(gridLayout, post);
                gridLayout.addView(card.getCardView());
            }
        }

        else {
            final TextView textView = binding.textFavorite;
            favoriteViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    textView.setText(s);
                }
            });
        }


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}