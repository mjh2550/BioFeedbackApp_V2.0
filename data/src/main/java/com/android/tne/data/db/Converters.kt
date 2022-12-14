package com.android.tne.data.db

import androidx.room.TypeConverter
import com.android.tne.data.model.CircularQueue
import org.json.JSONArray
import org.json.JSONObject

class Converters {


    /**
     * 저장
     * CirCularQueue -> JsonString
     */
    @TypeConverter
    fun toJsonString(circularQueue: CircularQueue) : String{
        val jsonObj = JSONObject()
        val jsonArray = JSONArray()
        while(true){
            val jsonObject = JSONObject()
            var data = circularQueue.pop()
            if(data=="fs"){
                break
            }
            jsonObject.put("bioData",circularQueue.pop())
            jsonObject.put("Time",System.currentTimeMillis().toString())
            jsonArray.put(jsonObject)
        }
        jsonObj.put("item",jsonArray)

//        val gson = Gson()
//        gson.toJson(jsonObj)

        return ""
    }


    /**
     * 로드
     * JsonString -> CircularQueue
     */
    @TypeConverter
    fun toCircularQueue(jsonString: String) : CircularQueue{

        return CircularQueue()
    }
}