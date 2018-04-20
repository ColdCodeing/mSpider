package com.mm.spider.queue.filter

import com.mm.spider.component.Request

interface Filter {
    fun isDuplicate(request: Request): Boolean
    fun getTotalRequestsCount(): Int
}