package com.examples.gson.generaltypes;

import lombok.Data;

import java.util.List;

@Data
public class AnimalHome {
    private String name;
    private List<AnimalEntity> entities;
}
