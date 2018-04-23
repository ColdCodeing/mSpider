package com.mm.spider

import com.mm.spider.processor.GithubRepoPageProcessor
import com.mm.spider.processor.LuooMusicProcessor
import com.mm.spider.queue.DefaultQueue
import com.mm.spider.queue.filter.HashDuplicateFilter
import java.util.*

fun main(args: Array<String>) {

    SpiderEngine()
            .addSpider(Spider().addPageProcessor(LuooMusicProcessor()).sleepTime(1).addUrl("http://www.luoo.net/music", null ))
            .addSpider(Spider().addPageProcessor(LuooMusicProcessor()).sleepTime(1).addUrl("http://www.luoo.net/music", null ))
            .addSpider(Spider().addPageProcessor(LuooMusicProcessor()).sleepTime(1).addUrl("http://www.luoo.net/music", null ))
            .addSpider(Spider().addPageProcessor(LuooMusicProcessor()).sleepTime(1).addUrl("http://www.luoo.net/music", null ))
            .run()
}