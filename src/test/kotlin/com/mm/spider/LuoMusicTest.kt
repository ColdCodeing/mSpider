package com.mm.spider

import com.mm.spider.processor.LuooMusicProcessor
import com.mm.spider.queue.DefaultQueue
import com.mm.spider.queue.filter.HashDuplicateFilter
import java.util.*

fun main(args: Array<String>) {
    Spider().init()
            .addPageProcessor(LuooMusicProcessor())
            .addQueue(DefaultQueue(LinkedList(), HashDuplicateFilter()))
            .setConcurrency(10)
            .sleepTime(10)
            .addUrl("http://www.luoo.net/music", null)
            .run()
}