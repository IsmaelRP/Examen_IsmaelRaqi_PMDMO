package com.practica.ismael.examen_ismaelraqi_pmdmo.data;

import com.practica.ismael.examen_ismaelraqi_pmdmo.base.Resource;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;

import java.util.List;

import androidx.lifecycle.LiveData;


@SuppressWarnings({"WeakerAccess", "unused"})
public interface Repository {

    //  Books
    LiveData<List<Book>> queryBooks();
    LiveData<Book> queryBook(long bookId);
    LiveData<Resource<Long>> insertBook(Book book);
    LiveData<Resource<Integer>> deleteBook(Book book);

}