package com.mm.spider.processor

import com.mm.spider.Page

abstract class PageProcessor {
    abstract fun process(page: Page)

    fun process(page: Page, processer: (page: Page) -> Unit) {
        processer(page)
    }
}