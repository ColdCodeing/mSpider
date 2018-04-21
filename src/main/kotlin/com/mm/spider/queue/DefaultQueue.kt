package com.mm.spider.queue

import com.mm.spider.component.Request
import com.mm.spider.queue.filter.BloomDuplicateFilter
import com.mm.spider.queue.filter.Filter
import com.mm.spider.queue.filter.HashDuplicateFilter
import java.util.*

class DefaultQueue() : Queue() {

    override var queue: java.util.Queue<Request> = LinkedList()
    override var filter: Filter = HashDuplicateFilter()

    constructor(queue: java.util.Queue<Request>, filter: Filter) : this() {
        this.queue = queue
        this.filter = filter
    }

    fun pushWhenNoDuplicate(request: Request) {
        if (!this.filter.isDuplicate(request)) {
            queue.offer(request)
        }
    }

    override fun poll(): Request? {
        return queue.poll()
    }

    override fun getLeftRequests(): Int {
        return queue.size
    }

    override fun getTotalRequests(): Int {
        return this.filter.getTotalRequestsCount()
    }

    override fun push(request: Request) {
        pushWhenNoDuplicate(request)
    }

}