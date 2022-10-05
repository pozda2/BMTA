package com.example.bmta.model

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


data class Settings (var settingsJson : String ="") {
    var width = 30
    var height = 20
    var numForests = 5
    var enemies : JSONArray?
    var items : JSONArray?
    var hero: JSONObject?

    init {

        parseJsonInt(settingsJson, "width")?.let { width = it }
        parseJsonInt(settingsJson, "height")?.let { height = it }
        parseJsonInt(settingsJson, "numforests")?.let { numForests = it }
        hero = parseJsonObject(settingsJson, "hero")
        enemies = parseJsonArray(settingsJson, "enemies")
        items = parseJsonArray(settingsJson, "items")
    }

    fun parseJsonArray(settingsJson:String, item:String) : JSONArray? {
        try {
            val jObj = JSONObject(settingsJson)
            return jObj.getJSONArray(item)
        } catch (ex: JSONException) {
            Log.e("JsonParser Example", "unexpected JSON exception", ex)
        }
        return null
    }

    fun parseJsonInt(settingsJson:String, item:String) : Int? {
        var o : Int? = 0
        try {
            val jObj = JSONObject(settingsJson)
            o = jObj.getInt(item)
        } catch (ex: JSONException) {
            Log.e("JsonParser Example", "unexpected JSON exception", ex)
        }
        return o
    }

    fun parseJsonObject(settingsJson:String, item:String) : JSONObject? {
        var o : JSONObject? = null
        try {
            val jObj = JSONObject(settingsJson)
            o = jObj.getJSONObject(item)
        } catch (ex: JSONException) {
            Log.e("JsonParser Example", "unexpected JSON exception", ex)
        }
        return o
    }
}