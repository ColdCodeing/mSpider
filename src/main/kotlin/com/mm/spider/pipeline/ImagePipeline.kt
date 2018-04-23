package com.mm.spider.pipeline

import com.mm.spider.component.ResultItems
import com.mm.spider.utils.VertxUtils.Companion.vertx
import io.vertx.core.buffer.Buffer

class ImagePipeline : Pipeline(){
    override fun process(resultItems: ResultItems) {
        resultItems.fields.forEach({
            val imageName = it.key
            val imageBuffer = it.value as Buffer
            vertx.fileSystem().createFile("/home/mm/Pictures/test/$imageName", {
                vertx.fileSystem().writeFile("/home/mm/Pictures/test/$imageName", imageBuffer, { result ->
                    if (result.succeeded()) {
                    } else {
                        System.err.println("Oh oh ..." + result.cause())
                    }
                })
            })
        })
    }
}