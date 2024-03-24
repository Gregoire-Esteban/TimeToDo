package dev.wazapps.timetodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.wazapps.timetodo.data.models.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class TodoDatabase() : RoomDatabase() {
    abstract fun todoDao(): ToDoDao
}