package com.mm.spider

import com.mm.spider.component.AbstractSpider

class SpiderEngine {
    var spiders: Map<String, AbstractSpider> = HashMap()

    constructor(spiders: Map<String, Spider>) {
        this.spiders = spiders
    }

    constructor() {
    }

    fun run() {
        this.spiders.forEach({
            it.value.run()
        })
    }

    fun stopSpider(name: String) {
        this.spiders.get(name)?.stopped()
    }

    fun stopAllSpider() {
        this.spiders.values.forEach({
            it.stopped()
        })
    }

    fun restartSpider(name: String) {
        this.spiders.get(name)?.restart()
    }

    fun addSpider(spider: AbstractSpider) : SpiderEngine {
        spider.spiderName?.let {(this.spiders as MutableMap).put(it, spider)}
        spider.spiderName?: (this.spiders as MutableMap).put(System.currentTimeMillis().toString(), spider)
        return this
    }

    fun addSpider(spider: AbstractSpider, name: String) : SpiderEngine {
        (this.spiders as MutableMap).put(name, spider)
        return this
    }

    fun httpd() {
        //TODO
    }
}