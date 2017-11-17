<g:if test="${plugin.labels}">
<ul class="labels">
    <g:each var="label" in="${plugin.labels}">
        <li class="label"><g:link uri="/q/label:${label}">${label}</g:link></li>
    </g:each>
</ul>
</g:if>