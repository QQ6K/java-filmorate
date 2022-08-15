package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mpa {
    int id;
    String name;

    public Mpa(int id, String name){
        this.id = id;
        this.name = name;
    }
}
