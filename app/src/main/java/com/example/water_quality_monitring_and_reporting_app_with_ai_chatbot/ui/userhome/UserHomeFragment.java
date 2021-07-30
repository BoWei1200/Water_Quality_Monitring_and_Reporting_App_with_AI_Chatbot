package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot.ui.userhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot.R;
import com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot.UserReportMenu;
import com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot.databinding.FragmentUserHomeBinding;

public class UserHomeFragment extends Fragment {

    private UserHomeViewModel userHomeViewModel;
    private FragmentUserHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userHomeViewModel =
                new ViewModelProvider(this).get(UserHomeViewModel.class);

        binding = FragmentUserHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        userHomeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.userHome_btn_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), UserReportMenu.class);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}