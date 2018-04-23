package com.mm.spider

import com.mm.spider.processor.LuooMusicProcessor
import com.mm.spider.queue.RabbitMqQueue
import com.mm.spider.utils.VertxUtils
import io.netty.handler.codec.http.HttpHeaders.setHost
import io.vertx.core.json.JsonObject
import io.vertx.rabbitmq.RabbitMQClient
import io.vertx.rabbitmq.RabbitMQOptions

fun main(args: Array<String>) {
    val rabbitMQOptions = RabbitMQOptions()
            .setUser("guest")
            .setPassword("guest")
            .setHost("localhost")
            .setPort(5672)
    val rabbitMQClient = RabbitMQClient.create(VertxUtils.vertx, rabbitMQOptions)
    val exchangeName = "spider.exchange"
    val routingKey = "spider.routing.key"
    val queueName = "spider.queue"
    rabbitMQClient.start({
        rabbitMQClient.exchangeDeclare(exchangeName, "direct", false, true, {
            rabbitMQClient.queueDeclare(queueName, true, false, true, {
                rabbitMQClient.queueBind(queueName, exchangeName, routingKey, {
                    val queue = RabbitMqQueue(rabbitMQClient, exchangeName, routingKey, queueName);
                    SpiderEngine()
                            .addSpider(Spider().addPageProcessor(LuooMusicProcessor()).sleepTime(1).addQueue(queue).addUrl("http://www.luoo.net/music", null))
                            .addSpider(Spider().addPageProcessor(LuooMusicProcessor()).sleepTime(1).addQueue(queue).addUrl("http://www.luoo.net/music", null))
                            .addSpider(Spider().addPageProcessor(LuooMusicProcessor()).sleepTime(1).addQueue(queue).addUrl("http://www.luoo.net/music", null))
                            .addSpider(Spider().addPageProcessor(LuooMusicProcessor()).sleepTime(1).addQueue(queue).addUrl("http://www.luoo.net/music", null))
                            .run()
                })
            })
        })
    })

}