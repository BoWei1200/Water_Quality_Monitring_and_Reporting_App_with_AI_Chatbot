package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class EmployeeReportRecycleVAdapter extends RecyclerView.Adapter<EmployeeReportRecycleVAdapter.EmployeeReportRecycleViewHolder>{
    Context context;
    String[] myReportIDs;
    String[] myReportDates;
    String[] myReportTimes;
    String[] myReportStatus;

    public EmployeeReportRecycleVAdapter(Context context, String[] myReportIDs, String[] myReportDates, String[] myReportTimes, String[] myReportStatus) {
        this.context = context;
        this.myReportIDs = myReportIDs;
        this.myReportDates = myReportDates;
        this.myReportTimes = myReportTimes;
        this.myReportStatus = myReportStatus;
    }

    @NonNull
    @NotNull

    @Override
    public EmployeeReportRecycleVAdapter.EmployeeReportRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.employee_report_from_user_recyclev_layout, parent, false);

        view.findViewById(R.id.employeeReportRecycleVLayout_cv_reportList).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                TextView userMyReportRecycleVLayout_txt_reportID =  v.findViewById(R.id.employeeReportRecycleVLayout_txt_reportID);
                String reportID = userMyReportRecycleVLayout_txt_reportID.getText().toString();

                Intent intent = new Intent(context, UserReportStatus.class);
                intent.putExtra("reportID", reportID);
                context.startActivity(intent);
            }
        });

        return new EmployeeReportRecycleVAdapter.EmployeeReportRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EmployeeReportRecycleVAdapter.EmployeeReportRecycleViewHolder holder, int position) {
        holder.employeeReportRecycleVLayout_txt_reportID.setText(myReportIDs[position]);
        holder.employeeReportRecycleVLayout_txt_reportDate.setText(myReportDates[position]);
        holder.employeeReportRecycleVLayout_txt_reportTime.setText(myReportTimes[position]);
        holder.employeeReportRecycleVLayout_txt_reportStatus.setText(myReportStatus[position]);

        if(myReportStatus[position].equals("Investigating1") || myReportStatus[position].equals("Investigating2"))
            holder.employeeReportRecycleVLayout_txt_reportStatus.setText("Investigating");

        int circleImgDrawable = R.drawable.ic_reportpending_icon;
        int circleImgBackgroundDrawable = R.drawable.circle_img_pending;
        int statusTextColor = R.color.circle_img_pending;
        int statusLinearLayoutBackground = R.drawable.rounded_corners_pending;

        if(myReportStatus[position].equals("Pending")){

        }
        else if (myReportStatus[position].equals("Investigating1") || myReportStatus[position].equals("Investigating2")){
            circleImgDrawable = R.drawable.ic_search_icon;
            circleImgBackgroundDrawable = R.drawable.circle_img_investigating;
            statusTextColor = R.color.circle_img_investigating;
            statusLinearLayoutBackground = R.drawable.rounded_corners_investigating;
        }
        else if (myReportStatus[position].equals("Resolving")){
            circleImgDrawable = R.drawable.ic_reportresolving_icon;
            circleImgBackgroundDrawable = R.drawable.circle_img_resolving;
            statusTextColor = R.color.circle_img_resolving;
            statusLinearLayoutBackground = R.drawable.rounded_corners_resolving;
        }
        else if (myReportStatus[position].equals("Examining")){
            circleImgDrawable = R.drawable.ic_reportexamining_icon;
            circleImgBackgroundDrawable = R.drawable.circle_img_examining;
            statusTextColor = R.color.white;
            statusLinearLayoutBackground = R.drawable.rounded_corners_examining;
        }
        else if (myReportStatus[position].equals("Resolved")){
            circleImgDrawable = R.drawable.ic_reportresolved_icon;
            circleImgBackgroundDrawable = R.drawable.circle_img_resolved;
            statusTextColor = R.color.circle_img_resolved;
            statusLinearLayoutBackground = R.drawable.rounded_corners_resolved;
        }
        else {
            circleImgDrawable = R.drawable.ic_reportrejected_icon;
            circleImgBackgroundDrawable = R.drawable.circle_img_rejected;
            statusTextColor = R.color.circle_img_rejected;
            statusLinearLayoutBackground = R.drawable.rounded_corners_rejected;
        }

        holder.employeeReportRecycleVLayout_img_reportListIcon.setImageDrawable(context.getResources().getDrawable(circleImgDrawable));
        holder.employeeReportRecycleVLayout_img_reportListIcon.setBackground(context.getResources().getDrawable(circleImgBackgroundDrawable));
        holder.employeeReportRecycleVLayout_txt_reportStatus.setTextColor(context.getResources().getColor(statusTextColor));
        holder.employeeReportRecycleVLayout_linearLayout_reportStatus.setBackground(context.getResources().getDrawable(statusLinearLayoutBackground));
    }

    @Override
    public int getItemCount() {
        return myReportIDs.length;
    }

    public class EmployeeReportRecycleViewHolder extends RecyclerView.ViewHolder {
        TextView employeeReportRecycleVLayout_txt_reportID;
        TextView employeeReportRecycleVLayout_txt_reportDate;
        TextView employeeReportRecycleVLayout_txt_reportTime;
        TextView employeeReportRecycleVLayout_txt_reportStatus;

        ImageView employeeReportRecycleVLayout_img_reportListIcon;

        LinearLayout employeeReportRecycleVLayout_linearLayout_reportStatus;

        public EmployeeReportRecycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.employeeReportRecycleVLayout_txt_reportID = itemView.findViewById(R.id.employeeReportRecycleVLayout_txt_reportID);
            this.employeeReportRecycleVLayout_txt_reportDate = itemView.findViewById(R.id.employeeReportRecycleVLayout_txt_reportDate);
            this.employeeReportRecycleVLayout_txt_reportTime = itemView.findViewById(R.id.employeeReportRecycleVLayout_txt_reportTime);
            this.employeeReportRecycleVLayout_txt_reportStatus = itemView.findViewById(R.id.employeeReportRecycleVLayout_txt_reportStatus);

            this.employeeReportRecycleVLayout_img_reportListIcon = itemView.findViewById(R.id.employeeReportRecycleVLayout_img_reportListIcon);

            this.employeeReportRecycleVLayout_linearLayout_reportStatus = itemView.findViewById(R.id.employeeReportRecycleVLayout_linearLayout_reportStatus);
        }
    }
}
