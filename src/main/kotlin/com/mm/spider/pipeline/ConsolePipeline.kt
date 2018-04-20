package com.mm.spider.pipeline

import com.mm.spider.component.ResultItems

class ConsolePipeline :Pipeline() {
    override fun process(resultItems: ResultItems) {
        System.out.println("get page: ")
        resultItems.fields.forEach({
            System.out.println(it.key + ":\t" + it.value);
        })
    }
}