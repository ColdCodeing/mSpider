package com.mm.spider.http

import io.vertx.core.MultiMap
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.kotlin.coroutines.awaitResult

class Downloader {

    lateinit var vertx: Vertx

    constructor(vertx: Vertx) {
        this.vertx = vertx
    }

    suspend fun download(request: Request) : HttpResponse<Buffer> {
        var webClient = WebClient.create(vertx, initWebClientOptions(request))

        val path = if (!request.urlParser.query.isNullOrBlank()) {
                                request.urlParser.path+"?"+request.urlParser.query
                            } else {
                                request.urlParser.path
                            }
        var httpRequest: HttpRequest<Buffer> =
            if (request.httpMethod == HttpMethod.POST) {
                if ("https" == request.urlParser.protocol) {
                    webClient.post(443, request.urlParser.host, path).ssl(true)
                } else {
                    webClient.post(request.url)
                }
            } else {
                if ("https" == request.urlParser.protocol) {
                    webClient.get(443, request.urlParser.host, path).ssl(true)
                } else {
                    webClient.getAbs(request.url)
                }
            }

        if (request.headers!!.isNotEmpty()) {
            for (entry in request.headers!!.entries) {
                httpRequest.putHeader(entry.key, entry.value)
            }
        }

        if (request.httpMethod == HttpMethod.POST) {
            if (request.body != null && !request.contentType.isNullOrBlank()) {
                httpRequest.putHeader("Content-type",request.contentType)
            }
        }

        var httpResponse: HttpResponse<Buffer> =
            if (request.httpMethod == HttpMethod.POST && request.body != null) {
                if (request.body is JsonArray || request.body is JsonObject) {
                    awaitResult { httpRequest.sendJson(request.body, it) }
                } else {
                    awaitResult { httpRequest.sendForm(request.body as MultiMap?, it) }
                }
            } else {
                awaitResult { httpRequest.send(it) }
            }
        webClient.close();
        return httpResponse
    }

    fun initWebClientOptions(request: Request) : WebClientOptions {
        var options = WebClientOptions()
        options.setKeepAlive(true).setReuseAddress(true).setFollowRedirects(true);
        options.userAgent = request.userAgent?:options.userAgent
        options.proxyOptions = request.proxy?: options.proxyOptions
        return options
    }
}