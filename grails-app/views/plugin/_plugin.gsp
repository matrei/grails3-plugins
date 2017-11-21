<ul class="iconlinks">
    <li><a href="${grailsplugins.bintrayLink(plugin: plugin)}"><asset:image src="bintray.svg" width="20"/></a></li>
<li><a href="${plugin.vcs_url}"><asset:image src="small_github.svg" width="20"/></a></li>
</ul>
<h3>
    <g:if test="${plugin.owner}">
        <g:link controller="plugin" action="pluginWithOwner" params="[ownerName: plugin.owner, pluginName: plugin.name]">${plugin.name}</g:link>
    </g:if>
    <g:else>
        <g:link controller="plugin" action="plugin" params="[pluginName: plugin.name]">${plugin.name}</g:link>
    </g:else>
</h3>
<p>${plugin.desc}</p>
<p>
    <b>${plugin.latest_version}</b>
    <span> <g:message code="plugin.published.label" default="published"/> </span>
    <grailsplugins:lastUpdated plugin="${plugin}"/>
    <span> <g:message code="plugin.by.label" default="by"/> </span>
    <g:link absolute="true" uri="/q/owner:${plugin.owner}">${plugin.owner}</g:link>
</p>
<g:render template="pluginlabels" model="[plugin: plugin]"/>
<g:render template="githubstar" model="[plugin: plugin]"/>
