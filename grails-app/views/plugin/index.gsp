<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
</head>
<body class="grails3plugins">
<content tag="title">plugins</content>
<article>
    <div class="goldenratio">
        <div class="column">
            <div class="plugins-list">
                <g:form action="index" controller="plugin" name="searchform" method="GET">
                    <input type="text" class="search-input" name="query" value="${query}"/>
                    <g:submitButton name="submit" class="search-submit" value="${g.message(code:'plugins.search.submit', default: 'Search')}"/>
                </g:form>
                <g:if test="${pluginTotal != pluginList.size()}">
                    <h4>
                        <g:message code="plugins.showing.of" args="${[pluginList.size(), pluginTotal]}"/>
                        <span>&nbsp;</span>
                        <g:link controller="plugin" action="index"><g:message code="plugins.showing.all" default="Show All"/></g:link>
                    </h4>

                </g:if>
                <g:else>
                    <h4><g:message code="plugins.showing" args="${pluginList.size()}"/></h4>
                </g:else>

                <g:each var="plugin" in="${pluginList}">
                    <li class="plugin">
                        <g:render template="plugin" model="[plugin: plugin]"/>
                    </li>
                </g:each>
            </div>
        </div>
        <div class="column">
            <g:render template="/templates/menu"/>
            <div class="latestguides">
                <h3 class="columnheader">Latest Plugins</h3>
                <ul>
                <g:each var="plugin" in="${latestPlugins}">
                    <li>
                    <b>${plugin.bintrayPackage?.name} </b>
                    <span><grailsplugins:lastUpdated plugin="${plugin}"/></span>
                        <g:render template="pluginlink" model="[plugin: plugin, text: 'Read More']"/>
                    </li>
                </g:each>
                </ul>
            </div>
            <div class="latestguides">
                <h3 class="columnheader">Top Rated Plugins</h3>
                <ul>
                    <g:each var="plugin" in="${topRatedPlugins}">
                        <li>
                            <b>${plugin.bintrayPackage?.name} </b>
                            <span><asset:image src="small_githubstar.svg" alt="Github"/> ${plugin.githubRepository?.stargazersCount}</span>
                            <g:render template="pluginlink" model="[plugin: plugin, text: 'Read More']"/>
                        </li>
                    </g:each>
                </ul>
            </div>
            <g:if test="${!query}">
                <grailsplugins:labelsTagCloud pluginList="${pluginList}"/>
                <grailsplugins:ownersTagCloud pluginList="${pluginList}"/>
                <g:render template="twitterTimeline"/>
            </g:if>
        </div>
    </div>
</article>
    </body>
</html>