<!DOCTYPE html>
<html>
<head>
    <title>${plugin.bintrayPackage?.name} | Plugins | Grails Framework</title>
    <meta name="layout" content="main" />
</head>
<body class="grails3plugins">

<div class="headerbar">
    <div class="content">
        <h1>${plugin.bintrayPackage?.name}</h1>
    </div>
</div>


<content tag="title">plugin</content>
<article class="post" style="padding: 4%;">
    <p>${plugin.bintrayPackage?.desc}</p>
    <p>
        <b><g:message code="plugin.owner" default="Owner"/>:</b> <g:link absolute="true" uri="/q/owner:${plugin.bintrayPackage?.owner}">${plugin.bintrayPackage?.owner}</g:link>
        <span> | </span><b>${plugin.bintrayPackage?.latestVersion}</b>
        <span> | </span><grailsplugins:lastUpdated plugin="${plugin}"/>
        <span> | </span><a href="${grailsplugins.bintrayLink(plugin: plugin)}">Package</a>
        <g:if test="${plugin.bintrayPackage?.issueTrackerUrl}">
            <span> | </span> <a href="${plugin.bintrayPackage?.issueTrackerUrl}">Issues</a>
        </g:if>
        <g:if test="${plugin.bintrayPackage?.vcsUrl}">
            <span> | </span> <a href="${plugin.bintrayPackage?.vcsUrl}">Source</a>
        </g:if>
        <g:if test="${plugin.documentationUrl}">
            <span> | </span><a href="${plugin.documentationUrl}">Documentation</a>
        </g:if>
        <g:if test="${plugin.bintrayPackage?.licenses}">
            <span> | </span><b><g:message code="plugin.license" default="License"/>:</b> <a href="https://opensource.org/licenses/${plugin.bintrayPackage?.licenses[0]}">${plugin.bintrayPackage?.licenses[0]}</a>
        </g:if>
    </p>
    <div>
        <div class="align-left" style="margin-top: 3px;margin-bottom: 10px;"><g:render template="githubstar" model="[plugin: plugin]"/></div>

        <div class="align-left"><g:render template="pluginlabels" model="[plugin: plugin]"/></div>
    </div>


    <div style="clear: both;">
            <pre>
<code>
dependencies {
    compile '${plugin.bintrayPackage?.systemIds?.getAt(0)}:${plugin.bintrayPackage?.latestVersion}'
}
</code>
            </pre>
        </div>


    <g:if test="${plugin.readme}">
        <div style="clear: both;">
        <hr/>
        <div class="readme">${raw(plugin.readme)}</div>
        </div>
    </g:if>

</article>
</body>
</html>
