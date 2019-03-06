package com.practica.ismael.examen_ismaelraqi_pmdmo.data.local;

import com.practica.ismael.examen_ismaelraqi_pmdmo.base.BaseDao;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface BookDao extends BaseDao<Book> {

    @Query("SELECT * FROM Book WHERE idBook = :bookId")
    LiveData<Book> queryBook(long bookId);

    @Query("SELECT * FROM Book ORDER BY nameBook")
    LiveData<List<Book>> queryBooks();



}