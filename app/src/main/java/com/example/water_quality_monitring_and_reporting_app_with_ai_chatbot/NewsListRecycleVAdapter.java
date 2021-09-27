package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class NewsListRecycleVAdapter extends RecyclerView.Adapter<NewsListRecycleVAdapter.NewsListRecycleViewHolder>{
    Context context;
    String newsID[];
    String newsImageName[];
    String newsDate[];
    String newsTime[];
    String newsTitle[];

    public NewsListRecycleVAdapter(Context context, String[] newsID, String[] newsImageName, String[] newsDate, String[] newsTime, String[] newsTitle) {
        this.context = context;
        this.newsID = newsID;
        this.newsImageName = newsImageName;
        this.newsDate = newsDate;
        this.newsTime = newsTime;
        this.newsTitle = newsTitle;
    }

    @NonNull
    @NotNull

    @Override
    public NewsListRecycleVAdapter.NewsListRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_recyclev_layout, parent, false);

        view.findViewById(R.id.news_cv_newsList).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                TextView news_txt_newsID =  v.findViewById(R.id.news_txt_newsID);
                String getNewsID = news_txt_newsID.getText().toString();
//
//                Intent intent = new Intent(context, UserDetailInfo.class);
//                intent.putExtra("SelectedNRIC", getIC);
//                context.startActivity(intent);
                Toast.makeText(context, getNewsID,Toast.LENGTH_SHORT).show();
            }
        });

        return new NewsListRecycleVAdapter.NewsListRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewsListRecycleVAdapter.NewsListRecycleViewHolder holder, int position) {
        holder.news_txt_newsID.setText(newsID[position]);
        displayImg(newsImageName[position], holder.news_img_pollutionPhoto);
        holder.news_txt_newsTitle.setText(newsTitle[position]);
        holder.news_txt_dateTime.setText(newsDate[position] + "   " + newsTime[position]);

        if(position == newsID.length-1){
            holder.news_txt_space.setHeight(200);
            holder.news_txt_space.requestLayout();
        }

    }

    public void displayImg(String imgName, ImageView imgView){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reportFromUserImage");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){
                    try{
                        UserReportImage userReportImageRead = ds.getValue(UserReportImage.class);
                        if(userReportImageRead.getName().equals(imgName)){
                            UserReportImage userReportImage = ds.getValue(UserReportImage.class);
                            System.out.println("IMG URL " + userReportImage.getUrl());
                            Uri imageUri = Uri.parse(userReportImage.getUrl());

                            Picasso.get().load(imageUri).fit().centerCrop().into(imgView);
                        }
                    }catch(Exception e){
                        System.out.println("ERROR IN FETCHING: " + e.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return newsID.length;
    }

    public class NewsListRecycleViewHolder extends RecyclerView.ViewHolder {
        TextView news_txt_newsID;
        ImageView news_img_pollutionPhoto;
        TextView news_txt_newsTitle, news_txt_dateTime, news_txt_space;

        public NewsListRecycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.news_txt_newsID = itemView.findViewById(R.id.news_txt_newsID);
            this.news_img_pollutionPhoto = itemView.findViewById(R.id.news_img_pollutionPhoto);
            this.news_txt_newsTitle = itemView.findViewById(R.id.news_txt_newsTitle);
            this.news_txt_dateTime = itemView.findViewById(R.id.news_txt_dateTime);
            this.news_txt_space = itemView.findViewById(R.id.news_txt_space);
        }
    }
}
