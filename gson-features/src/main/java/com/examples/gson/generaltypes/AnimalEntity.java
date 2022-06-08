package com.examples.gson.generaltypes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimalEntity {
    private Animal animal;
    private int price; /*价格*/
}
