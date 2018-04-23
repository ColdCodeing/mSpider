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



class LuooMusicImageProcessor : PageProcessor() {
    override fun process(page: Page) {
        if (page.request.binaryContent) {
            val imageName = page.request.url.substring(page.request.url.indexOf("!") - 17, page.request.url.indexOf("!"))
            page.resultItems.put(imageName, Buffer.buffer(page.bytes))
            page.resultItems.skip = false
        }
    }
}