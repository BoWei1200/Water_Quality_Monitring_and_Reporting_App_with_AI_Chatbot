package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class UserEmployeeRecycleVAdapter extends RecyclerView.Adapter<UserEmployeeRecycleVAdapter.UserEmployeeRecycleViewHolder>{
    Context context;
    String[] userID;
    String[] userName;

    public UserEmployeeRecycleVAdapter(Context context, String[] userID, String userName[]) {
        this.context = context;
        this.userID = userID;
        this.userName = userName;
    }

    @NonNull
    @NotNull

    @Override
    public UserEmployeeRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_or_employee_recyclev_layout, parent, false);

        return new UserEmployeeRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserEmployeeRecycleVAdapter.UserEmployeeRecycleViewHolder holder, int position) {
        holder.userOrEmployee_txt_userID.setText(userID[position]);
        holder.userOrEmployee_txt_userName.setText(userName[position]);

        holder.userOrEmployee_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserOrEmployeeDetail.class);
                intent.putExtra("userID", userID[position]);
                context. startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userID.length;
    }

    public class UserEmployeeRecycleViewHolder extends RecyclerView.ViewHolder {
        CardView userOrEmployee_cv;
        TextView userOrEmployee_txt_userID, userOrEmployee_txt_userName;

        public UserEmployeeRecycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.userOrEmployee_cv = itemView.findViewById(R.id.userOrEmployee_cv);

            this.userOrEmployee_txt_userID = itemView.findViewById(R.id.userOrEmployee_txt_userID);
            this.userOrEmployee_txt_userName = itemView.findViewById(R.id.userOrEmployee_txt_userName);
        }
    }
}