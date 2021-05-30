package com.beslenge.iterio.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.beslenge.iterio.R;

public class NoDataAvailableFragment extends Fragment {
    
    public NoDataAvailableFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_no_data_available, container, false);
    
        LottieAnimationView animationView = root.findViewById(R.id.lottieAnimationView_no_data);
    
        LinearLayout linearLayout = root.findViewById(R.id.linear_layout_nodata_fragment);
    
        animationView.setRepeatCount(0);
        new Handler(Looper.getMainLooper()).postDelayed(animationView::playAnimation, 900);
    
        setObjectAnimator(animationView, 500, 500);
        setObjectAnimator(linearLayout, 1000, 500);
    
        return root;
    }
    
    private void setObjectAnimator(View view, int startDelay, int animationDuration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f);
        animator.setStartDelay(startDelay);
        animator.setDuration(animationDuration);
        animator.start();
    }
    
    
}