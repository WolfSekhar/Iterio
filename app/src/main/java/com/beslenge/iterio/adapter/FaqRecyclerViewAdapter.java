package com.beslenge.iterio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beslenge.iterio.R;

import java.util.Arrays;
import java.util.List;

public class FaqRecyclerViewAdapter extends RecyclerView.Adapter<FaqRecyclerViewAdapter.MyViewHolder> {
    List<String> listQuestions;
    List<String> listAnswers;
    Context context;
    
    public FaqRecyclerViewAdapter(Context context) {
        this.context = context;
        listQuestions = Arrays.asList(context.getResources().getStringArray(R.array.faq_questions));
        listAnswers = Arrays.asList(context.getResources().getStringArray(R.array.faq_answers));
    }
    
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.faq_items_layout, parent, false);
        return new MyViewHolder(root);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.getTextViewQuestions().setText(listQuestions.get(position));
        holder.getTextViewAnswers().setText(listAnswers.get(position));
    }
    
    @Override
    public int getItemCount() {
        return listAnswers.size();
    }
    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewQuestions;
        private final TextView textViewAnswers;
        
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestions = itemView.findViewById(R.id.textView_que);
            textViewAnswers = itemView.findViewById(R.id.textView_ans);
            
        }
        
        private TextView getTextViewQuestions() {
            return textViewQuestions;
        }
        
        private TextView getTextViewAnswers() {
            return textViewAnswers;
        }
    }
}
