package com.example.secure_notes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes_table")
    LiveData<List<notes>> getAlldata();

    @Query("SELECT * FROM notes_table WHERE title LIKE '%' || :searchQuery || '%'")
    LiveData<List<notes>> searchNotes(String searchQuery);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(notes note);
    @Update
    void update(notes note);

    @Delete
    void delete(notes note);

    @Query("DELETE FROM notes_table")
    void deleteAllUsers();

}
