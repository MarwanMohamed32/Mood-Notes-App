package com.example.mobile_project_notes_app.DI

import androidx.room.Room
import com.example.mobile_project_notes_app.data.AppDatabase
import com.example.mobile_project_notes_app.data.repository.CalendarRepository
import com.example.mobile_project_notes_app.data.repository.NoteRepository
import com.example.mobile_project_notes_app.features.feature_category.CategoryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "notes_app_database"

        )
            .fallbackToDestructiveMigration().build()
    }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().noteDao() }
    single { get<AppDatabase>().calendarDayDao() }

    single { CalendarRepository(get()) }
    single { NoteRepository(get()) }
    single { CategoryRepository(get()) }

}