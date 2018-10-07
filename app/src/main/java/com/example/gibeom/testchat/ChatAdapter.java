package com.example.gibeom.testchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static com.example.gibeom.testchat.MainActivity.ANDROID_DEVICE_ID;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    List<MessageModel> msgList;
    Context mCtx;

    public ChatAdapter(List<MessageModel> msgList, Context mCtx) {
        this.msgList = msgList;
        this.mCtx = mCtx;
    }

    @Override
    public int getItemViewType(int position) {
        if(msgList.get(position).getDeviceId().equals(ANDROID_DEVICE_ID)) { // 내 안드로이드 단말과 디바이스 아이디와 같다면
            return 1;
        } else { // 아니면
            return 0;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MessageModel msgItem = msgList.get(position);
        holder.chatDevice.setText(msgItem.getDeviceName());
        holder.chatMessage.setText(msgItem.getChatMessage());
        holder.chatTime.setText(msgItem.getChatTime());
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView chatDevice, chatMessage, chatTime;

        ViewHolder(View itemView) {
            super(itemView);
            chatDevice = itemView.findViewById(R.id.list_item_chat_device);
            chatMessage = itemView.findViewById(R.id.list_item_chat_message);
            chatTime = itemView.findViewById(R.id.list_item_chat_time);
        }
    }


}
