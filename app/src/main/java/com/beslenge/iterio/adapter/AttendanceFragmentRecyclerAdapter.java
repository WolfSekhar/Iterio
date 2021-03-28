package com.beslenge.iterio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beslenge.iterio.R;
import com.beslenge.iterio.model.AttendanceData;
import com.beslenge.iterio.model.BunkCalculator;

public class AttendanceFragmentRecyclerAdapter extends RecyclerView.Adapter<AttendanceFragmentRecyclerAdapter.ViewHolder> {
    private final Context context;
    private AttendanceData data;
    private String minAttendance;

    public AttendanceFragmentRecyclerAdapter(Context context, @NonNull String data, String minattendance) {
        this.context = context;
        this.data = new AttendanceData(data);
        this.minAttendance = minattendance;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendance_fragment_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        double percentage = data.getAttendancePercentage().get(position);
        String subjectName = data.getSubjects().get(position);
        String subjectType = data.getTypeOfClass().get(position);
        int attendedClasses = data.getAttendedClasses().get(position);
        int totalClasses = data.getTotalClasses().get(position);

        /*-----------------------------------------------------------------------------------------------------------------------------------------------*/
        holder.textView_percentage_before_decimal.setText(percentageBeforeDecimal(percentage));
        if (percentage==100){
            holder.getTextView_percentage_after_decimal.setVisibility(View.GONE);
        }else {
            holder.getTextView_percentage_after_decimal.setText(".".concat(percentageAfterDecimal(percentage)));
        }

        holder.textView_subject.setText(subjectName);
        /*-----------------------------------------------------------------------------------------------------------------------------------------------*/
        holder.textView_subject_type.setText(subjectType.concat("   "));
        holder.textView_classes_attended.setText(String.valueOf(attendedClasses).concat("/").concat(String.valueOf(totalClasses)).concat("   "));
        /*-----------------------------------------------------------------------------------------------------------------------------------------------*/

        holder.textView_needtoAttend1.setVisibility(View.VISIBLE);
        holder.textView_needtoAttend2.setVisibility(View.VISIBLE);
        holder.textView_needtoBunk1.setVisibility(View.VISIBLE);
        holder.textView_needtoBunk2.setVisibility(View.VISIBLE);
        holder.textView_needtoBunk3.setVisibility(View.VISIBLE);

        /*-----------------------------------------------------------------------------------------------------------------------------------------------*/
        if (percentage < 35.0) {
            holder.side_bar.setBackgroundColor(context.getResources().getColor(R.color.google_red));
        } else if (percentage < getMinimum(minAttendance)) {
            holder.side_bar.setBackgroundColor(context.getResources().getColor(R.color.google_yellow));
        } else {
            holder.side_bar.setBackgroundColor(context.getResources().getColor(R.color.google_blue));
        }

        /*-----------------------------------------------------------------------------------------------------------------------------------------------*/
        if (data.getAttendancePercentage().get(position) > getMinimum(minAttendance)) {
            holder.textView_needtoAttend1.setVisibility(View.GONE);
            holder.textView_needtoAttend2.setVisibility(View.GONE);
            holder.textView_needtoBunk1.setText(context.getString(R.string.bunk).concat(classesToBunk1(position)).concat(" more classes for ").concat(String.valueOf((int) getMinimum(minAttendance))).concat("%"));
            holder.textView_needtoBunk2.setText(context.getString(R.string.bunk).concat(classesToBunk2(position)).concat(" more classes for ").concat(String.valueOf((int) getMinimum(minAttendance) - 5)).concat("%"));
            holder.textView_needtoBunk3.setText(context.getString(R.string.bunk).concat(classesToBunk3(position)).concat(" more classes for ").concat(String.valueOf((int) getMinimum(minAttendance) - 10)).concat("%"));

        } else {
            holder.textView_needtoBunk1.setVisibility(View.GONE);
            holder.textView_needtoBunk2.setVisibility(View.GONE);
            holder.textView_needtoBunk3.setVisibility(View.GONE);
            holder.textView_needtoAttend1.setText(context.getString(R.string.need).concat(classesToAttend1(position)).concat(" more classes for ").concat(minAttendance).concat("%"));
            holder.textView_needtoAttend2.setText(context.getString(R.string.need).concat(classesToAttend2(position)).concat(" more classes for ").concat(String.valueOf((int) getMinimum(minAttendance) - 5)).concat("%"));

        }

        /*-----------------------------------------------------------------------------------------------------------------------------------------------*/
        holder.textView_classes_updatedON.setText(context.getString(R.string.updated_date).concat(data.getUpdatedOn().get(position)));
    }

    private double getMinimum(@NonNull String minAttendance) {
        switch (minAttendance) {
            case "35":
                return 35.0;
            case "50":
                return 50.0;
            case "60":
                return 60.0;
            case "75":
                return 75.0;
            case "85":
                return 85.0;
            case "95":
                return 95.0;
            default:
                return 25.0;
        }
    }

    @Override
    public int getItemCount() {
        return data.getSubjects().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView_percentage_before_decimal, getTextView_percentage_after_decimal, textView_subject,
                textView_classes_attended, textView_needtoAttend1, textView_needtoAttend2, textView_needtoBunk1, textView_needtoBunk2, textView_needtoBunk3,
                textView_subject_type, textView_classes_updatedON;
        private final View side_bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_percentage_before_decimal = itemView.findViewById(R.id.textview_percentage_before_decimal);
            getTextView_percentage_after_decimal = itemView.findViewById(R.id.textview_percentage_after_decimal);
            textView_subject = itemView.findViewById(R.id.textView_for_subject);
            textView_classes_attended = itemView.findViewById(R.id.textView_class_attended);
            textView_needtoAttend1 = itemView.findViewById(R.id.textView_classes_need_to_attend1);
            textView_needtoAttend2 = itemView.findViewById(R.id.textView_classes_need_to_attend2);
            textView_needtoBunk1 = itemView.findViewById(R.id.textView_classes_need_to_bunk1);
            textView_needtoBunk2 = itemView.findViewById(R.id.textView_classes_need_to_bunk2);
            textView_needtoBunk3 = itemView.findViewById(R.id.textView_classes_need_to_bunk3);
            textView_subject_type = itemView.findViewById(R.id.textview_subject_type);
            textView_classes_updatedON = itemView.findViewById(R.id.textView_classes_updated);
            side_bar = itemView.findViewById(R.id.attendance_item_side_bar_view);
        }
    }

    public void updateAdapter(@NonNull AttendanceData data, @NonNull String minAttendance) {
        this.data = data;
        this.minAttendance = minAttendance;
        notifyDataSetChanged();
    }

    @NonNull
    private String classesToAttend1(int position) {
        return String.valueOf(
                new BunkCalculator().classesRequiredToAttend(
                        data.getAttendedClasses().get(position),
                        data.getTotalClasses().get(position),
                        data.getAttendancePercentage().get(position),
                        getMinimum(minAttendance))
        );
    }

    @NonNull
    private String classesToAttend2(int position) {
        return String.valueOf(
                new BunkCalculator().classesRequiredToAttend(
                        data.getAttendedClasses().get(position),
                        data.getTotalClasses().get(position),
                        data.getAttendancePercentage().get(position),
                        getMinimum(minAttendance) - 5.0)
        );
    }

    @NonNull
    private String classesToBunk1(int position) {
        return String.valueOf(
                new BunkCalculator().classesRequiredToBunk(
                        data.getAttendedClasses().get(position),
                        data.getTotalClasses().get(position),
                        data.getAttendancePercentage().get(position),
                        getMinimum(minAttendance))
        );
    }

    @NonNull
    private String classesToBunk2(int position) {
        return String.valueOf(
                new BunkCalculator().classesRequiredToBunk(
                        data.getAttendedClasses().get(position),
                        data.getTotalClasses().get(position),
                        data.getAttendancePercentage().get(position),
                        getMinimum(minAttendance) - 5.0)
        );
    }

    @NonNull
    private String classesToBunk3(int position) {
        return String.valueOf(
                new BunkCalculator().classesRequiredToBunk(
                        data.getAttendedClasses().get(position),
                        data.getTotalClasses().get(position),
                        data.getAttendancePercentage().get(position),
                        getMinimum(minAttendance) - 10.0)
        );
    }

    @NonNull
    private String percentageBeforeDecimal(double percentage) {
        return String.valueOf((int) (percentage));
    }

    @NonNull
    private String percentageAfterDecimal(double percentage) {
        return String.valueOf(Math.round((percentage % 1) * 100));
    }

}
