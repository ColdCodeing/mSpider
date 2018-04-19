package com.mm.spider.http

import com.mm.spider.selector.Html
import com.mm.spider.selector.Json
import com.mm.spider.selector.Selectable
import com.mm.spider.utils.HttpConstant
import com.mm.spider.utils.UrlUtils
import java.util.ArrayList

class Page {
    var request: Request? = null
    var resultItems: ResultItems? = null
    var html: Html? = null
    var Json: Json? = null
    var rawText: String? = null
    var url: Selectable? = null
    var headers: Map<String, List<String>>? = null
    var statusCode = HttpConstant.Companion.StatusCode
    var downloadSuccess = true
    var bytes: ByteArray? = null
    var targetRequests: List<Request> = ArrayList<Request>()
    var charset: String? = null

    fun fail(): Page {
        val page = Page()
        page.downloadSuccess = false
        return page
    }

    fun putField(key: String, value: Any) {
        resultItems?.put(key, value)
    }

    fun addTargetRequests(requestStrs: List<String>) {
        requestStrs.forEach({
            if (!(it.isBlank() && it == "#" && it.startsWith("javascript:"))) {
                var url = UrlUtils.canonicalizeUrl(it, url.toString());
                targetRequests.toMutableList().add(Request(url));
            }
        })
    }

    fun addTargetRequest(requestStr: String) {
        if (requestStr.isBlank() || requestStr.equals("#")) {
            return
        }
        var url = UrlUtils.canonicalizeUrl(requestStr, url.toString())
        targetRequests.toMutableList().add(Request(url));
    }

    fun addTargetRequest(request: Request) {
        targetRequests.toMutableList().add(request)
    }

    fun setRequestAndResultItem(request: Request) {
        this.request = request
        this.resultItems?.request = request
    }

    fun setRowText(rowText: String) : Page {
        this.rawText = rowText
        return this
    }
}