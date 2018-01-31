<g:if test="${plugin.bintrayPackage?.labels}">
<ul class="labels">
    <g:each var="label" in="${plugin.bintrayPackage?.labels}">
        <li class="label"><g:link absolute="true" uri="/q/label:${label}">${label}</g:link></li>
    </g:each>
</ul>
</g:if>