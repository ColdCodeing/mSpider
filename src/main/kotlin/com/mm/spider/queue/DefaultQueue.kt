package com.mm.spider.queue

import com.mm.spider.component.Request
import java.util.*
import java.util.Queue

class DefaultQueue : AbstractQueue() {
    var queue: Queue<Request> = LinkedList()

    override fun pushWhenNoDuplicate(request: Request) {
        if (this.filter.isDuplicate(request)) {
            queue.offer(request)
        }
    }

    override fun poll(): Request {
        return queue.poll()
    }

    override fun getLeftRequests(): Int {
        return queue.size
    }

    override fun getTotalRequests(): Int {
        return this.filter.getTotalRequestsCount()
    }
}