package com.mm.spider;

class ResultItems {
    var fields: MutableMap<String, Any> = HashMap()
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

    override fun toString(): String {
        return "ResultItems(fields=$fields, skip=$skip)"
    }
}
