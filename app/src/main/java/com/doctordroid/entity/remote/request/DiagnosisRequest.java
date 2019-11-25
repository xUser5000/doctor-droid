package com.doctordroid.entity.remote.request;

import com.doctordroid.entity.remote.model.RemoteEvidence;
import com.doctordroid.entity.remote.model.Extras;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisRequest {

    private String sex;
    private int age;
    private List<RemoteEvidence> evidence;
    private final Extras extras = new Extras();

}
