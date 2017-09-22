<g:if test="${plugin.labels}">
<ul class="labels">
    <g:each var="label" in="${plugin.labels}">
        <li class="label"><a href="q/label:${label}">${label}</a></li>
    </g:each>
</ul>
</g:if>