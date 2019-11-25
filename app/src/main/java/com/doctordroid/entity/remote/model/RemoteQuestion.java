package com.doctordroid.entity.remote.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoteQuestion {

    private String type;
    private String text;
    private List<Item> items;

}
