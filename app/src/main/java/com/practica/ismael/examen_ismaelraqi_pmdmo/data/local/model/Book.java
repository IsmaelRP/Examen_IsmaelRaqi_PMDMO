package com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"nameBook"}, unique = true)})
public class Book {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idBook", index = true)
    private long idBook;

    @ColumnInfo(name = "nameBook")
    private String nameBook;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "date")
    private int date;

    @ColumnInfo(name = "cover")
    private String cover;

    @ColumnInfo(name = "synopsis")
    private String synopsis;

    public Book(long idBook, String nameBook, String author, int date, String cover, String synopsis) {
        this.idBook = idBook;
        this.nameBook = nameBook;
        this.author = author;
        this.date = date;
        this.cover = cover;
        this.synopsis = synopsis;
    }

    public long getIdBook() {
        return idBook;
    }

    public String getNameBook() {
        return nameBook;
    }

    public String getAuthor() {
        return author;
    }

    public int getDate() {
        return date;
    }

    public String getCover() {
        return cover;
    }

    public String getSynopsis() {
        return synopsis;
    }
}
