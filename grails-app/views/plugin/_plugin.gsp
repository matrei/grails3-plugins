<ul class="iconlinks">
    <li><a href="https://bintray.com/${plugin.owner}/${plugin.name}"><asset:image src="bintray.svg" width="20"/></a></li>
<li><a href="https://github.com/${plugin.owner}/${plugin.name}"><asset:image src="small_github.svg" width="20"/></a></li>
</ul>
<h3><a href="plugin/${plugin.owner}/${plugin.name}">${plugin.name}</a></h3>
<p>${plugin.desc}</p>
<p><b>${plugin.latest_version}</b> published <grailsplugins:lastUpdated plugin="${plugin}"/> by <a href="q/owner:${plugin.owner}">${plugin.owner}</a></p>
<g:render template="pluginlabels" model="[plugin: plugin]"/>
<g:render template="githubstar" model="[plugin: plugin]"/>
