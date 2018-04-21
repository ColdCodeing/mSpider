package com.mm.spider.component

import com.mm.spider.selector.Html
import com.mm.spider.selector.Json
import com.mm.spider.selector.Selectable
import com.mm.spider.utils.HttpConstant
import com.mm.spider.utils.canonicalizeUrl
import io.vertx.core.MultiMap
import io.vertx.core.http.HttpMethod
import java.util.*

class Page {
    lateinit var request: Request
    var resultItems: ResultItems = ResultItems()
    var html: Html? = null
    var Json: Json? = null
    var rawText: String? = null
    lateinit var url: Selectable
    var headers: MultiMap? = null
    var statusCode: Int = HttpConstant.Companion.StatusCode.CODE_200
    var downloadSuccess = true
    var bytes: ByteArray? = null
    var targetRequests: List<Request> = ArrayList<Request>()
    var charset: String? = null

    companion object {
        fun fail(): Page {
            val page = Page()
            page.downloadSuccess = false
            return page
        }
    }

    fun putField(key: String, value: Any?) {
        resultItems.put(key, value)
    }

    fun addTargetRequests(requestStrs: List<String>, httpMethod: HttpMethod?) {
        requestStrs.forEach({
            if (!(it.isBlank() && it == "#" && it.startsWith("javascript:"))) {
                var url = canonicalizeUrl(it, url.toString());
                (targetRequests as MutableList).add(Request(url, httpMethod ?: HttpMethod.GET));
            }
        })
    }

    fun addTargetRequest(requestStr: String, httpMethod: HttpMethod?) {
        if (requestStr.isBlank() || requestStr.equals("#")) {
            return
        }
        var url = canonicalizeUrl(requestStr, url.toString())
        (targetRequests as MutableList).add(Request(url, httpMethod ?: HttpMethod.GET));
    }

    fun addTargetRequest(request: Request) {
        (targetRequests as MutableList).add(request)
    }

    override fun toString(): String {
        return "Page(request=$request, resultItems=$resultItems, html=$html, Json=$Json, rawText=$rawText, url=$url, headers=$headers, statusCode=$statusCode, downloadSuccess=$downloadSuccess, bytes=${Arrays.toString(bytes)}, targetRequests=$targetRequests, charset=$charset)"
    }

}