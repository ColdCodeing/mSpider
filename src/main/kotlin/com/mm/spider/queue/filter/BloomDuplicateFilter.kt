package com.mm.spider.queue.filter

import com.google.common.hash.BloomFilter
import com.mm.spider.component.Request
import java.util.concurrent.atomic.AtomicInteger
import java.nio.charset.Charset
import com.google.common.hash.Funnels

class BloomDuplicateFilter() : Filter {

    var expectedInsertions: Int = 0
    var fpp: Double = 0.1
    var counter: AtomicInteger = AtomicInteger()
    var bloomFilter: BloomFilter<CharSequence> = rebuildBloomFilter()


    constructor(expectedInsertions: Int) : this(expectedInsertions, 0.01){
    }

    constructor(expectedInsertions: Int, fpp: Double) : this() {
        this.expectedInsertions = expectedInsertions
        this.fpp = fpp
        this.bloomFilter = rebuildBloomFilter()
    }

    protected fun rebuildBloomFilter(): BloomFilter<CharSequence> {
        counter = AtomicInteger(0)
        return BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), expectedInsertions, fpp)
    }

    override fun isDuplicate(request: Request): Boolean {
        val isDuplicate = bloomFilter.mightContain(request.url)
        if (!isDuplicate) {
            bloomFilter.put(request.url)
            counter.incrementAndGet()
        }
        return isDuplicate
    }

    override fun getTotalRequestsCount(): Int {
        return counter.get()
    }
}