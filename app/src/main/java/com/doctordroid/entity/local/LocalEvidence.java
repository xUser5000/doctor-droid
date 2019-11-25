package com.doctordroid.entity.local;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RealmClass
public class LocalEvidence implements RealmModel {

    private String id;
    private String choice_id;

}
