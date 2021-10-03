package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class AdminManageInvestigationTeamRecycleVAdapter extends RecyclerView.Adapter<AdminManageInvestigationTeamRecycleVAdapter.AdminManageInvestigationTeamRecycleViewHolder>{
    Context context;

    String[] inTeamID;
    String[] inTeamName;

    public AdminManageInvestigationTeamRecycleVAdapter(Context context, String[] inTeamID, String[] inTeamName) {
        this.context = context;
        this.inTeamID = inTeamID;
        this.inTeamName = inTeamName;
    }

    @NonNull
    @NotNull

    @Override
    public AdminManageInvestigationTeamRecycleVAdapter.AdminManageInvestigationTeamRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_or_employee_recyclev_layout, parent, false);

        view.findViewById(R.id.userOrEmployee_cv).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                TextView teamID =  v.findViewById(R.id.userOrEmployee_txt_userID);
                TextView teamName =  v.findViewById(R.id.userOrEmployee_txt_userName);

                String getTeamID = teamID.getText().toString();
                String getTeamName = teamName.getText().toString();

                Intent intent = new Intent(context, InvestigatorTeamDetail.class);
                intent.putExtra("teamID", getTeamID);
                intent.putExtra("teamName", getTeamName);

                context.startActivity(intent);
            }
        });

        return new AdminManageInvestigationTeamRecycleVAdapter.AdminManageInvestigationTeamRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdminManageInvestigationTeamRecycleVAdapter.AdminManageInvestigationTeamRecycleViewHolder holder, int position) {
        holder.userOrEmployee_txt_inTeamID.setText(inTeamID[position]);
        holder.userOrEmployee_txt_inTeamName.setText(inTeamName[position]);
    }

    @Override
    public int getItemCount() {
        return inTeamID.length;
    }

    public class AdminManageInvestigationTeamRecycleViewHolder extends RecyclerView.ViewHolder {
        TextView userOrEmployee_txt_inTeamID;
        TextView userOrEmployee_txt_inTeamName;

        public AdminManageInvestigationTeamRecycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.userOrEmployee_txt_inTeamID = itemView.findViewById(R.id.userOrEmployee_txt_userID);
            this.userOrEmployee_txt_inTeamName = itemView.findViewById(R.id.userOrEmployee_txt_userName);
        }
    }
}
