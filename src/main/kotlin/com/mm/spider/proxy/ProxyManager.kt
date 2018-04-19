package com.mm.spider.proxy

import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class ProxyManager {
    val proxyList : List<Proxy> = LinkedList()
    var index: AtomicInteger = AtomicInteger()

    fun getProxy() : Proxy? {
        var result: Proxy? = null
        if (proxyList.size > 0) {
            if (index.get() > proxyList.size - 1) {
                index.set(0)
            }
            result = proxyList.get(index.get())
            index.incrementAndGet()
        }
        return result
    }

    fun addProxy(proxy: Proxy) {
        (proxyList as MutableList).add(proxy)
    }
}