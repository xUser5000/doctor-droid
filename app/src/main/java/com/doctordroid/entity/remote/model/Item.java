package com.doctordroid.entity.remote.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

    private String id;
    private String name;
    private List<Choice> choices;

}
