<g:if test="${plugin.owner}">
    <g:link controller="plugin" action="pluginWithOwner" params="[ownerName: plugin.owner, pluginName: plugin.name]">${text}</g:link>
</g:if>
<g:else>
    <g:link controller="plugin" action="plugin" params="[pluginName: plugin.name]">${text}</g:link>
</g:else>