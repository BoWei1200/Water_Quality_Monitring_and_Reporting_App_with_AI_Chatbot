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

public class SystemAdminManageOrgRecycleVAdapter extends RecyclerView.Adapter<SystemAdminManageOrgRecycleVAdapter.SystemAdminManageOrgRecycleViewHolder>{
    Context context;
    String[] orgID;
    String[] orgName;

    public SystemAdminManageOrgRecycleVAdapter(Context context, String[] orgID, String[] orgName) {
        this.context = context;
        this.orgID = orgID;
        this.orgName = orgName;
    }

    @NonNull
    @NotNull

    @Override
    public SystemAdminManageOrgRecycleVAdapter.SystemAdminManageOrgRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.system_admin_manage_org_recyclev_layout, parent, false);

        view.findViewById(R.id.systemAdminManageOrgLayout_cv).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                TextView orgID =  v.findViewById(R.id.systemAdminManageOrgLayout_txt_orgID);
                String getOrgID = orgID.getText().toString();

                Intent intent = new Intent(context, OrgDetail.class);
                intent.putExtra("orgID", getOrgID);
                context.startActivity(intent);
                //Toast.makeText(context, getIC,Toast.LENGTH_SHORT).show();
            }
        });

        return new SystemAdminManageOrgRecycleVAdapter.SystemAdminManageOrgRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SystemAdminManageOrgRecycleVAdapter.SystemAdminManageOrgRecycleViewHolder holder, int position) {
        holder.systemAdminManageOrgLayout_txt_orgID.setText(orgID[position]);
        holder.systemAdminManageOrgLayout_txt_orgName.setText(orgName[position]);
    }

    @Override
    public int getItemCount() {
        return orgID.length;
    }

    public class SystemAdminManageOrgRecycleViewHolder extends RecyclerView.ViewHolder {
        TextView systemAdminManageOrgLayout_txt_orgID;
        TextView systemAdminManageOrgLayout_txt_orgName;

        public SystemAdminManageOrgRecycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.systemAdminManageOrgLayout_txt_orgID = itemView.findViewById(R.id.systemAdminManageOrgLayout_txt_orgID);
            this.systemAdminManageOrgLayout_txt_orgName = itemView.findViewById(R.id.systemAdminManageOrgLayout_txt_orgName);
        }
    }
}
