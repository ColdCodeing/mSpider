package com.mm.spider

import com.mm.spider.component.AbstractSpider
import com.mm.spider.component.Request
import com.mm.spider.consts.*
import com.mm.spider.downloader.Downloader
import com.mm.spider.downloader.VertxDownloader
import com.mm.spider.pipeline.ConsolePipeline
import com.mm.spider.pipeline.Pipeline
import com.mm.spider.processor.PageProcessor
import com.mm.spider.queue.DefaultQueue
import com.mm.spider.utils.VertxUtils
import io.vertx.core.Future
import io.vertx.core.http.HttpMethod
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.launch


class Spider : com.mm.spider.component.AbstractSpider {
    constructor() {
    }

    constructor(downloader: Downloader, pipelines: List<Pipeline>, pageProcessor: PageProcessor) {
        this.downloader = downloader
        this.pipelines = pipelines
        this.pageProcessor = pageProcessor
    }

    override fun start(startFuture: Future<Void>?) {
        startFuture?.complete()
    }

    override fun init() : AbstractSpider {
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

    override fun run() {
        VertxUtils.vertx.deployVerticle(this, {
            for (i in 0 until concurrency) {
                nextEvent()
            }
        })
    }

    suspend fun doEvent() {
        val request = this.queue?.poll()
        if (request != null) {
            if (this.openProxy) {
                //get proxy
            }
            try {
                val page = this.downloader?.download(request)
                if (page?.downloadSuccess!!) {
                    if (page.statusCode == 200) {
                        pageProcessor?.process(page)
                        if (!page.resultItems.skip) {
                            pipelines.forEach({ it.process(page.resultItems) })
                        }
                        this.addRequests(page.targetRequests)
                        println("task queue remain: " + this.queue?.getLeftRequests())
                    }
                } else {
                    println("down load fail " + request.url)
                }
            } catch (e: Exception) {
                println(request.url)
            }
        }
        nextEvent()
    }


    fun nextEvent() {
        vertx.setTimer(sleepTime, {taskId ->
            launch(vertx.dispatcher()) {
                when(spiderStatus) {
                    SPIDER_STATUS_BUZYING -> doEvent()
                    SPIDER_STATUS_IDEL -> doEvent()
                    SPIDER_STATUS_ERROR -> {}
                    SPIDER_STATUS_STOPPED -> {}
                }
            }
        })
    }

    override fun pause() : AbstractSpider {
        this.spiderStatus = SPIDER_STATUS_PAUSE
        return this
    }

    override fun addUrls(urls: List<String>, httpMethod: HttpMethod?) : Spider {
        urls.forEach({this.addUrl(it, null)})
        return this
    }

    override fun addRequests(requests: List<Request>) : Spider {
        requests.forEach({ this.addRequest(it) })
        return this
    }
}