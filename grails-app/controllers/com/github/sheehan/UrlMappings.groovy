package com.github.sheehan

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'plugin', action: 'index')
        "/plugin/json"(controller: 'plugin', action: 'json')
        "/plugin/refresh"(controller: 'plugin', action: 'refresh')
        "/plugin/$pluginName"(controller: 'plugin', action: 'plugin')
        "/plugin/$ownerName/$pluginName"(controller: 'plugin', action: 'pluginWithOwner')
        "/q/$query"(controller: 'plugin', action: 'index')

        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
