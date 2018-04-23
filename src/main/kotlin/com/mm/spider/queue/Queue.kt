package com.mm.spider.queue

import com.mm.spider.component.Request
import com.mm.spider.queue.filter.Filter

interface Queue {
    abstract suspend fun push(request: Request)
    abstract suspend fun poll(): Request?
    abstract fun getLeftRequests(): Int
    abstract fun getTotalRequests(): Int
}