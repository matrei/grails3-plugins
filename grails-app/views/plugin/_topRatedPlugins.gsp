<div class="latestguides">
    <h3 class="columnheader">Top Rated Plugins</h3>
    <ul>
        <g:each var="plugin" in="${topRatedPlugins}">
            <li>
                <b>${plugin.name} </b>
                <span><asset:image src="small_githubstar.svg" alt="Github"/> ${plugin.githubRepo?.stargazersCount}</span>
                <g:render template="pluginlink" model="[plugin: plugin, text: 'Read More']"/>
            </li>
        </g:each>
    </ul>
</div>
