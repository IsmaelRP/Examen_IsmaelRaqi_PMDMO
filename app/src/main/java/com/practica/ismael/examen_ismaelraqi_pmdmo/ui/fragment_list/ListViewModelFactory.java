package com.practica.ismael.examen_ismaelraqi_pmdmo.ui.fragment_list;

import android.app.Application;

import com.practica.ismael.examen_ismaelraqi_pmdmo.data.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ListViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final Repository repository;

    public ListViewModelFactory(@NonNull Application application, @NonNull Repository repository) {
        this.application = application;
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(application, repository);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
