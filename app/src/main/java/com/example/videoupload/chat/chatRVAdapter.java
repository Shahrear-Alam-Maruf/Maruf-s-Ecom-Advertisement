package com.example.videoupload.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoupload.MsgModal;
import com.example.videoupload.R;

import java.util.ArrayList;

public class chatRVAdapter  extends RecyclerView.Adapter {

    // variable for our array list and context.
    private ArrayList<ChatsModal> chatsModalArrayList;
    private Context context;

    public chatRVAdapter(ArrayList<ChatsModal> messageModalArrayList, Context context) {
        this.chatsModalArrayList = messageModalArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        // below code is to switch our
        // layout type along with view holder.
        switch (viewType) {
            case 0:
                // below line we are inflating user message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_rvb_item, parent, false);
                return new UserViewHolder(view);
            case 1:
                // below line we are inflating bot message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_rv_item, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatsModal modal = chatsModalArrayList.get(position);

        switch (modal.getSender()) {
            case "user":
                // below line is to set the text to our text view of user layout
                ((UserViewHolder) holder).userTV.setText(modal.getMessage());
                break;
            case "bot":
                // below line is to set the text to our text view of bot layout
                ((BotViewHolder) holder).botTV.setText(modal.getMessage());
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch ( chatsModalArrayList.get(position).getSender()){
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;

        }
    }

    @Override
    public int getItemCount() {
        return chatsModalArrayList.size();
    }

    //1st viewHolder For userMessage
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userTV;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing with id.
            userTV = itemView.findViewById(R.id.idTVUser);
        }
    }

    // viewHolder for bot
    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botTV;
        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing with id.
            botTV = itemView.findViewById(R.id.idTVBot);
        }
    }
}



