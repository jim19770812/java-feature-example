package com.examples.gson.generaltypes.impl;

import com.examples.gson.generaltypes.Animal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DogImpl implements Animal {
    private String type;
    private String name;
    private int childrenCount;
}
