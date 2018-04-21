package com.mm.spider.queue.filter;

import com.mm.spider.component.Request

class HashDuplicateFilter : Filter{
    val urlSet: HashSet<String> = HashSet()

    override fun isDuplicate(request: Request): Boolean {
        return !urlSet.add(request.url)
    }

    override fun getTotalRequestsCount(): Int {
        return urlSet.size
    }
}
