package com.practica.ismael.examen_ismaelraqi_pmdmo.ui.fragment_book;

import android.app.Application;

import com.practica.ismael.examen_ismaelraqi_pmdmo.data.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BooksFormViewModelFactory implements ViewModelProvider.Factory{

    private final Application application;
    private final Repository repository;

    public BooksFormViewModelFactory(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BooksFormViewModel.class)) {
            return (T) new BooksFormViewModel(application, repository);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
