package com.mm.spider.http

import io.vertx.core.http.HttpMethod
import io.vertx.core.net.ProxyOptions

class Request {
    lateinit var url: String
    var userAgent: String? = null
    lateinit var httpMethod: HttpMethod
    var proxy: ProxyOptions? = null
    var headers: Map<String, String>? = null
    var cookies: Map<String, String>? = null
    var body: Any? = null
    var contentType: String? = null

    constructor(url: String) {
        this.url = url
    }
}