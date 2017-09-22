<!DOCTYPE html>
<html>
<head>
    <title>${plugin.name} | Plugins | Grails Framework</title>
    <meta name="layout" content="main" />
</head>
<body class="grails3plugins">
<content tag="title">plugin</content>
<article class="post">
    <div class="align_right">
        <g:render template="githubstar" model="[plugin: plugin]"/>
    </div>
    <h1>${plugin.name}</h1>
    <div class="plugin-metadata">
        <ul class="align_right">
            <li><asset:image src="bintray.svg" alt="bintray" width="15"/> <a href="${grailsplugins.bintrayLink(plugin: plugin)}">Package</a></li>
            <g:if test="${plugin.issue_tracker_url}">
                <li><asset:image src="bug.svg" alt="Bug" width="15"/> <a href="${plugin.issue_tracker_url}">Issues</a></li>
            </g:if>
            <g:if test="${plugin.vcs_url}">
                <li><asset:image src="source.svg" alt="Source" width="15"/> <a href="${plugin.vcs_url}">Source</a></li>
            </g:if>
        </ul>
        <ul class="align_left">
            <li><b><g:message code="plugin.owner" default="Owner"/></b> <a href="q/owner:${plugin.owner}">${plugin.owner}</a></li>
            <g:if test="${plugin.licenses}">
                <li><b><g:message code="plugin.license" default="License"/></b> <a href="https://opensource.org/licenses/${plugin.licenses[0]}">${plugin.licenses[0]}</a></li>
            </g:if>
        </ul>
    </div>

    <g:render template="pluginlabels" model="[plugin: plugin]"/>

    <p>${plugin.desc}</p>

<p><b>${plugin.latest_version}</b> published <grailsplugins:lastUpdated plugin="${plugin}"/>
<pre>
    <code>dependencies {
        compile '${plugin.system_ids[0]}:${plugin.latest_version}'
    }
    </code>
</pre>

    <g:if test="${plugin.readmeHTML}">
        <hr/>
        <div class="readme">${raw(plugin.readmeHTML)}</div>
    </g:if>

</article>
</body>
</html>