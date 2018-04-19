package com.mm.spider.processor;

import com.mm.spider.Page

class GithubRepoPageProcessor : PageProcessor(){
    override fun process(page: Page) {
        page.addTargetRequests(page.html.links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all(), null)
        page.addTargetRequests(page.html.links().regex("(https://github\\.com/[\\w\\-])").all(), null)
        page.putField("author", page.url.regex("https://github\\.com/(\\w+)/.*").get() as Any);
        page.putField("url", page.request.url)
        if (page.resultItems.get<String>("author").isNullOrBlank()){
            //skip this page
            page.resultItems.skip = true;
        }
    }
}
