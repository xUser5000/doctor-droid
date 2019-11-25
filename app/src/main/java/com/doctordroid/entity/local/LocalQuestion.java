package com.doctordroid.entity.local;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocalQuestion extends RealmObject {

    @PrimaryKey
    private long time;      // the time of the remoteQuestion
    private String id;      // id of the symptom
    private String type;    // free_text, single
    private String text;

    public LocalQuestion(long time, String type, String text) {
        this.time = time;
        this.type = type;
        this.text = text;
    }

    public class Type {
        public static final String FREE_TEXT = "free_text";
        public static final String SINGLE = "single";
        public static final String INFO = "info";
    }

}