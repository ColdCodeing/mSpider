package com.mm.spider.downloader

import com.mm.spider.component.Page
import com.mm.spider.component.Request

abstract class Downloader {
    abstract suspend fun download(request: Request) : Page

    suspend fun download(request: Request, onSuccess: (reques: Request) -> Unit, onError: (request: Request) -> Unit) : Page {
        var page: Page
        try {
            page = download(request)
            onSuccess(request)
        } catch (e: Exception) {
            page = Page.Companion.fail()
            onError(request)
        }
        return page
    }
}