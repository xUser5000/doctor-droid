package com.doctordroid.entity.local;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Chat extends RealmObject {

    @PrimaryKey
    private String id;
    private String gender;
    private int age;
    private String name;
    private RealmList<LocalQuestion> localQuestions;
    private RealmList<Answer> answers;
    private RealmList<LocalEvidence> localEvidence;
    private RealmList<LocalCondition> localConditions;

}
