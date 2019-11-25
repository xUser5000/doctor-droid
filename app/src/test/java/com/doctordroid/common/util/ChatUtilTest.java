package com.doctordroid.common.util;

import com.doctordroid.entity.local.Answer;
import com.doctordroid.entity.local.LocalQuestion;
import com.doctordroid.presentation.chat.Message;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ChatUtilTest {

    @Test
    public void toMessage() {
        LocalQuestion localQuestion = new LocalQuestion(System.currentTimeMillis(), LocalQuestion.Type.FREE_TEXT, "Why ?");
        Answer answer = new Answer(System.currentTimeMillis(), "Because it is fun");

        Message message1 = ChatUtil.toMessage(localQuestion);
        assertEquals(message1.getTime(), localQuestion.getTime());
        assertEquals(message1.getText(), localQuestion.getText());

        Message message2 = ChatUtil.toMessage(answer);
        assertEquals(message2.getTime(), answer.getTime());
        assertEquals(message2.getText(), answer.getText());
    }

    @Test
    public void sort() {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            long time = (long) (Math.random() * 10);
            messages.add(new Message(time, "Your time is " + time, Message.Sender.USER));
        }

        for (Message message: ChatUtil.sort(messages)) System.out.println(message.getText());
    }

    @Test
    public void formString () {
        List<String> stringList = new ArrayList<>(Arrays.asList("Aekhrgb", "isngegr", "oierjg"));
        assertEquals(ChatUtil.fromStringListToString(stringList), "Aekhrgb, isngegr, oierjg");
    }
}