package com.mm.spider.selector

interface Selectable {
    fun css(selector: String) : Selectable
    fun css(selector: String, attrname: String) : Selectable
    fun `$`(selector: String) : Selectable
    fun `$`(selector: String, attrname: String) : Selectable
    fun links() : Selectable
    fun regex(regex: String) : Selectable
    fun regex(regex: String, group: Int) : Selectable
    fun replace(regex: String, replacement: String) : Selectable
    fun get() : String?
    fun match() : Boolean
    fun all() : List<String>
    fun jsonPath(jsonPath: String) : Selectable
    fun select(selector: Selector) : Selectable
    fun selectList(selector: Selector) : Selectable
    fun nodes() : List<Selectable>
}