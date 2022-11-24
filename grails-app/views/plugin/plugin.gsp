<!DOCTYPE html>
<html>
<head>
    <title>${plugin.name} | Plugins | Grails Framework</title>
    <meta name="layout" content="main" />
</head>
<body class="grails3plugins">

<div class="headerbar">
    <div class="content">
        <h1>${plugin.name}</h1>
    </div>
</div>

<content tag="title">plugin</content>
<article class="post" style="padding: 4%;">
    <p>${plugin.description}</p>
    <p>
        <b><g:message code="plugin.owner" default="Owner"/>:</b> <g:link absolute="true" uri="/q/owner:${plugin.owner}">${plugin.owner}</g:link>
        <span> | </span><b>${plugin.latestVersion}</b>
        <span> | </span><grailsplugins:lastUpdated plugin="${plugin}"/>
        <g:if test="${plugin.systemIds}">
            <span> | </span><a href="${grailsplugins.packageLink(plugin: plugin)}">Package</a>
        </g:if>
        <g:if test="${plugin.issueTrackerUrl}">
            <span> | </span> <a href="${plugin.issueTrackerUrl}">Issues</a>
        </g:if>
        <g:if test="${plugin.vcsUrl}">
            <span> | </span> <a href="${plugin.vcsUrl}">Source</a>
        </g:if>
        <g:if test="${plugin.documentationUrl}">
            <span> | </span><a href="${plugin.documentationUrl}">Documentation</a>
        </g:if>
        <g:if test="${plugin.licenses}">
            <span> | </span><b><g:message code="plugin.license" default="License"/>:</b> <a href="https://opensource.org/licenses/${plugin.licenses[0].replaceAll(' ', '-')}">${plugin.licenses[0]}</a>
        </g:if>
    </p>
    <div>
        <div class="align-left" style="margin-top: 3px;margin-bottom: 10px;"><g:render template="githubstar" model="[plugin: plugin]"/></div>
        <div class="align-left"><g:render template="pluginlabels" model="[plugin: plugin]"/></div>
    </div>
    <g:if test="${plugin.systemIds}">
        <div style="clear: both;">
            <pre>
<code>
dependencies {
    implementation '${plugin.systemIds[0]}:${plugin.latestVersion}'
}
</code>
            </pre>
        </div>
    </g:if>

    <div style="clear: both;">
        <g:if test="${plugin.comment}">
            <div class="alert alert-info" role="alert">${plugin.comment}</div>
        </g:if>
        <g:if test="${plugin.deprecated}">
            <div class="alert alert-danger" role="alert">${plugin.deprecated}</div>
        </g:if>
        <g:if test="${plugin.readme}">
            <hr/>
            <div class="readme" style="margin-top: 2em;">${raw(plugin.readme)}</div>
        </g:if>
    </div>

</article>
</body>
</html>
