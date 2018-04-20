package com.mm.spider.queue

import com.mm.spider.component.Request
import com.mm.spider.queue.filter.BloomDuplicateFilter
import com.mm.spider.queue.filter.Filter
import jdk.nashorn.internal.runtime.regexp.joni.Config.log


abstract class AbstractQueue : Queue{
    var filter: Filter = BloomDuplicateFilter()

    fun setFilter(filter: Filter): AbstractQueue {
        this.filter = filter
        return this
    }

    override fun push(request: Request) {
        if (!filter.isDuplicate(request)) {
            pushWhenNoDuplicate(request)
        }
    }

    protected abstract fun pushWhenNoDuplicate(request: Request)
}