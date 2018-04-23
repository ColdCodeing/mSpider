package com.mm.spider.processor

import com.mm.spider.component.Page
import com.mm.spider.component.Request
import io.vertx.core.http.HttpMethod

class LuooMusicPageProcessor : PageProcessor() {
    override fun process(page: Page) {
        page.html?.let {
            if (it.css("div.vol-list").match()) {
                for (i in 0 until 10) {
                    val item = it.css("div.item:eq($i)")
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
                val next = it.css("a.next", "href").get()
                next?.let { page.addTargetRequest(next, null) }
            }
        }
        page.resultItems.skip = false

    }
}