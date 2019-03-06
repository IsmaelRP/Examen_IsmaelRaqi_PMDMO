package com.practica.ismael.examen_ismaelraqi_pmdmo.ui.fragment_list;

import android.app.Application;

import com.practica.ismael.examen_ismaelraqi_pmdmo.R;
import com.practica.ismael.examen_ismaelraqi_pmdmo.base.Event;
import com.practica.ismael.examen_ismaelraqi_pmdmo.base.Resource;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.Repository;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class ListViewModel extends ViewModel {

    private final Application application;
    private final LiveData<List<Book>> books;
    private Repository repository;

    private LiveData<Resource<Integer>> deleteResult;

    private LiveData<Resource<Long>> insertResult;

    private final MutableLiveData<Book> deleteTrigger = new MutableLiveData<>();

    private final MutableLiveData<Book> insertTrigger = new MutableLiveData<>();

    private final MediatorLiveData<Event<String>> successMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Event<String>> errorMessage = new MediatorLiveData<>();

    ListViewModel(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
        deleteResult = Transformations.switchMap(deleteTrigger, repository::deleteBook);
        insertResult = Transformations.switchMap(insertTrigger, repository::insertBook);
        setupSuccessMessage();
        setupErrorMessage();
        books = this.repository.queryBooks();
    }

    private void setupSuccessMessage() {
        successMessage.addSource(deleteResult, resource -> {
            if (resource.hasSuccess() && resource.getData() >= 0) {
                successMessage.setValue(new Event<>(application.getString(R.string.book_deleted_successfully)));
            }
        });
        successMessage.addSource(insertResult, resource -> {
            if (resource.hasSuccess() && resource.getData() >= 0) {
                successMessage.setValue(new Event<>(application.getString(R.string.book_inserted_successfully)));
            }
        });
    }

    private void setupErrorMessage() {
        errorMessage.addSource(deleteResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(resource.getException().getMessage()));
            } else if (resource.hasSuccess() && resource.getData() <= 0) {
                errorMessage.setValue(new Event<>(application.getString(R.string.book_error_deleting)));
            }
        });
        errorMessage.addSource(insertResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(resource.getException().getMessage()));
            } else if (resource.hasSuccess() && resource.getData() <= 0) {
                errorMessage.setValue(new Event<>(application.getString(R.string.book_error_inserting)));
            }
        });
    }

    public LiveData<List<Book>> getBooks() {
        return books;
    }

    void deleteBook(Book book) {
        deleteTrigger.setValue(book);
    }

    void insertBook(Book book) {
        insertTrigger.setValue(book);
    }

    public LiveData<Event<String>> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }
}
