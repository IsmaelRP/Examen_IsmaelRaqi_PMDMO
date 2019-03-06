package com.practica.ismael.examen_ismaelraqi_pmdmo.ui.activity;

import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Book> addBookLiveData = new MutableLiveData<>();
    private MutableLiveData<Book> showBookLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> undoDelete = new MutableLiveData<>();
    private MutableLiveData<Boolean> showConfirm = new MutableLiveData<>();

    public MutableLiveData<Book> getAddBookLiveData() {
        return addBookLiveData;
    }

    public void setAddBookLiveData(Book book) {
        this.addBookLiveData.setValue(book);
    }

    public MutableLiveData<Book> getShowBookLiveData() {
        return showBookLiveData;
    }

    public void setShowBookLiveData(Book book) {
        this.showBookLiveData.setValue(book);
    }

    public MutableLiveData<Boolean> getUndoDelete() {
        return undoDelete;
    }

    public void setUndoDelete(Boolean undoDelete) {
        this.undoDelete.setValue(undoDelete);
    }

    public MutableLiveData<Boolean> getShowConfirm() {
        return showConfirm;
    }

    public void setShowConfirm(Boolean confirm) {
        this.showConfirm.setValue(confirm);
    }
}
