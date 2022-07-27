package com.lolamaglione.meplancapstone.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.lolamaglione.meplancapstone.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeWebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeWebFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RECIPE_URL = "param1";

    private String mRecipeURL;
    private WebView wvURL;
    private Button btnGoToUrl;

    public RecipeWebFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RecipeWebFragment.
     */
    public static RecipeWebFragment newInstance(String param1) {
        RecipeWebFragment fragment = new RecipeWebFragment();
        Bundle args = new Bundle();
        args.putString(RECIPE_URL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeURL = getArguments().getString(RECIPE_URL);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_web, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wvURL = view.findViewById(R.id.wvURL);
        wvURL.getSettings().setJavaScriptEnabled(true);
        wvURL.getSettings().setDomStorageEnabled(true);

        wvURL.setWebViewClient(new WebViewClient());
        wvURL.loadUrl(mRecipeURL);
        btnGoToUrl = view.findViewById(R.id.btnGoToUrl);
        btnGoToUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mRecipeURL));
                startActivity(i);
            }
        });
    }
}