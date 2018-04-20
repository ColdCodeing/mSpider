package com.mm.spider

import com.mm.spider.component.Page
import com.mm.spider.component.Request
import com.mm.spider.consts.*
import com.mm.spider.downloader.Downloader
import com.mm.spider.downloader.VertxDownloader
import com.mm.spider.pipeline.ConsolePipeline
import com.mm.spider.pipeline.Pipeline
import com.mm.spider.processor.PageProcessor
import com.mm.spider.queue.DefaultQueue
import com.mm.spider.utils.VertxUtils
import io.vertx.core.http.HttpMethod
import kotlinx.coroutines.experimental.delay
import java.util.*
import java.util.concurrent.TimeUnit


class Spider : com.mm.spider.component.AbstractSpider {
    constructor() {
    }

    constructor(downloader: Downloader, pipelines: List<Pipeline>, pageProcessor: PageProcessor) {
        this.downloader = downloader
        this.pipelines = pipelines
        this.pageProcessor = pageProcessor
    }

    override fun addUrls(urls: List<String>, httpMethod: HttpMethod?) : Spider {
        urls.forEach({this.addRequest(Request(it, httpMethod ?: HttpMethod.GET))})
        return this
    }

    override fun addRequests(requests: List<Request>) : Spider {
        requests.forEach({this.addRequest(it)})
        return this
    }

    override fun init() : Spider {
        if (downloader == null) {
            this.downloader = VertxDownloader(VertxUtils.vertx)
        }
        if (pipelines.isEmpty()) {
            (this.pipelines as MutableList).add(ConsolePipeline())
        }
        if (queue == null) {
            this.queue = DefaultQueue()
        }
        return this
    }

    override suspend fun run() {
        try {
            println("start run")
            while (this.spiderStatus != SPIDER_STATUS_STOPPED) {
                when (this.spiderStatus) {
                    SPIDER_STATUS_RUNNING -> running()
                    SPIDER_STATUS_PAUSE -> pause()
                    SPIDER_STATUS_ERROR -> reportError()
                    SPIDER_STATUS_IDEL -> idel()
                }
            }
        } finally {
            stop()
        }
    }

    suspend fun running() {
        val request = this.queue?.poll()
        if (request != null) {
            if (this.openProxy) {
                //get proxy
            }
            processRequest(request)
        }
    }

    suspend fun processRequest(request: Request) {
        val page = this.downloader?.download(request)
        if (page?.downloadSuccess!!) {
            onDownloadSuccess(request, page)
        } else {
            onDownloadFail(request)
        }
    }

    suspend fun onDownloadSuccess(request: Request, page: Page) {
        if (page.statusCode == 200) {
            pageProcessor?.process(page)
            if (!page.resultItems.skip) {
                pipelines.forEach({it.process(page.resultItems)})
            }
            this.addRequests(page.targetRequests)
            println("task queue remain: " + this.queue?.getLeftRequests())
        }
        sleep(sleepTime)
    }

    fun onDownloadFail(request: Request) {
        println("down load fail " + request.url)
    }

    //download result process


    //spider status process
    override fun pause() {
        //TODO
    }

    fun reportError() {
        //TODO
    }

    fun idel() {
        //TODO
    }

    override fun stop() {
        //TODO
    }

    suspend fun sleep(millisecond: Long) {
        delay(millisecond, TimeUnit.MILLISECONDS)
    }
}