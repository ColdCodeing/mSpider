package com.mm.spider.component

import com.mm.spider.Spider
import com.mm.spider.consts.SPIDER_STATUS_IDEL
import com.mm.spider.downloader.Downloader
import com.mm.spider.pipeline.Pipeline
import com.mm.spider.processor.PageProcessor
import com.mm.spider.queue.Queue
import io.vertx.core.http.HttpMethod
import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.util.*
import kotlin.collections.ArrayList

abstract class AbstractSpider : CoroutineVerticle() {

    var spiderName: String? = null
    var downloader: Downloader? = null
    var pipelines: List<Pipeline> = ArrayList()
    var pageProcessor: PageProcessor? = null
    var queue: Queue? = null
    var sleepTime: Long = 100
    var openProxy: Boolean = false
    var spiderStatus: Int = 0

    abstract fun run()
    abstract fun pause() : AbstractSpider
    abstract fun stopped() : AbstractSpider
    abstract fun restart() : AbstractSpider
    abstract fun addRequest(request: Request) : AbstractSpider

    fun addUrl(url: String, httpMethod: HttpMethod?) : AbstractSpider {
        this.addRequest(Request(url, httpMethod ?: HttpMethod.GET))
        return this
    }

    fun addPipeline(pipeline: Pipeline) : AbstractSpider {
        (this.pipelines as MutableList).add(pipeline)
        return this
    }

    fun setPipeline(pipeline: Pipeline) : AbstractSpider {
        clearPipeline()
        addPipeline(pipeline)
        return this
    }

    fun clearPipeline() : AbstractSpider {
        this.pipelines = ArrayList()
        return this
    }

    fun addDownloader(downloader: Downloader): AbstractSpider {
        this.downloader = downloader
        return this
    }

    fun addPageProcessor(pageProcessor: PageProcessor) : AbstractSpider {
        this.pageProcessor = pageProcessor
        return this
    }

    fun  addQueue(queue: Queue) : AbstractSpider {
        this.queue = queue
        return this
    }

    fun openProxy(openProxy: Boolean) : AbstractSpider {
        this.openProxy = openProxy
        return this
    }

    fun sleepTime(time: Long) : AbstractSpider {
        this.sleepTime = time
        return this
    }

    fun isIdel() : Boolean {
        return this.spiderStatus == SPIDER_STATUS_IDEL
    }

    fun addUrls(urls: List<String>, httpMethod: HttpMethod?) : AbstractSpider {
        urls.forEach({this.addUrl(it, null)})
        return this
    }

    fun addRequests(requests: List<Request>) : AbstractSpider {
        requests.forEach({ this.addRequest(it) })
        return this
    }
}