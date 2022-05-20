package com.examples.gson.generaltypes.impl;

import com.examples.gson.generaltypes.Animal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CatImpl implements Animal {
    private String type;
    private String name;
    private String color;
}
