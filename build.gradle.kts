import java.io.FileInputStream
import java.util.Properties

fun loadEnvVars(): Map<String, String> {
    val envFile = rootProject.file(".env")
    if (!envFile.exists()) {
        println("Warning: .env file not found")
        return emptyMap()
    }
    
    val properties = Properties()
    FileInputStream(envFile).use { properties.load(it) }
    
    return properties.entries.associate { entry ->
        val key = entry.key.toString()
        val value = entry.value.toString()
        key to value
    }
}

val envVars = loadEnvVars()
ext {
    envVars.forEach { (key, value) ->
        set(key, value)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
}