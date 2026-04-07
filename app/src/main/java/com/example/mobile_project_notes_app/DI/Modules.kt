package com.example.mobile_project_notes_app.DI

import org.koin.core.module.Module

//Aggregator
val appModules: List<Module> = listOf(appModule, dataModule, viewModelModule)