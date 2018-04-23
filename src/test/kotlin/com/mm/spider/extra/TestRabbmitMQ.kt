package com.mm.spider.extra

import com.mm.spider.Spider
import com.mm.spider.pipeline.ImagePipeline
import com.mm.spider.processor.LuooMusicImageProcessor
import com.mm.spider.processor.LuooMusicPageProcessor
import com.mm.spider.queue.RabbitMqQueue
import com.mm.spider.utils.VertxUtils
import com.mm.spider.utils.VertxUtils.Companion.vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.rabbitmq.RabbitMQClient
import io.vertx.rabbitmq.RabbitMQOptions
import kotlinx.coroutines.experimental.launch
import io.vertx.groovy.ext.web.client.HttpResponse_GroovyExtension.body
import io.vertx.kotlin.core.json.get


class TestRabbmitMQ {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            //queue name <next.url.queue> next url
            //queue name <luomusic.image.queue>
            val rabbitMQOptions = RabbitMQOptions()
                    .setUser("guest")
                    .setPassword("guest")
                    .setHost("localhost")
                    .setPort(5672)
            val rabbitMQClient = RabbitMQClient.create(VertxUtils.vertx, rabbitMQOptions)
            val exchangeName = "spider.exchange"
            val nextUrlRoutingKey = "next.url"
            val nextUrlQueue = "next.url.queue"

            launch(VertxUtils.vertx.dispatcher()) {
                val pageQueue = "page.queue"
                val luoMusicImageQueue = "luomusic.image.queue"
                val pageRoutingKey = "page.routingkey"
                val imageRoutingKey = "image.routingkey"
                awaitResult<Void> {  rabbitMQClient.start(it) }
                awaitResult<Void> { rabbitMQClient.exchangeDeclare(exchangeName, "direct", false, true, it) }
                //url汇集队列
                awaitResult<JsonObject> { rabbitMQClient.queueDeclare(nextUrlQueue, true, false, true, it) }
                awaitResult<Void> { rabbitMQClient.queueBind(nextUrlQueue, exchangeName, nextUrlRoutingKey, it) }
                vertx.eventBus().consumer<JsonObject>("my.address", {
                    val json = it.body()
                    json.getString("body")?.let {
                        if (JsonObject(it).getBoolean("binaryContent")) {
                            rabbitMQClient.basicPublish(exchangeName, imageRoutingKey, JsonObject().put("body", it), {
                                if (it.failed()) {
                                    it.cause().printStackTrace()
                                }
                            })
                        } else {
                            rabbitMQClient.basicPublish(exchangeName, pageRoutingKey, JsonObject().put("body", it), {
                                if (it.failed()) {
                                    it.cause().printStackTrace()
                                }
                            })
                        }
                    }
                })
                awaitResult<Void> { rabbitMQClient.basicConsume(nextUrlQueue, "my.address", it) }

                //分组接收队列
                awaitResult<JsonObject> { rabbitMQClient.queueDeclare(pageQueue, true, false, true, it) }
                awaitResult<JsonObject> { rabbitMQClient.queueDeclare(luoMusicImageQueue, true, false, true, it) }
                awaitResult<Void> { rabbitMQClient.queueBind(pageQueue, exchangeName, pageRoutingKey, it) }
                awaitResult<Void> { rabbitMQClient.queueBind(luoMusicImageQueue, exchangeName, imageRoutingKey, it) }

                //页面解析的url汇集到中心，由中心处理完之后进行分发
                val urlQueue = RabbitMqQueue(rabbitMQClient, exchangeName, nextUrlRoutingKey, pageQueue)
                val imageQueue = RabbitMqQueue(rabbitMQClient, exchangeName, nextUrlRoutingKey, luoMusicImageQueue)

                Spider().addPageProcessor(LuooMusicImageProcessor())
                        .setPipeline(ImagePipeline())
                        .sleepTime(1)
                        .addQueue(imageQueue)
                        .run()
                Spider().addPageProcessor(LuooMusicImageProcessor())
                        .setPipeline(ImagePipeline())
                        .sleepTime(1)
                        .addQueue(imageQueue)
                        .run()
                Spider().addPageProcessor(LuooMusicPageProcessor())
                        .sleepTime(1)
                        .addQueue(urlQueue)
                        .addUrl("http://www.luoo.net/music", null)
                        .run()
            }
        }
    }

}