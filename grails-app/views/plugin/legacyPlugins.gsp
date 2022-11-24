<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <title>Legacy Grails Plugins</title>
</head>
<body class="grails3plugins">
<content tag="title">plugins</content>
<article>
    <div class="goldenratio">
        <div class="column">
            <div class="plugins-list">
                <p><g:message code="grails.plugin.portal.inactive.1.message" /></p>
                <p>
                    <g:message code="grails.plugin.portal.inactive.2.message" />
                    <a href="https://grails.org/support.html"><g:message code="grails.support.page.title" /></a>.
                </p>
            </div>
        </div>
        <div class="column">
            <g:render template="/templates/menu" model="[active:'legacy']"/>
            <g:render template="/plugin/latestPlugins"/>
            <g:render template="/plugin/topRatedPlugins"/>
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
