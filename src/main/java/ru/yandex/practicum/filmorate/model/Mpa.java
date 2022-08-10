package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    int id;
    String name;

    public Mpa(){

    }

    public Mpa(int id, String name){
        this.id = id;
        this.name = name;
    }
}
