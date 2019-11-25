package com.doctordroid.presentation.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Comparable<Message> {

    private long time;
    private String text;
    private int sender;

    public static final class Sender {
        public static final int USER = 0;
        public static final int ROBOT = 1;
    }

    @Override
    public int compareTo(Message message) {
        return Long.compare(this.time, message.time);
    }
}
