package com.example.trekmesh

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EncounterLogger {

    private const val ENCOUNTERS_FILE = "encounters.csv"
    private const val REGISTRY_FILE   = "rifugio_registry.csv"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    // --- Hiker side ---

    fun logEncounter(context: Context, peerId: String, lat: Double?, lon: Double?, alt: Double?) {
        val ts = dateFormat.format(Date())
        val line = "$peerId,${lat ?: ""},${lon ?: ""},${alt ?: ""},$ts\n"
        File(context.filesDir, ENCOUNTERS_FILE).appendText(line)
    }

    fun hasEncounters(context: Context): Boolean =
        File(context.filesDir, ENCOUNTERS_FILE).let { it.exists() && it.length() > 0 }

    /** Returns file contents as bytes and deletes the file. */
    fun popEncounters(context: Context): ByteArray? {
        val file = File(context.filesDir, ENCOUNTERS_FILE)
        if (!file.exists()) return null
        val data = file.readBytes()
        file.delete()
        return data
    }

    // --- Rifugio side ---
    fun mergeIntoRegistry(context: Context, data: ByteArray, reportedBy: String) {
        val registry = File(context.filesDir, REGISTRY_FILE)
        val ts = dateFormat.format(Date())
        val lines = String(data, Charsets.UTF_8).trim().lines()
        val appended = lines.filter { it.isNotBlank() }.joinToString("\n") { "$it,$reportedBy,$ts" }
        if (appended.isNotBlank()) registry.appendText(appended + "\n")
    }

    fun getRegistryPath(context: Context): String =
        File(context.filesDir, REGISTRY_FILE).absolutePath
}
