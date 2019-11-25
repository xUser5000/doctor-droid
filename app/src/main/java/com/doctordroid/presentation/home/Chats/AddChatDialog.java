package com.doctordroid.presentation.home.Chats;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.doctordroid.R;
import com.doctordroid.common.util.IdGenerator;
import com.doctordroid.entity.local.Chat;
import com.doctordroid.entity.local.Gender;

public class AddChatDialog extends DialogFragment {

    private OnAddChatDialogListener listener;

    public void setListener (OnAddChatDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_chat, null);

        builder
                .setTitle("Add a new conversation")
                .setView(view)
                .setPositiveButton("Confirm", (dialogInterface, i) -> {

                    EditText nameText = view.findViewById(R.id.dialog_add_chat_name_text);
                    EditText ageText = view.findViewById(R.id.dialog_add_chat_age_text);
                    RadioButton maleRadio = view.findViewById(R.id.dialog_add_chat_radio_male);

                    String name = nameText.getText().toString().trim();
                    String ageT = ageText.getText().toString().trim();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageT)) {
                        Toast.makeText(getContext(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int age = Byte.parseByte(ageT);
                    String gender = (maleRadio.isChecked()) ? (Gender.MALE) : (Gender.FEMALE);

                    Chat chat = new Chat();
                    chat.setId(IdGenerator.randomIdentifier());
                    chat.setName(name);
                    chat.setAge(age);
                    chat.setGender(gender);

                    listener.onPositiveClick(chat);

                    dialogInterface.dismiss();

                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        return builder.create();
    }

    public interface OnAddChatDialogListener {
        void onPositiveClick (Chat chat);
    }
}
