package com.beslenge.iterio.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beslenge.iterio.R;
import com.beslenge.iterio.adapter.FaqRecyclerViewAdapter;

public class FaqFragment extends DialogFragment {
    private final Context context;
    private RecyclerView.LayoutManager layoutManager;
    private FaqRecyclerViewAdapter faqRecyclerViewAdapter;
    
    
    public FaqFragment(Context context) {
        this.context = context;
        
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutManager = new LinearLayoutManager(context);
        faqRecyclerViewAdapter = new FaqRecyclerViewAdapter(context);
        
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(context).inflate(R.layout.faq_listview_layout, container, false);
        Button closeButton = root.findViewById(R.id.faq_button_close);
        RecyclerView recyclerView = root.findViewById(R.id.listView);
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(faqRecyclerViewAdapter);
        
        closeButton.setOnClickListener(v -> {
            this.dismiss();
        });
        
        return root;
    }
    
    @Override
    public int getTheme() {
        return R.style.AppTheme;
    }
}
