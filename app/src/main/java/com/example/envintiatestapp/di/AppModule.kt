package com.example.envintiatestapp.di

import android.content.Context
import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import com.example.envintiatestapp.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Provides
    fun provideJsonFile(@ApplicationContext app: Context): File {
        val nameOfFile = "sample_data_file.json"
        val file = File(app.filesDir, nameOfFile)
        Log.i("di_file", "checking if file exists or will attempt to create with path ${file.canonicalPath}")
        if (!file.exists()) {
            val wasFileCreated = file.createNewFile()
            val jsonWriter = JsonWriter(FileWriter(file))
            jsonWriter.beginArray()
            jsonWriter.endArray()
            jsonWriter.close()
            Log.i("di_file", "was file created: $wasFileCreated")
        }
        return file
    }

    @Provides
    fun provideDisposables(): CompositeDisposable {
        return CompositeDisposable()
    }
}