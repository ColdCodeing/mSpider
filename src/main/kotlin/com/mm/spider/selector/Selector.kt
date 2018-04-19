package com.mm.spider.selector

interface Selector {
    fun select(html: String) : String?
    fun selectList(html: String) : List<String>
}