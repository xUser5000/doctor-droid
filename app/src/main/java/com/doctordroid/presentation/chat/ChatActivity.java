package com.doctordroid.presentation.chat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doctordroid.R;
import com.doctordroid.common.util.ChatUtil;
import com.doctordroid.entity.local.Chat;
import com.doctordroid.entity.local.LocalQuestion;
import com.doctordroid.entity.remote.response.ParseResponse;
import com.doctordroid.presentation.results.ResultsActivity;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class ChatActivity extends AppCompatActivity implements ChatInterface {

    // ui
    private TextView titleText;
    private ImageButton deleteMessagesButton;
    private RecyclerView recyclerView;
    private ChatMessagesAdapter adapter;

    private LinearLayout yesNoContainer, freeTextContainer;
    private RelativeLayout progressContainer, getResultContainer;
    private EditText freeText;
    private FloatingActionButton sendButton;
    private Button yesButton, noButton, dontKnowButton;
    private Button getResultButton;

    // data
    private ChatViewModel viewModel;
    private Disposable subscription;
    private String chatId;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        viewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        viewModel.setChatInterface(this);

        if (viewModel.getChatId() == null) {
            chatId = getIntent().getStringExtra("chatId");
            viewModel.setChatId(chatId);
        } else chatId = viewModel.getChatId();

        // view bindings
        titleText = findViewById(R.id.chat_title_text);
        deleteMessagesButton = findViewById(R.id.chat_delete_messages);
        recyclerView = findViewById(R.id.chat_messages_recycler_view);

        yesNoContainer = findViewById(R.id.chat_yes_no_container);
        freeTextContainer = findViewById(R.id.chat_text_container);
        progressContainer = findViewById(R.id.chat_progress_container);
        getResultContainer = findViewById(R.id.chat_get_result_container);

        freeText = findViewById(R.id.chat_free_text);
        sendButton = findViewById(R.id.chat_send_button);
        yesButton = findViewById(R.id.chat_yes_button);
        noButton = findViewById(R.id.chat_no_button);
        dontKnowButton = findViewById(R.id.chat_dont_know_button);
        getResultButton = findViewById(R.id.chat_get_result_button);

        initRecyclerView();

        // click listeners
        sendButton.setOnClickListener(this::sendFreeText);
        deleteMessagesButton.setOnClickListener(this::deleteAllMessages);
        yesButton.setOnClickListener(view -> viewModel.answerYesNoQuestion("Yes", ChatViewModel.YesNoAnswer.YES));
        noButton.setOnClickListener(view -> viewModel.answerYesNoQuestion("No", ChatViewModel.YesNoAnswer.NO));
        dontKnowButton.setOnClickListener(view -> viewModel.answerYesNoQuestion("I don't know", ChatViewModel.YesNoAnswer.DONT_KNOW));
        getResultButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("chatId", chatId);
            startActivity(intent);
        });

        // get chats and listen for changes using RX
        subscription = viewModel.getChat(chatId).subscribe(change -> {

            Chat chat = (Chat) change.getObject();

            if (TextUtils.isEmpty(titleText.getText().toString()))
                titleText.setText(chat.getName());

            adapter.setMessages(ChatUtil.getMessages(chat));
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);

            // show an input suitable for the remoteQuestion
            if (!chat.getLocalQuestions().isEmpty()) {

                if (ChatUtil.isCompleted(chat)) showGetResults();
                else {

                    String type = chat.getLocalQuestions().last().getType();
                    switch (type) {

                        case LocalQuestion.Type.FREE_TEXT:
                            showFreeText();
                            break;

                        case LocalQuestion.Type.SINGLE:
                            showYesNo();
                            break;

                        case LocalQuestion.Type.INFO:
                            showProgress();
                            break;
                    }
                }
            }

        });
    }

    private void initRecyclerView () {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatMessagesAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    public void sendFreeText (View view) {
        String text = freeText.getText().toString().trim();

        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, "Please, write down your answer", Toast.LENGTH_SHORT).show();
            return;
        }

        freeText.setText("");

        viewModel.parse(text).subscribe(new SingleObserver<ParseResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showProgress();
            }

            @Override
            public void onSuccess(ParseResponse response) {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllMessages (View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Remove all Messages")
                .setMessage("Are you sure you want to delete all the messages ?")
                .setPositiveButton("Delete", (dialogInterface, i) -> viewModel.deleteAllMessages())
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        dialog.show();
    }

    private void showFreeText() {
        freeTextContainer.setVisibility(View.VISIBLE);
        yesNoContainer.setVisibility(View.INVISIBLE);
        progressContainer.setVisibility(View.INVISIBLE);
        getResultContainer.setVisibility(View.INVISIBLE);
    }

    private void showYesNo() {
        freeTextContainer.setVisibility(View.INVISIBLE);
        yesNoContainer.setVisibility(View.VISIBLE);
        progressContainer.setVisibility(View.INVISIBLE);
        getResultContainer.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {
        freeTextContainer.setVisibility(View.INVISIBLE);
        yesNoContainer.setVisibility(View.INVISIBLE);
        progressContainer.setVisibility(View.VISIBLE);
        getResultContainer.setVisibility(View.INVISIBLE);
    }

    private void showGetResults() {
        freeTextContainer.setVisibility(View.INVISIBLE);
        yesNoContainer.setVisibility(View.INVISIBLE);
        progressContainer.setVisibility(View.INVISIBLE);
        getResultContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void startLoading() {
        showProgress();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.dispose();
        subscription = null;
    }
}
