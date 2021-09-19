package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserAIChatting extends AppCompatActivity {

    private RecyclerView userAIChatting_recyclerV_chat;
    private ImageButton userAIChatting_imgBtn_send;
    private EditText userAIChatting_eTxt_message;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";

    private RequestQueue mRequestQueue;

    private ArrayList<UserAIChattingMessageModal> userAIChattingMessageModalArrayList;
    private UserAIChattingMessageRVAdapter userAIChattingMessageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_aichatting);

        userAIChatting_recyclerV_chat = findViewById(R.id.userAIChatting_recyclerV_chat);
        userAIChatting_imgBtn_send = findViewById(R.id.userAIChatting_imgBtn_send);
        userAIChatting_eTxt_message = findViewById(R.id.userAIChatting_eTxt_message);

        mRequestQueue = Volley.newRequestQueue(UserAIChatting.this);
        mRequestQueue.getCache().clear();

        userAIChattingMessageModalArrayList = new ArrayList<>();

        userAIChatting_imgBtn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userAIChatting_eTxt_message.getText().toString().isEmpty()) {
                    Toast.makeText(UserAIChatting.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendMessage(userAIChatting_eTxt_message.getText().toString());

                userAIChatting_eTxt_message.setText("");
            }
        });

        userAIChattingMessageRVAdapter = new UserAIChattingMessageRVAdapter(userAIChattingMessageModalArrayList, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserAIChatting.this, RecyclerView.VERTICAL, false);
        userAIChatting_recyclerV_chat.setLayoutManager(linearLayoutManager);
        userAIChatting_recyclerV_chat.setAdapter(userAIChattingMessageRVAdapter);
    }

    private void sendMessage(String userMsg) {
        userAIChattingMessageModalArrayList.add(new UserAIChattingMessageModal(userMsg, USER_KEY));
        userAIChattingMessageRVAdapter.notifyDataSetChanged();

        String url = "http://api.brainshop.ai/get?bid=159458&key=q29yTPbDHr89KUNu&uid=[uid]&msg=" + userMsg;

        RequestQueue queue = Volley.newRequestQueue(UserAIChatting.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String botResponse = response.getString("cnt");
                    userAIChattingMessageModalArrayList.add(new UserAIChattingMessageModal(botResponse, BOT_KEY));
                    userAIChattingMessageRVAdapter.notifyDataSetChanged();
                    userAIChatting_recyclerV_chat.smoothScrollToPosition(userAIChattingMessageModalArrayList.size()-1);

                } catch (JSONException e) {
                    e.printStackTrace();
                    userAIChattingMessageModalArrayList.add(new UserAIChattingMessageModal("No response", BOT_KEY));
                    userAIChattingMessageRVAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userAIChattingMessageModalArrayList.add(new UserAIChattingMessageModal("Sorry no response found", BOT_KEY));
            }
        });

        queue.add(jsonObjectRequest);
    }

    public void toHome(View view) {
        finish();
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        Boolean needFinish = true;

        switch(view.getId()){
            case R.id.userAIChatting_btn_bottomMenuReport:
                intent = new Intent(this, UserReportMenu.class);
                break;
        }

        startActivity(intent);

        if(needFinish)
            finish();
    }
}