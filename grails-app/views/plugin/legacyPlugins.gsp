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

                <p>The Grails 1 & 2 plugin portal is no longer active.  We are not accepting submissions for new plugins for Grails 1 & 2.</p>
                <p>Documentation for Grails 1 & 2 plugins can be found on the website or source code repository of each plugin.  If you require additional assitance, please visit the Grails <a href="https://grails.org/support.html">support page</a>.</p>

            </div>
        </div>
        <div class="column">
            <g:render template="/templates/menu" model="[active:'legacy']"/>
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
