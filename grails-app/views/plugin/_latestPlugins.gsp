<div class="latestguides">
    <h3 class="columnheader">Latest Plugins</h3>
    <ul>
        <g:each var="plugin" in="${latestPlugins}">
            <li>
                <b>${plugin.name} </b>
                <span><grailsplugins:lastUpdated plugin="${plugin}"/></span>
                <g:render template="pluginlink" model="[plugin: plugin, text: 'Read More']"/>
            </li>
        </g:each>
    </ul>
</div>
