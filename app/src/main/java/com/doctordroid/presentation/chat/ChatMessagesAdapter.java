package com.doctordroid.presentation.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.doctordroid.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.MessageHolder> {

    private List<Message> messages = new ArrayList<>();

    static class MessageHolder extends RecyclerView.ViewHolder {

        private MessageView messageView;

        private MessageHolder(View view) {
            super(view);
            messageView = view.findViewById(R.id.item_message);
        }
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_messages_recycler_view, parent, false);
        return new MessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageView.setMessage(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

}
