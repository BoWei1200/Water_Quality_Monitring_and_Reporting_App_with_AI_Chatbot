package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot.ui.userreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot.databinding.FragmentUserReportBinding;

public class UserReportFragment extends Fragment {

    private UserReportViewModel userReportViewModel;
    private FragmentUserReportBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userReportViewModel =
                new ViewModelProvider(this).get(UserReportViewModel.class);

        binding = FragmentUserReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        userReportViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}