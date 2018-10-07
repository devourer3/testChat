package com.example.gibeom.testchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ChildEventListener {

    static public String ANDROID_DEVICE_ID;
    static public Long idxNumber;

    @BindView(R.id.chatRecycler)
    RecyclerView chatRecycler;
    @BindView(R.id.chatText)
    EditText chatText;
    @BindView(R.id.sendButton)
    Button sendButton;
    List<MessageModel> msgList;
    ChatAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference chatAddRef;
    Query chatListRef, chatListAddRef;
    Context mCtx;
    Integer paginationNumber;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCtx = getApplicationContext();
        idxNumber = 0L;
        paginationNumber = 10;
        ANDROID_DEVICE_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        database = FirebaseDatabase.getInstance();
        msgList = new ArrayList<>();
        adapter = new ChatAdapter(msgList, mCtx);
//        chatListRef = database.getReference("testChat").child("singleRoom").orderByChild("chatTime").limitToLast(20);
//        chatListAddRef = database.getReference("testChat").child("singleRoom").orderByChild("chatIdx").startAt("0").endAt("10");
        chatListAddRef = database.getReference("testChat").child("singleRoom").orderByChild("chatIdx");
        chatListAddRef.addChildEventListener(this);
//        loadMore();
    }

    private void loadMore() {
        chatListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    MessageModel model = snapshot.getValue(MessageModel.class);
                    msgList.add(model);
                    adapter.notifyDataSetChanged();
                    chatRecycler.smoothScrollToPosition(msgList.size());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("GB", databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mCtx);
//        mLayoutManager.setReverseLayout(true);
//        mLayoutManager.setStackFromEnd(true);
        chatRecycler.setLayoutManager(mLayoutManager);
        chatRecycler.setHasFixedSize(true);
        chatRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (!recyclerView.canScrollVertically(-1)) {
//                    paginationNumber+=5;
//                    chatListRef = database.getReference("testChat").child("singleRoom").orderByChild("chatTime").startAt(0).limitToLast(10);
//                    loadMore();
//                }
//            }
        });
        chatRecycler.setAdapter(adapter);
    }

    @OnClick(R.id.sendButton)
    public void onViewClicked() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String nowTime = simpleDateFormat.format(date);
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        chatAddRef = database.getReference("testChat").child("singleRoom").push();
        map.put("deviceId", ANDROID_DEVICE_ID);
        map.put("deviceName", Build.BRAND + "(" + Build.MODEL + ")");
        map.put("chatMessage", chatText.getText().toString());
        map.put("chatTime", nowTime);
        map.put("chatIdx", String.valueOf(idxNumber));
        chatAddRef.setValue(map);
        chatText.setText("");
    }


    /**
     * ChildEvent 리스너
     *
     * @param dataSnapshot
     * @param s
     */


    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        MessageModel msg = dataSnapshot.getValue(MessageModel.class);
        idxNumber = Long.valueOf(msg.getChatIdx())+1;
        msgList.add(msg);
        adapter.notifyDataSetChanged();
        chatRecycler.smoothScrollToPosition(msgList.size());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

}
