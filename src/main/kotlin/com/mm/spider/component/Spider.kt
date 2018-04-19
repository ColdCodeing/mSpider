package com.mm.spider.component

import com.mm.spider.Request
import com.mm.spider.Spider
import io.vertx.core.http.HttpMethod

interface Spider {
    fun addUrl(url: String, httpMethod: HttpMethod?) : Spider
    fun addUrls(urls: List<String>, httpMethod: HttpMethod?) : Spider
    fun addRequests(requests: List<Request>) : Spider
    fun addRequest(request: Request) : Spider
    fun init() : Spider
    suspend fun run()
    fun stop()
    fun pause()
}