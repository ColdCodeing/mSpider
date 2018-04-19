package com.mm.spider.proxy

import io.vertx.core.net.ProxyOptions

data class Proxy(var updateTime: Long = -1, var proxyOptions : ProxyOptions) {
}