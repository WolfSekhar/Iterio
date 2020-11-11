package com.beslenge.iterio.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beslenge.iterio.viewmodel.AttendanceFragmentViewModel;
import com.beslenge.iterio.model.AttendanceData;
import com.beslenge.iterio.data.Pref;
import com.beslenge.iterio.R;
import com.beslenge.iterio.adapter.AttendanceFragmentRecyclerAdapter;

import java.util.Objects;
/*
 * Fragment class for showing attendance data
 * */

public class AttendanceFragment extends Fragment {
    private AttendanceFragmentViewModel attendanceviewModel;
    private RecyclerView.LayoutManager layoutManager;
    private AttendanceFragmentRecyclerAdapter adapter;
    private String data;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String minimum;
    // --Commented out by Inspection (30/8/20 5:17 PM):private final String TAG = "BOB";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareSharedPreferences();

        data = sharedPreferences.getString(Pref.attendanceData, "{}");
        minimum = sharedPreferences.getString(Pref.minimumAttendance, "25");

        instantiateViewModels();
        adapter = new AttendanceFragmentRecyclerAdapter(requireContext(), Objects.requireNonNull(data), minimum);
        layoutManager = new LinearLayoutManager(requireContext());
        //updateAttendanceView();
        updateAttendanceView();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance_fragment_layout, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void updateAttendanceView() {
        assert data != null;
        assert minimum != null;
        assert adapter != null;
        attendanceviewModel.getAttendanceData().observe(AttendanceFragment.this, s -> {

            adapter.updateAdapter(new AttendanceData(s), minimum);

        });

        attendanceviewModel.getMinimumAttendance().observe(AttendanceFragment.this, s -> {
            data = sharedPreferences.getString(Pref.attendanceData, "NA");
            minimum = sharedPreferences.getString(Pref.minimumAttendance, "25");

            adapter.updateAdapter(new AttendanceData(data), minimum);
        });
    }

    private void prepareSharedPreferences() {
        sharedPreferences = requireActivity().getSharedPreferences(Pref.sharedPrefName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    private void instantiateViewModels() {
        attendanceviewModel = new ViewModelProvider(requireActivity()).get(AttendanceFragmentViewModel.class);
    }
}
