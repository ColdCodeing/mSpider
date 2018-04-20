package com.mm.spider.queue

import com.mm.spider.component.Request

interface Queue {
    fun push(request: Request)
    fun poll(): Request
    fun getLeftRequests(): Int
    fun getTotalRequests(): Int
}