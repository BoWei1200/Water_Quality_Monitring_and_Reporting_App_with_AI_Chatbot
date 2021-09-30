package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class UserMyReportRecycleVAdapter extends RecyclerView.Adapter<UserMyReportRecycleVAdapter.UserMyReportRecycleViewHolder>{
    Context context;
    String[] myReportIDs;
    String[] myReportDates;
    String[] myReportTimes;
    String[] myReportStatus;

    ArrayList<String> reportIDSelected;

    Boolean selectMode = false;

    public UserMyReportRecycleVAdapter(Context context, String[] myReportIDs, String[] myReportDates, String[] myReportTimes, String[] myReportStatus, ArrayList reportIDSelected) {
        this.context = context;
        this.myReportIDs = myReportIDs;
        this.myReportDates = myReportDates;
        this.myReportTimes = myReportTimes;
        this.myReportStatus = myReportStatus;
        this.reportIDSelected = reportIDSelected;
    }

    @NonNull
    @NotNull

    @Override
    public UserMyReportRecycleVAdapter.UserMyReportRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_my_report_recyclev_layout, parent, false);

        return new UserMyReportRecycleVAdapter.UserMyReportRecycleViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull @NotNull UserMyReportRecycleVAdapter.UserMyReportRecycleViewHolder holder, int position) {
        ConstraintLayout userMyReport_constraintLayout_longPressOperation = (ConstraintLayout) ((UserMyReport)context).findViewById(R.id.userMyReport_constraintLayout_longPressOperation);

        holder.userMyReportRecycleVLayout_txt_reportID.setText(myReportIDs[position]);
        holder.userMyReportRecycleVLayout_txt_reportDate.setText(myReportDates[position]);
        holder.userMyReportRecycleVLayout_txt_reportTime.setText(myReportTimes[position]);
        holder.userMyReportRecycleVLayout_txt_reportStatus.setText(myReportStatus[position]);

        if(myReportStatus[position].equals("Investigating1") || myReportStatus[position].equals("Investigating2"))
            holder.userMyReportRecycleVLayout_txt_reportStatus.setText("Investigating");

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

        holder.userMyReportRecycleVLayout_img_reportListIcon.setImageDrawable(context.getResources().getDrawable(circleImgDrawable));
        holder.userMyReportRecycleVLayout_img_reportListIcon.setBackground(context.getResources().getDrawable(circleImgBackgroundDrawable));
        holder.userMyReportRecycleVLayout_txt_reportStatus.setTextColor(context.getResources().getColor(statusTextColor));
        holder.userMyReportRecycleVLayout_linearLayout_reportStatus.setBackground(context.getResources().getDrawable(statusLinearLayoutBackground));

        holder.userMyReportRecycleVLayout_cv_reportList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(!selectMode){
                    Intent intent = new Intent(context, UserReportStatus.class);
                    intent.putExtra("reportID", myReportIDs[position]);
                    context.startActivity(intent);
                }else{
                    for(int i = 0; i < reportIDSelected.size(); i++){
                        if(myReportIDs[position].equals(reportIDSelected.get(i))){
                            reportIDSelected.remove(reportIDSelected.get(i));
                            holder.userMyReport_img_arrowGoTo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_forward_icon));

                            if(reportIDSelected.size() == 0){
                                selectMode = false;
                                userMyReport_constraintLayout_longPressOperation.setVisibility(View.GONE);
                            }

                            return;
                        }
                    }
                    reportIDSelected.add(myReportIDs[position]);
                    holder.userMyReport_img_arrowGoTo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_icon_red));

                }
            }
        });

        holder.userMyReportRecycleVLayout_cv_reportList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!selectMode){
                    selectMode = true;
                    ImageView userMyReport_img_arrowGoTo = v.findViewById(R.id.userMyReport_img_arrowGoTo);
                    userMyReport_img_arrowGoTo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_icon_red));
                    reportIDSelected.add(myReportIDs[position]);
                    userMyReport_constraintLayout_longPressOperation.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return myReportIDs.length;
    }

    public class UserMyReportRecycleViewHolder extends RecyclerView.ViewHolder {
        CardView userMyReportRecycleVLayout_cv_reportList;

        TextView userMyReportRecycleVLayout_txt_reportID;
        TextView userMyReportRecycleVLayout_txt_reportDate;
        TextView userMyReportRecycleVLayout_txt_reportTime;
        TextView userMyReportRecycleVLayout_txt_reportStatus;

        ImageView userMyReportRecycleVLayout_img_reportListIcon, userMyReport_img_arrowGoTo;

        LinearLayout userMyReportRecycleVLayout_linearLayout_reportStatus;

        public UserMyReportRecycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.userMyReportRecycleVLayout_cv_reportList = itemView.findViewById(R.id.userMyReportRecycleVLayout_cv_reportList);

            this.userMyReportRecycleVLayout_txt_reportID = itemView.findViewById(R.id.userMyReportRecycleVLayout_txt_reportID);
            this.userMyReportRecycleVLayout_txt_reportDate = itemView.findViewById(R.id.userMyReportRecycleVLayout_txt_reportDate);
            this.userMyReportRecycleVLayout_txt_reportTime = itemView.findViewById(R.id.userMyReportRecycleVLayout_txt_reportTime);
            this.userMyReportRecycleVLayout_txt_reportStatus = itemView.findViewById(R.id.userMyReportRecycleVLayout_txt_reportStatus);

            this.userMyReportRecycleVLayout_img_reportListIcon = itemView.findViewById(R.id.userMyReportRecycleVLayout_img_reportListIcon);
            this.userMyReport_img_arrowGoTo = itemView.findViewById(R.id.userMyReport_img_arrowGoTo);

            this.userMyReportRecycleVLayout_linearLayout_reportStatus = itemView.findViewById(R.id.userMyReportRecycleVLayout_linearLayout_reportStatus);
        }
    }
}
