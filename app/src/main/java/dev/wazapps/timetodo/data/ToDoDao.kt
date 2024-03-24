package dev.wazapps.timetodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.wazapps.timetodo.data.models.ToDoTask
import dev.wazapps.timetodo.utils.Constants.DATABASE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * from $DATABASE_TABLE ORDER BY id ASC")
    fun getAllTasks(): Flow<List<ToDoTask>>

    @Query("SELECT * from $DATABASE_TABLE WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoTask: ToDoTask)

    @Update
    suspend fun updateTask(toDoTask: ToDoTask)

    @Delete
    suspend fun deleteTask(toDoTask: ToDoTask)

    @Query("DELETE from $DATABASE_TABLE")
    suspend fun deleteAllTasks()

    @Query("SELECT * from $DATABASE_TABLE where title like :searchQuery OR description like :searchQuery")
    fun searchTasks(searchQuery: String): Flow<List<ToDoTask>>

    @Query("SELECT * from $DATABASE_TABLE ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    @Query("SELECT * from $DATABASE_TABLE ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): Flow<List<ToDoTask>>
}