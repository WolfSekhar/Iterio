package com.beslenge.iterio.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.beslenge.iterio.R;
import com.beslenge.iterio.data.Pref;
import com.beslenge.iterio.viewmodel.AttendanceFragmentViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PercentageSettingFragment extends DialogFragment {
    private final AttendanceFragmentViewModel viewModel;
    private final SharedPreferences.Editor editor;
    private final String[] array;
    private int position = 0;
    
    public PercentageSettingFragment(Context context,
                                     AttendanceFragmentViewModel viewModel,
                                     SharedPreferences.Editor editor,
                                     SharedPreferences sharedPreferences) {
        this.viewModel = viewModel;
        this.editor = editor;
        
        array = context.getResources().getStringArray(R.array.percentage_list);
        List<String> list = new ArrayList<>(Arrays.asList(array));
        position = list.indexOf(
                String.valueOf(sharedPreferences.getString(Pref.minimumAttendance, "25"))
        );
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireActivity());
        
        dialog.setSingleChoiceItems(array, position, (dialog1, which) -> {
            editor.putString(Pref.minimumAttendance, array[which]).commit();
            viewModel.getMinimumAttendance().postValue(array[which]);
            dialog1.dismiss();
        });
        dialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());
        dialog.setTitle(R.string.minAttendanceTitle);
        return dialog.create();
    }
}
