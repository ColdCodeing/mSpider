package com.mm.spider

import com.mm.spider.processor.GithubRepoPageProcessor
import com.mm.spider.processor.LuooMusicProcessor
import com.mm.spider.queue.DefaultQueue
import com.mm.spider.queue.filter.HashDuplicateFilter
import java.util.*

fun main(args: Array<String>) {

    SpiderEngine()
            .addSpider(Spider("http://www.luoo.net/music").addPageProcessor(LuooMusicProcessor()).sleepTime(1))
            .addSpider(Spider("http://www.luoo.net/music").addPageProcessor(LuooMusicProcessor()).sleepTime(1))
            .addSpider(Spider("http://www.luoo.net/music").addPageProcessor(LuooMusicProcessor()).sleepTime(1))
            .addSpider(Spider("http://www.luoo.net/music").addPageProcessor(LuooMusicProcessor()).sleepTime(1))
            .run()
}