package com.mm.spider.processor;

import com.mm.spider.component.Page

class GithubRepoPageProcessor : PageProcessor(){
    override fun process(page: Page) {
        page.html?.let {
            page.addTargetRequests(it.links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all(), null)
            page.addTargetRequests(it.links().regex("(https://github\\.com/[\\w\\-])").all(), null)
            var author = page.url.regex("https://github\\.com/(\\w+)/.*").get()
            author?.let { page.putField("author", it) }
            page.putField("url", page.request.url)
            if (page.resultItems.get<String>("author").isNullOrBlank()){
                //skip this page
                page.resultItems.skip = true;
            }
        }
    }
}
