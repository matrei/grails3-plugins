<%@ page import="grailsplugins.util.EncodingUtil" %>
<ul class="iconlinks">
    <li><a href="${grailsplugins.packageLink(plugin: plugin)}"><asset:image src="bintray.svg" width="20"/></a></li>
    <g:if test="${plugin.vcsUrl}">
        <li><a href="${plugin.vcsUrl}"><asset:image src="small_github.svg" width="20"/></a></li>
    </g:if>
</ul>
<h3><g:render template="pluginlink" model="[plugin: plugin, text: plugin.name]"/></h3>
<p>${plugin.description}</p>
<p>
    <b>${plugin.latestVersion}</b>
    <span> <g:message code="plugin.published.label" default="published"/> </span>
    <grailsplugins:lastUpdated plugin="${plugin}"/>
    <span> <g:message code="plugin.by.label" default="by"/> </span>
    <g:link absolute="true" uri="/q/owner:${ EncodingUtil.encodeURIComponent plugin.owner }">${plugin.owner}</g:link>
</p>
<g:if test="${plugin.comment}">
    <div class="alert alert-info" role="alert">${plugin.comment}</div>
</g:if>
<g:if test="${plugin.deprecated}">
    <div class="alert alert-danger" role="alert">${plugin.deprecated}</div>
</g:if>
<g:render template="pluginlabels" model="[plugin: plugin]"/>
<g:render template="githubstar" model="[plugin: plugin]"/>
