package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAIChattingMessageRVAdapter extends RecyclerView.Adapter {

    private ArrayList<UserAIChattingMessageModal> userAIChattingMessageModalArrayList;
    private Context context;

    public UserAIChattingMessageRVAdapter(ArrayList<UserAIChattingMessageModal> userAIChattingMessageModalArrayList, Context context) {
        this.userAIChattingMessageModalArrayList = userAIChattingMessageModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ai_chatting_user_msg, parent, false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ai_chatting_bot_msg, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserAIChattingMessageModal modal = userAIChattingMessageModalArrayList.get(position);
        switch (modal.getSender()) {
            case "user":
                ((UserViewHolder) holder).userTV.setText(modal.getMessage());
                break;
            case "bot":
                ((BotViewHolder) holder).botTV.setText(modal.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return userAIChattingMessageModalArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (userAIChattingMessageModalArrayList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userTV;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTV = itemView.findViewById(R.id.idTVUser);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botTV;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botTV = itemView.findViewById(R.id.idTVBot);
        }
    }
}
