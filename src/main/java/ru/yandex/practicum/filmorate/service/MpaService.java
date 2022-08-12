package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.DbMpaStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage dbMpaStorage;

    @Autowired
    public MpaService(DbMpaStorage dbMpaStorage) {
        this.dbMpaStorage = dbMpaStorage;
    }

    public Collection<Mpa> findAll() {
        return dbMpaStorage.findAll();
    }

    public Mpa create(Mpa mpa) {
        return dbMpaStorage.create(mpa);
    }

    public Mpa update(Mpa mpa) {
        return dbMpaStorage.update(mpa);
    }

    public Mpa getMpa(int id) {
        Mpa mpa = dbMpaStorage.getMpa(id);
        if (mpa.getId() == 0 && mpa.getName() == null) {
            log.warn("Отсутствует рейтинг MPA с id=" + id);
            throw new NoFoundException("Не удалось найти MPA с id = " + id);
        }
        return mpa;
    }
}
