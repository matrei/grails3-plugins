<ul class="iconlinks">
    <li><a href="${grailsplugins.bintrayLink(plugin: plugin)}"><asset:image src="bintray.svg" width="20"/></a></li>
<li><a href="${plugin.bintrayPackage?.vcsUrl}"><asset:image src="small_github.svg" width="20"/></a></li>
</ul>
<h3><g:render template="pluginlink" model="[plugin: plugin, text: plugin.bintrayPackage?.name]"/></h3>
<p>${plugin.bintrayPackage?.desc}</p>
<p>
    <b>${plugin.bintrayPackage?.latestVersion}</b>
    <span> <g:message code="plugin.published.label" default="published"/> </span>
    <grailsplugins:lastUpdated plugin="${plugin}"/>
    <span> <g:message code="plugin.by.label" default="by"/> </span>
    <g:link absolute="true" uri="/q/owner:${plugin.bintrayPackage?.owner}">${plugin.bintrayPackage?.owner}</g:link>
</p>
<g:render template="pluginlabels" model="[plugin: plugin]"/>
<g:render template="githubstar" model="[plugin: plugin]"/>
