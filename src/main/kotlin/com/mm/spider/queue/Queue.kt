package com.mm.spider.queue

import com.mm.spider.component.Request
import com.mm.spider.queue.filter.Filter

abstract class Queue {
    abstract var queue: java.util.Queue<Request>
    abstract var filter: Filter
    abstract fun push(request: Request)
    abstract fun poll(): Request?
    abstract fun getLeftRequests(): Int
    abstract fun getTotalRequests(): Int
}