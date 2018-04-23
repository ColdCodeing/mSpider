package com.mm.spider.component

import io.vertx.core.MultiMap
import io.vertx.core.http.HttpMethod
import io.vertx.core.net.ProxyOptions
import io.vertx.kotlin.core.json.JsonObject

class Request {
    lateinit var url: String
    var userAgent: String? = null
    lateinit var httpMethod: HttpMethod
    var proxy: ProxyOptions? = null
    var headers: Map<String, String>? = null
    var cookies: Map<String, String>? = null
    var body: Any? = null
    var contentType: String? = null
    var binaryContent = false
    var charset: String? = null
    var responseHeader: Boolean = true

    constructor() {

    }

    constructor(url: String, httpMethod: HttpMethod) {
        this.url = url
        this.httpMethod = httpMethod
    }

    override fun toString(): String {
        return "Request(url='$url', userAgent=$userAgent, httpMethod=$httpMethod, proxy=$proxy, headers=$headers, cookies=$cookies, body=$body, contentType=$contentType, binaryContent=$binaryContent, charset=$charset, responseHeader=$responseHeader)"
    }
}