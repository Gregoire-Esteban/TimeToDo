package dev.wazapps.timetodo.DI

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.wazapps.timetodo.data.TodoDatabase
import dev.wazapps.timetodo.utils.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TodoDatabase::class.java,
        Constants.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: TodoDatabase) = database.todoDao()
}