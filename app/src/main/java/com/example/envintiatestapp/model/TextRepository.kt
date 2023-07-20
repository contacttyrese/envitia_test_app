package com.example.envintiatestapp.model

import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.inject.Inject

class TextRepository @Inject constructor(
    private val file: File,
) {
    val capTexts = arrayListOf<CapturedText>()
    private val isTest = file.path == "testPath"

    init {
        if (!isTest){
            loadCapturedTexts()
        }
    }

    fun saveTextToJson(text: CapturedText): Boolean {
        var didSaveSuccess = false
        if (isCapturedTextValid(text)) {
            Log.i("repo_saving_text", "saving text to json file")
                val jsonWriter = JsonWriter(FileWriter(file))
            Log.i("repo_saving_text", "attempting to save ${text.text} to file")

            val newList = arrayListOf(text)
            newList.addAll(capTexts)

            jsonWriter.beginArray()
            newList.forEach { textToAdd ->
                jsonWriter.beginObject()
                if (!isTest) {
                    jsonWriter.name("time").value(textToAdd.time)
                    jsonWriter.name("text").value(textToAdd.text)
                }
                jsonWriter.endObject()
            }
            jsonWriter.endArray()
            jsonWriter.close()
            didSaveSuccess = true
        }
        return didSaveSuccess
    }

    fun getLoadedTexts(): Single<MutableList<CapturedText>> {
        loadCapturedTexts()
        Log.i("repo_loading_text", "attempting to load texts into observable")
        val observable = Observable.fromIterable(capTexts)
        return observable.subscribeOn(Schedulers.io())
            .toList()
            .doOnSuccess { list ->
                capTexts.removeAll(capTexts.toSet())
                capTexts.addAll(list)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun isCapturedTextValid(text: CapturedText): Boolean {
        var isValid = false
        if (text.text.isNotBlank() && text.time.isNotBlank()) {
            try {
                SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).parse(text.time)
                isValid = true
            } catch (exception: ParseException) {
                Log.e("is_timeformat_valid", "time cannot be parsed")
            }
        }
        return isValid
    }

    private fun loadCapturedTexts() {
        Log.i("repo_loading_json", "attempting to load texts from json file")
        val capturedTexts = arrayListOf<CapturedText>()
        val jsonReader = JsonReader(FileReader(file))
        try {
            jsonReader.beginArray()
            while (jsonReader.hasNext()) {
                jsonReader.beginObject()
                var savedTime = ""
                var savedText = ""
                while (jsonReader.hasNext()) {
                    val name = jsonReader.nextName()
                    if (name.equals("time")) {
                        savedTime = jsonReader.nextString()
                    } else if (name.equals("text")) {
                        savedText = jsonReader.nextString()
                    }
                }
                jsonReader.endObject()
                val capText = CapturedText(savedTime, savedText)
                capturedTexts.add(capText)
            }
            jsonReader.endArray()
            capTexts.removeAll(capTexts.toSet())
            capTexts.addAll(capturedTexts)
        } catch (exception: IOException) {
            exception.printStackTrace()
        } finally {
            jsonReader.close()
        }
    }
}