package com.practica.ismael.examen_ismaelraqi_pmdmo.ui.fragment_book;

import android.app.Application;

import com.practica.ismael.examen_ismaelraqi_pmdmo.R;
import com.practica.ismael.examen_ismaelraqi_pmdmo.base.Event;
import com.practica.ismael.examen_ismaelraqi_pmdmo.base.Resource;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.Repository;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class BooksFormViewModel extends ViewModel {


    private Application application;
    private Repository repository;
    private LiveData<Book> bookLiveData;
    private Event<Boolean> restoreData = new Event<>(false);

    private final MediatorLiveData<Event<String>> successMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Event<String>> errorMessage = new MediatorLiveData<>();

    private LiveData<Resource<Long>> insertResult;

    private final MutableLiveData<Book> insertTrigger = new MutableLiveData<>();


    BooksFormViewModel(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
        insertResult = Transformations.switchMap(insertTrigger, repository::insertBook);
        setupSuccessMessage();
        setupErrorMessage();
    }

    private void setupSuccessMessage() {
        successMessage.addSource(insertResult, resource -> {
            if (resource.hasSuccess() && resource.getData() >= 0) {
                successMessage.setValue(new Event<>(application.getString(R.string.book_inserted_successfully)));
            }
        });
    }

    private void setupErrorMessage() {
        errorMessage.addSource(insertResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(resource.getException().getMessage()));
            } else if (resource.hasSuccess() && resource.getData() <= 0) {
                errorMessage.setValue(new Event<>(application.getString(R.string.book_error_inserting)));
            }
        });
    }

    public LiveData<Book> getBook(long bookId) {
        if (bookLiveData == null) {
            bookLiveData = repository.queryBook(bookId);
            this.restoreData = new Event<>(true);
        }
        return bookLiveData;
    }

    public LiveData<Event<String>> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }

    public void insertBook(Book book) {
        insertTrigger.setValue(book);
    }

    public Event<Boolean> getRestoreData() {
        return restoreData;
    }
}
