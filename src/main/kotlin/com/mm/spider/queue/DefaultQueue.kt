package com.mm.spider.queue

import com.mm.spider.component.Request
import com.mm.spider.queue.filter.BloomDuplicateFilter
import com.mm.spider.queue.filter.Filter
import com.mm.spider.queue.filter.HashDuplicateFilter
import java.util.*

class DefaultQueue() : Queue {

    var queue: java.util.Queue<Request> = LinkedList()
    var filter: Filter = HashDuplicateFilter()

    constructor(queue: java.util.Queue<Request>, filter: Filter) : this() {
        this.queue = queue
        this.filter = filter
    }

    override suspend fun push(request: Request) {
        if(!filter.isDuplicate(request)) {
            this.queue.offer(request)
        }
    }

    override suspend fun poll(): Request? {
        return queue.poll()
    }

    override fun getLeftRequests(): Int {
        return queue.size
    }

    override fun getTotalRequests(): Int {
        return this.filter.getTotalRequestsCount()
    }
}