package com.pngchecklistmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM Tasks ORDER BY dueDate ASC")
    List<Task> getAllByDueDate();

    @Query("SELECT * FROM Tasks ORDER BY category ASC")
    List<Task> getAllByCategory();

    @Query("SELECT * FROM Tasks WHERE id = :id LIMIT 1")
    Task getById(int id);
}
