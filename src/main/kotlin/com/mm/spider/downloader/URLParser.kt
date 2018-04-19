package com.mm.spider.downloader

import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset


class URLParser {
    lateinit var host: String
    var port: Int? = null
    var protocol: String? = null
    lateinit var params: LinkedHashMap<String, List<String>>
    var path: String? = null
    var userInfo: String? = null
    var query: String? = null
    var hasDomain: Boolean = true

    constructor(url: String) : this(url, "utf-8") {
    }

    constructor(url: String, charset: String) {
        if (!Charset.isSupported(charset)) {
            throw IllegalArgumentException("charset is not support: " + charset)
        }
        val u: URL
        if (url.matches(Regex("\\w+[:][/][/].*"))) {
            hasDomain = true
            u = URL(url)
        } else {
            hasDomain = false
            u = URL("downloader://dummy" + if (url.startsWith("/")) url else "/$url")
        }
        if (hasDomain) {
            this.protocol = u.protocol
            this.host = u.host
            this.port = if (u.port == -1) null else u.port
            this.path = u.path
            this.userInfo = u.userInfo
        } else {
            this.path = if (url.startsWith("/")) u.path else u.path.substring(1)
        }
        this.query = u.query
        this.params = parseQueryString(substringAfter(url, "?"))
    }

    fun createQueryString(charset: String): String {
        if (this.params.isEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        for (name in this.params.keys) {
            val values = this.params[name]
            for (value in values!!) {
                if (sb.length > 0) {
                    sb.append("&")
                }
                sb.append(name).append("=").append(encode(value, charset))
            }
        }
        return sb.toString()
    }

    private fun parseQueryString(query: String): LinkedHashMap<String, List<String>> {
        val params = LinkedHashMap<String, List<String>>()
        if (query.isNotBlank()) {
            return params
        }
        val items = query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (item in items) {
            val name = substringBefore(item, "=")
            val value = substringAfter(item, "=")
            val values = getOrCreate(params, name)
            values.add(value)
        }
        return params
    }

    private fun getOrCreate(map: MutableMap<String, List<String>>, name: String): MutableList<String> {
        var list: MutableList<String>? = map.get(name) as MutableList<String>?
        if (list == null) {
            list = ArrayList()
            map[name] = list
        }
        return list
    }

    private fun substringBefore(str: String, sep: String) : String {
        val index = str.indexOf(sep)
        return if (index == -1) "" else str.substring(0, index)
    }

    private fun substringAfter(str: String, sep: String): String {
        val index = str.indexOf(sep)
        return if (index == -1) "" else str.substring(index + 1)
    }

    private fun decode(value: String, charset: String) : String {
        return URLDecoder.decode(value, charset)
    }

    private fun encode(value: String, charset: String) : String {
        return URLEncoder.encode(value, charset)
    }

}
