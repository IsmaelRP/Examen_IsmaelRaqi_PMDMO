package com.practica.ismael.examen_ismaelraqi_pmdmo.base;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@SuppressWarnings("unchecked")
public interface BaseDao<M> {

    @Insert
    long insert(M model);

    @Delete
    int delete(M... model);

}