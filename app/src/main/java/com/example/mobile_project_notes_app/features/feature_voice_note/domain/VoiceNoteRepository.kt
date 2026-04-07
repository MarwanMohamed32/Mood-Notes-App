package com.example.mobile_project_notes_app.features.feature_voice_note.domain

import java.io.File

interface VoiceNoteRepository {
    suspend fun startRecording(): File
    suspend fun stopRecording()
    fun getLastRecording(): File?
}