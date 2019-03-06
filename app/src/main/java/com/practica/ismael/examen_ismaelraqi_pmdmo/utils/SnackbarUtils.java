package com.practica.ismael.examen_ismaelraqi_pmdmo.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {

    private SnackbarUtils() {
    }

    public static void snackbar(View v, String message, int length){
        Snackbar.make(v, message, length).show();
    }
}
