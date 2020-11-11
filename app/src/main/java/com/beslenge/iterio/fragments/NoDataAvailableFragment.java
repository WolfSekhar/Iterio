package com.beslenge.iterio.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beslenge.iterio.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoDataAvailableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoDataAvailableFragment extends Fragment {

    public NoDataAvailableFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NoDataAvailableFragment newInstance() {
        return new NoDataAvailableFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_data_available, container, false);
    }
}