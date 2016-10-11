package com.github.sheehan

import spock.lang.Specification

class CompareSpec extends Specification {

    void 'it should add a new version if plugin not found'() {
        given:
        List<Map> oldJson = []
        List<Map> newJson = [
            [
                name: 'plugin1',
                latest_version: '1.0'
            ]
        ]

        when:
        Compare compare = new Compare(oldJson, newJson)

        then:
        compare.newVersions.size() == 1
        compare.newVersions[0].plugin.name == 'plugin1'
        compare.newVersions[0].version == '1.0'
    }

    void 'it should add a new versions if plugin found with older version'() {
        given:
        List<Map> oldJson = [
            [
                name: 'plugin1',
                latest_version: '1.0',
                latest_version_created: '2014-01-01',
                versions: [
                    '1.0'
                ]
            ]
        ]
        List<Map> newJson = [
            [
                name: 'plugin1',
                latest_version: '2.0',
                latest_version_created: '2015-01-01',
                versions: [
                    '1.0',
                    '1.1',
                    '2.0'
                ]
            ]
        ]

        when:
        Compare compare = new Compare(oldJson, newJson)

        then:
        compare.newVersions.size() == 2
        compare.newVersions[0].plugin.name == 'plugin1'
        compare.newVersions[0].version == '2.0'
        compare.newVersions[1].plugin.name == 'plugin1'
        compare.newVersions[1].version == '1.1'
    }
}
