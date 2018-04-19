package com.mm.spider.selector

import com.jayway.jsonpath.JsonPath
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import java.awt.SystemColor.text

class JsonPathSelector(var jsonPathStr: String) : Selector {
    var jsonPath: JsonPath

    override fun select(html: String): String? {
        val obj: Any? = jsonPath.read(html)
        obj?: return null
        if (obj is List<*>) {
            val list = obj as List<Any>
            toString(list.iterator().next())
        }
        return obj.toString()
    }

    override fun selectList(html: String): List<String> {
        var list = ArrayList<String> ()
        var obj: Any? = jsonPath.read(html)
        obj?: return list
        if (obj is List<*>) {
            val items = obj as List<Any>
            list = items.map { toString(it) }.toList<String>() as ArrayList<String>
        } else {
            list.add(toString(obj))
        }
        return list
    }

    private fun toString(obj: Any): String {
        return if (obj is Map<*, *>) {
           Json.encodePrettily(obj)
        } else {
            obj.toString()
        }
    }

    init {
        this.jsonPath = JsonPath.compile(jsonPathStr)
    }
}