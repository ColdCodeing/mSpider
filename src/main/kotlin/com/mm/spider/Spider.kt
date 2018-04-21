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
import com.mm.spider.queue.Queue
import com.mm.spider.utils.VertxUtils
import io.vertx.core.Future
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit


class Spider : com.mm.spider.component.AbstractSpider {

    constructor() {
        this.spiderName = System.currentTimeMillis().toString()
        if (downloader == null) {
            this.downloader = VertxDownloader(VertxUtils.vertx)
        }
        if (pipelines.isEmpty()) {
            (this.pipelines as MutableList).add(ConsolePipeline())
        }
        if (queue == null) {
            this.queue = DefaultQueue()
        }
    }

    constructor(url: String) : this(){
        this.addUrl(url, null)
    }

    constructor(downloader: Downloader, pipelines: List<Pipeline>, queue: Queue) {
        this.downloader = downloader
        this.pipelines = pipelines
        this.queue = queue
    }

    override fun start(startFuture: Future<Void>?) {
        startFuture?.complete()
    }

    override fun run() {
        VertxUtils.vertx.deployVerticle(this, {
            nextEvent()
        })
    }

    suspend fun doCrawl() {
        if (this.queue?.getLeftRequests() == 0) {
            this.spiderStatus = SPIDER_STATUS_IDEL
        } else {
            this.spiderStatus = SPIDER_STATUS_BUZYING
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
                        }
                    } else {
                        throw RuntimeException("down load fail " + request.url)
                    }
                } catch (e: Exception) {
                    println("down load fail " + request.url)
                    e.printStackTrace()
                }
            }
        }
    }

    //循环产生事件,推动爬虫的移动
    fun nextEvent() {
        vertx.setTimer(sleepTime, {taskId ->
            launch(vertx.dispatcher()) {
                when(spiderStatus) {
                    SPIDER_STATUS_BUZYING -> {
                        doCrawl()
                        nextEvent()
                    }
                    SPIDER_STATUS_IDEL -> {
                        delay(2000, TimeUnit.SECONDS)   //主动释放执行权2秒钟
                        doCrawl()
                        nextEvent()
                    }
                    SPIDER_STATUS_ERROR -> {

                    }
                    SPIDER_STATUS_PAUSE, SPIDER_STATUS_STOPPED -> {
                        //啥也不干就行了,不会有Timer再产生事件,爬虫也就停止了
                    }
                }
            }
        })
    }

    override fun pause() : AbstractSpider {
        this.spiderStatus = SPIDER_STATUS_PAUSE
        return this
    }

    override fun stopped(): AbstractSpider {
        this.spiderStatus = SPIDER_STATUS_STOPPED
        return this
    }

    override fun restart(): AbstractSpider {
        this.spiderStatus = SPIDER_STATUS_BUZYING
        nextEvent()
        return this
    }

    override fun addRequest(request: Request): AbstractSpider {
        queue?.push(request)
        return this
    }
}