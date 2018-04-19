package com.mm.spider.http;

class ResultItems {
    var fields: MutableMap<String, Any> = HashMap()
    var request: Request? = null
    var skip: Boolean = false

    fun <T> get(key: String) : T? {
        if (fields.get(key) == null) {
            return null
        } else {
            return fields.get(key) as T
        }
    }

    fun <T> put(key: String, value: T) {
        fields.put(key, value!!)
    }
}
