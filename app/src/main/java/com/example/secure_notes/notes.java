package com.example.secure_notes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class notes {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "title")
    String text;

    @ColumnInfo(name = "descripation")
    String descri;

    @ColumnInfo(name = "date")
    String date;

    @ColumnInfo(name = "time")
    String time;


    public notes( String text, String descri, String date, String time) {
        this.text = text;
        this.descri = descri;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }
}
