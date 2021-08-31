package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class InvestigationTeamDetailMemberRecycleVAdapter extends RecyclerView.Adapter<InvestigationTeamDetailMemberRecycleVAdapter.InvestigationTeamDetailMemberRecycleViewHolder>{
    Context context;
    String[] teamMemNames;

    public InvestigationTeamDetailMemberRecycleVAdapter(Context context, String[] names) {
        this.context = context;
        this.teamMemNames = names;
    }

    @NonNull
    @NotNull

    @Override
    public InvestigationTeamDetailMemberRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.investigation_team_detail_member_recyclev_layout, parent, false);

        view.findViewById(R.id.teamMemDetailRecycleVLayout_cv_teamName).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
//                TextView NRIC =  v.findViewById(R.id.txt_adminVIewUsersLayout_ICs);
//                String getIC = NRIC.getText().toString();
//
//                Intent intent = new Intent(context, UserDetailInfo.class);
//                intent.putExtra("SelectedNRIC", getIC);
//                context.startActivity(intent);
                //Toast.makeText(context, getIC,Toast.LENGTH_SHORT).show();
            }
        });

        return new InvestigationTeamDetailMemberRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InvestigationTeamDetailMemberRecycleVAdapter.InvestigationTeamDetailMemberRecycleViewHolder holder, int position) {
        holder.teamMemDetailRecycleVLayout_txt_teamMemName.setText(teamMemNames[position]);
    }

    @Override
    public int getItemCount() {
        return teamMemNames.length;
    }

    public class InvestigationTeamDetailMemberRecycleViewHolder extends RecyclerView.ViewHolder {
        TextView teamMemDetailRecycleVLayout_txt_teamMemName;

        public InvestigationTeamDetailMemberRecycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.teamMemDetailRecycleVLayout_txt_teamMemName = itemView.findViewById(R.id.teamMemDetailRecycleVLayout_txt_teamMemName);
        }
    }
}
