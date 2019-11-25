package com.doctordroid.presentation.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doctordroid.R;

public class MessageView extends RelativeLayout {

    private LinearLayout messageContainer;
    private TextView messageText;

    public MessageView(Context context) {
        super(context);
        init();
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init () {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_message, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        messageContainer = findViewById(R.id.view_message_container);
        messageText = findViewById(R.id.view_message_text);
    }

    public void setMessage (Message message) {
        messageText.setText(message.getText());

        if (message.getSender() == Message.Sender.ROBOT) {
            messageContainer.setGravity(Gravity.START);
            messageText.setBackgroundResource(R.drawable.background_message_gray);
            messageText.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            messageContainer.setGravity(Gravity.END);
            messageText.setBackgroundResource(R.drawable.background_message_blue);
            messageText.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

}
