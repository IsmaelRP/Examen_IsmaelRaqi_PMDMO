package com.practica.ismael.examen_ismaelraqi_pmdmo.data.local;

import android.content.Context;
import android.provider.ContactsContract;

import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (ContactsContract.Data.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        }
                    }).build();
                }
            }
        }
        return instance;
    }

    public abstract BookDao bookDao();

}