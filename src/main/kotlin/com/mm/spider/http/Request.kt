package com.mm.spider.http

import com.mm.spider.utils.URLParser
import io.netty.handler.codec.http.HttpContent
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.core.net.ProxyOptions
import kotlin.collections.HashMap

class Request {
    lateinit var url: String
    var userAgent: String? = null
    lateinit var httpMethod: HttpMethod
    var proxy: ProxyOptions? = null
    var sleepTime: Long = 0L
    var urlParser: URLParser
    var headers: Map<String, String>? = null
    var params: Map<String, String>? = null
    var body: Any? = null
    var contentType: String? = null

    constructor(url: String, httpMethod: HttpMethod, sleepTime: Long, urlParser: URLParser) {
        this.url = url
        this.httpMethod = httpMethod
        this.sleepTime = sleepTime
        this.urlParser = urlParser
    }
}