package com.practica.ismael.examen_ismaelraqi_pmdmo.data;

import android.os.AsyncTask;

import com.practica.ismael.examen_ismaelraqi_pmdmo.base.Resource;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.BookDao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RepositoryImpl implements Repository {

    private BookDao bookDao;

    public RepositoryImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public LiveData<List<Book>> queryBooks() {
        return bookDao.queryBooks();
    }

    @Override
    public LiveData<Book> queryBook(long bookId) {
        return bookDao.queryBook(bookId);
    }


    public LiveData<Resource<Long>> insertBook(final Book book) {
        MutableLiveData<Resource<Long>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                long id = bookDao.insert(book);
                result.postValue(Resource.success(id));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

    @Override
    public LiveData<Resource<Integer>> deleteBook(Book book) {
        MutableLiveData<Resource<Integer>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                int deleted = bookDao.delete(book);
                result.postValue(Resource.success(deleted));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }



}