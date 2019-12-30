package com.doctordroid.presentation.home.Chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.doctordroid.R;
import com.doctordroid.common.util.ChatUtil;
import com.doctordroid.entity.local.Chat;
import com.doctordroid.presentation.chat.Message;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatHolder> {

    private List<Chat> chats = new ArrayList<>();

    // view holder
    public static class ChatHolder extends RecyclerView.ViewHolder {

        private TextView titleText, lastMessageText;

        public ChatHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.item_chats_title_text);
            lastMessageText = view.findViewById(R.id.item_chats_last_message_text);
        }
    }

    @Nonnull
    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_chats_recycler_view, parent, false);
        return new ChatHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.titleText.setText(chat.getName());

        List<Message> messages = ChatUtil.getMessages(chat);

        if (!messages.isEmpty()) {
            holder.lastMessageText.setText(messages.get(messages.size() - 1).getText());
        }

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    public Chat getChatAt (int position) {
        return chats.get(position);
    }
}
