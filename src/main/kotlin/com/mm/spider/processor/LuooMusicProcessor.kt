package com.mm.spider.processor

import com.mm.spider.component.Page
import com.mm.spider.component.Request
import com.mm.spider.utils.VertxUtils
import com.mm.spider.utils.VertxUtils.Companion.vertx
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import javassist.util.proxy.FactoryHelper.writeFile
import javassist.util.proxy.FactoryHelper.writeFile



class LuooMusicProcessor : PageProcessor() {
    override fun process(page: Page) {
        if (page.request.binaryContent) {
            val imageName = page.request.url.substring(page.request.url.indexOf("!") - 17, page.request.url.indexOf("!"))
            vertx.fileSystem().createFile("/home/panmin/图片/$imageName", {
                        vertx.fileSystem().writeFile("/home/panmin/图片/$imageName", Buffer.buffer(page.bytes), { result ->
                            if (result.succeeded()) {
                                println("File written")
                            } else {
                                System.err.println("Oh oh ..." + result.cause())
                            }
                        })
                    })
        }
        if (page.html.css("div.vol-list").match()) {
            for (i in 0 until 10) {
                val item = page.html.css("div.item:eq($i)")
                val src = item.`$`("a.cover-wrapper", "href").get()
                val name = item.`$`("a.cover-wrapper", "title").get()
                val image = item.`$`("img", "src").get()
                name?.let {
                    page.putField(it, src)
                }
                image?.let {
                    val request = Request(it, HttpMethod.GET)
                    request.binaryContent = true
                    page.addTargetRequest(request)
                }
            }
            val next = page.html.css("a.next", "href").get()
            next?.let { page.addTargetRequest(next, null) }
        }
        page.resultItems.skip = false

    }
}