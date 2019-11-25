package com.doctordroid.entity.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConditionResponse {

    private String id;
    private String name;
    @SerializedName("sex_filter") private String genderFilter;
    private List<String> categories;
    private String prevalence;
    private String acuteness;
    private String severity;
    private Extras extras;

    public class Extras {
        public String hint;
        public String icd10_code;
    }

}
