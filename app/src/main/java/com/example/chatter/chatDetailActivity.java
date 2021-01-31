package com.example.chatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatter.Adapters.chatAdapter;
import com.example.chatter.Models.messagesModel;
import com.example.chatter.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        String senderId=auth.getUid();
        String reciverId=getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("userName");
        String profilePic=getIntent().getStringExtra("profilePic");

        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avtar).into(binding.profileImage);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(chatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<messagesModel>Messagesmodel=new ArrayList<>();
        final chatAdapter Chatadapter=new chatAdapter(Messagesmodel,this,reciverId);
        binding.chatRecyclerView.setAdapter(Chatadapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);

        final String senderRoom=senderId+reciverId;
        final String receiverRoom=reciverId+senderId;

        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Messagesmodel.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            messagesModel model=dataSnapshot.getValue(messagesModel.class);
                            model.setMessageId(dataSnapshot.getKey());
                            Messagesmodel.add(model);

                        }
                        Chatadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String message= binding.edtMessage.getText().toString();
               final messagesModel model=new messagesModel(senderId,message);
               model.setTimeStamp(new Date().getTime());

               binding.edtMessage.setText("");
               database.getReference().child("chats")
                       .child(senderRoom)
                       .push()
                       .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       database.getReference()
                               .child("chats")
                               .child(receiverRoom)
                               .push()
                               .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {

                           }
                       });
                   }
               });
            }
        });
    }
}