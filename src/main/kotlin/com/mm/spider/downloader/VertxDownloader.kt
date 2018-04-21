package com.mm.spider.downloader

import com.mm.spider.component.Page
import com.mm.spider.component.Request
import com.mm.spider.selector.Html
import com.mm.spider.selector.PlainText
import com.mm.spider.utils.detectCharset
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
import java.nio.charset.Charset
import java.io.IOException



class VertxDownloader : Downloader {

    lateinit var vertx: Vertx

    constructor(vertx: Vertx) {
        this.vertx = vertx
    }

    override suspend fun download(request: Request) : Page {
        var webClient: WebClient? = null
        try {

            val urlParser = URLParser(request.url)
            webClient = WebClient.create(vertx, initWebClientOptions(request))

            val path = if (!urlParser.query.isNullOrBlank()) {
                urlParser.path + "?" + urlParser.query
            } else {
                urlParser.path
            }
            val httpRequest: HttpRequest<Buffer> =
                    if (request.httpMethod == HttpMethod.POST) {
                        if ("https" == urlParser.protocol) {
                            webClient.post(443, urlParser.host, path).ssl(true)
                        } else {
                            webClient.post(request.url)
                        }
                    } else {
                        if ("https" == urlParser.protocol) {
                            webClient.get(443, urlParser.host, path).ssl(true)
                        } else {
                            webClient.getAbs(request.url)
                        }
                    }
            request.headers?.forEach({
                httpRequest.putHeader(it.key, it.value)
            })

            if (request.httpMethod == HttpMethod.POST) {
                if (request.body != null && !request.contentType.isNullOrBlank()) {
                    httpRequest.putHeader("Content-type", request.contentType)
                }
            }

            val httpResponse: HttpResponse<Buffer> =
                    if (request.httpMethod == HttpMethod.POST && request.body != null) {
                        if (request.body is JsonArray || request.body is JsonObject) {
                            awaitResult { httpRequest.sendJson(request.body, it) }
                        } else {
                            awaitResult { httpRequest.sendForm(request.body as MultiMap?, it) }
                        }
                    } else {
                        awaitResult { httpRequest.send(it) }
                    }
            return handleHttResponse(request, httpResponse, null)
        } finally {
            webClient?.close()
        }
    }

    fun handleHttResponse(request: Request, response: HttpResponse<Buffer>, charset: String?) : Page {
        var bytes = response.bodyAsBuffer().bytes
        var charset = charset
        val page = Page()
        page.bytes = bytes
        //非二进制需要根据编码转换为html
        if (!request.binaryContent) {
            if (charset == null) {
                charset = getHtmlCharset(response.getHeader("Content-Type"), bytes)
            }
            page.charset = charset
            page.rawText = String(bytes, Charset.forName(charset))
            page.rawText?.let { page.html = Html(it, request.url) }
        }
        //页面的base url
        page.url = PlainText(request.url)
        page.request = request
        page.statusCode = response.statusCode()
        page.downloadSuccess = true
        if (request.responseHeader) {
            page.headers = response.headers()
        }
        return page
    }

    private fun getHtmlCharset(contentType: String, contentBytes: ByteArray): String {
        var charset = detectCharset(contentType, contentBytes)
        if (charset == null) {
            charset = Charset.defaultCharset().name()
        }
        return charset!!
    }

    fun initWebClientOptions(request: Request) : WebClientOptions {
        var options = WebClientOptions()
        options.setKeepAlive(true).setReuseAddress(true).setFollowRedirects(true);
        options.userAgent = request.userAgent?:options.userAgent
        options.proxyOptions = request.proxy?: options.proxyOptions
        options.setConnectTimeout(10000)
        return options
    }
}