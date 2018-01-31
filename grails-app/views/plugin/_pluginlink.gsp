<g:if test="${plugin.bintrayPackage?.owner}">
    <g:link controller="plugin" action="pluginWithOwner" params="[ownerName: plugin.bintrayPackage?.owner, pluginName: plugin.bintrayPackage?.name]">${text}</g:link>
</g:if>
<g:else>
    <g:link controller="plugin" action="plugin" params="[pluginName: plugin.bintrayPackage?.name]">${text}</g:link>
</g:else>