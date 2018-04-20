package com.mm.spider

import com.mm.spider.processor.GithubRepoPageProcessor
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) =  runBlocking<Unit>{
    Spider().init()
            .addUrl("https://github.com/ColdCodeing/kotlin-vertx-demo", null)
            .addPageProcessor(GithubRepoPageProcessor())
            .sleepTime(10)
            .run()
}