<%@ page import="grailsplugins.util.EncodingUtil" %>
<g:if test="${plugin.labels}">
<ul class="labels">
    <g:each var="label" in="${plugin.labels}">
        <g:set var="lcLabel" value="${ label.toLowerCase(Locale.ENGLISH) }"/>
        <g:set var="urlLabel" value="${ EncodingUtil.encodeURIComponent(lcLabel) }"/>
        <li class="label"><g:link absolute="true" uri="/q/label:${urlLabel}">${lcLabel}</g:link></li>
    </g:each>
</ul>
</g:if>