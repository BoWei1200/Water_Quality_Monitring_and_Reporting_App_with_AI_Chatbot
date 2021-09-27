package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageGalleryRecycleVAdapter extends RecyclerView.Adapter<ImageGalleryRecycleVAdapter.ImageGalleryRecycleViewHolder>{
    Context context;
    String[] reportImgName;
    ArrayList<String> reportImageSelected;
    Boolean selectMode = false;

    public ImageGalleryRecycleVAdapter(Context context, String[] names, ArrayList reportImageSelected) {
        this.context = context;
        this.reportImgName = names;
        this.reportImageSelected = reportImageSelected;
    }

    @NonNull
    @NotNull

    @Override
    public ImageGalleryRecycleVAdapter.ImageGalleryRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.img_gallery_layout, parent, false);

        //view. set on click

        return new ImageGalleryRecycleVAdapter.ImageGalleryRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageGalleryRecycleVAdapter.ImageGalleryRecycleViewHolder holder, int position) {
        if(position % 2 == 0){
            displayImage(reportImgName[position], holder.imgGalleryLayout_img_photoLeft);
            try{
                displayImage(reportImgName[position+1], holder.imgGalleryLayout_img_photoRight);
            }catch (Exception e){
                System.out.println("ERROR IN img layout : " + e.toString() + " position " + position );
            }
        }
        else{
            holder.imgGalleryLayout_linear.setVisibility(View.GONE);
        }
    }

    public void displayImage(String imgName, ImageView imgView){
        TextView txtErrorImg = (TextView) ((NewsAdd)context).findViewById(R.id.newsAdd_txt_errorImage);

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

                            imgView.setOnClickListener(v -> {
                                if(!selectMode){
                                    Intent intent = new Intent(context, ReportPhotoViewer.class);
                                    intent.putExtra("imageToDisplay", imageUri.toString());
                                    intent.putExtra("passedActivity", "fromWeb");
                                    context.startActivity(intent);
                                }else{
                                   for(int i = 0; i < reportImageSelected.size(); i++){
                                       if(imgName.equals(reportImageSelected.get(i))){
                                           reportImageSelected.remove(reportImageSelected.get(i));
                                           Picasso.get().load(imageUri).fit().centerCrop().into(imgView);
                                           imgView.setPadding(0, 0, 0, 0);

                                           if(reportImageSelected.size() == 0){
                                               selectMode = false;
                                               txtErrorImg.setVisibility(View.VISIBLE);
                                           }


                                           return;
                                       }

                                   }
                                   reportImageSelected.add(imgName);
                                    imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.check_icon));
                                    imgView.setPadding(100, 100, 100, 100);
                                    txtErrorImg.setVisibility(View.GONE);
                                    String imgSelected = "";
                                   for(int i = 0; i < reportImageSelected.size(); i++){
                                       imgSelected += reportImageSelected.get(i) + ", ";
                                   }
                                    System.out.println("IMG selected: " + imgSelected);
                                }
                            });

                            imgView.setOnLongClickListener(v -> {
                                selectMode = true;
                                reportImageSelected.add(imgName);
                                txtErrorImg.setVisibility(View.GONE);
                                imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.check_icon));
                                imgView.setPadding(80, 80, 80, 80);
                                return true;
                            });
                            break;
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
        return reportImgName.length;
    }

    public class ImageGalleryRecycleViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGalleryLayout_img_photoLeft, imgGalleryLayout_img_photoRight;
        LinearLayout imgGalleryLayout_linear;

        public ImageGalleryRecycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.imgGalleryLayout_img_photoLeft = itemView.findViewById(R.id.imgGalleryLayout_img_photoLeft);
            this.imgGalleryLayout_img_photoRight = itemView.findViewById(R.id.imgGalleryLayout_img_photoRight);
            this.imgGalleryLayout_linear = itemView.findViewById(R.id.imgGalleryLayout_linear);
        }
    }
}
