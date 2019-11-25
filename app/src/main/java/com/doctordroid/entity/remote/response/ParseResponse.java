package com.doctordroid.entity.remote.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParseResponse {

    private List<Mention> mentions;
    private boolean obvious;

    public class Mention {
        public String id;
        public String orth;
        public String choice_id;
        public String name;
        public String common_name;
        public String type;
    }

}
