package com.mm.spider.queue

import com.mm.spider.component.Request
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.rabbitmq.RabbitMQClient

class RabbitMqQueue : com.mm.spider.queue.Queue{

    lateinit var rabbitMQClient: RabbitMQClient
    lateinit var exchangeName: String
    lateinit var routingKey: String
    lateinit var queueName: String

    constructor(rabbitMQClient: RabbitMQClient, exchangeName: String, routingKey: String, queueName: String) {
        this.rabbitMQClient = rabbitMQClient
        this.exchangeName = exchangeName
        this.routingKey = routingKey
        this.queueName = queueName
    }

    override suspend fun push(request: Request) {
        println(JsonObject(Json.encode(request)))
        awaitResult < Void > {
            rabbitMQClient.basicPublish(this.exchangeName, this.routingKey, JsonObject().put("body", Json.encode(request)), it)
        }
    }

    override suspend fun poll(): Request? {
        val msg = awaitResult<JsonObject> {
            rabbitMQClient.basicGet(this.queueName, true, it)
        }
        var request: Request? = null
        msg.getString("body")?.let {
            request = Json.decodeValue(it, Request::class.java)
        }
        return request
    }

    override fun getLeftRequests(): Int {
        return 0
    }

    override fun getTotalRequests(): Int {
        return 0
    }
}
