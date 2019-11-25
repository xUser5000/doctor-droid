package com.doctordroid.entity.local;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalConditionInfo extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String genderFilter;
    private RealmList<String> categories;
    private String prevalence;
    private String acuteness;
    private String severity;
    private String hint;

}
