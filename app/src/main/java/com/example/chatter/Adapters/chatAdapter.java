package com.example.chatter.Adapters;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;

import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatter.Models.messagesModel;
import com.example.chatter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public class chatAdapter extends RecyclerView.Adapter{


    ArrayList<messagesModel>MessagesModels;
    Context context;
    FirebaseDatabase database;
    String recId;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;
    public chatAdapter(ArrayList<messagesModel> messagesModels, Context context) {
        MessagesModels = messagesModels;
        this.context = context;
    }

    public chatAdapter(ArrayList<messagesModel> messagesModels, Context context, String recId) {
        MessagesModels = messagesModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE){
            View view=LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new senderViewholder(view);
        }
        else
        {
            View view=LayoutInflater.from(context).inflate(R.layout.sample_reciver,parent,false);
            return new reciverViewholder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        messagesModel MessagesModel= MessagesModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete").setMessage("Are You Sure You Want To Delete This Message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String senderRoom=FirebaseAuth.getInstance().getUid()+recId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(MessagesModel.getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });
        if(holder.getClass()==senderViewholder.class){
            ((senderViewholder)holder).senderMessage.setText(MessagesModel.getMessage());
       //     String time = DateUtils.formatDateTime(context,MessagesModel.getTimeStamp(), DateUtils.FORMAT_SHOW_TIME);
        //   ((senderViewholder)holder).senderTime.setText(time);

        }
        else
        {
            ((reciverViewholder)holder).reciverMessage.setText(MessagesModel.getMessage());
         //   String time = DateUtils.formatDateTime(context,MessagesModel.getTimeStamp(), DateUtils.FORMAT_SHOW_TIME);
            //((reciverViewholder) holder).reciverTime.setText(time);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(MessagesModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return MessagesModels.size();
    }

    public class reciverViewholder extends RecyclerView.ViewHolder {

        TextView reciverMessage,reciverTime;
        public reciverViewholder(@NonNull View itemView) {
            super(itemView);
            reciverMessage=itemView.findViewById(R.id.reciverText);
            reciverTime=itemView.findViewById(R.id.reciverTime);
        }
    }
    public class senderViewholder extends RecyclerView.ViewHolder {
        TextView senderMessage,senderTime;
        public senderViewholder(@NonNull View itemView) {
            super(itemView);
            senderMessage=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.reciverText);
        }
    }
}
