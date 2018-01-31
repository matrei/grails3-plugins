package com.bintray

import grails.testing.services.ServiceUnitTest
import com.bintray.BintrayService
import spock.lang.Specification
import spock.lang.Unroll

class BintrayServiceSpec extends Specification implements ServiceUnitTest<BintrayService> {

    @Unroll
    def "Total #total Start: #start end: #end expects #expected"(Integer total, Integer start, Integer end, List<Integer> expected) {
        expect:
        expected == service.expectedExtraStarPositions(total, start, end)

        where:
        total  | start | end  || expected
        235    | 0     | 99   || [100, 200]
        135    | 0     | 99   || [100]
        80     | 0     | 87   || []
        null   | 0     | 99   || []
        235    | null  | 99   || []
        235    | 0     | null || []
    }
}
