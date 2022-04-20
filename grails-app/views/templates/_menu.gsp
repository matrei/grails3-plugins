<nav class="thirdmenu">
    <ul>
        <g:set var="activePlugins" value="active" />
        <g:set var="activeLegacy" value="" />

        <g:if test="${active == 'legacy'}">
            <g:set var="activePlugins" value="" />
            <g:set var="activeLegacy" value="active" />
        </g:if>

        <li class="${activePlugins}"><a href="https://plugins.grails.org/"><g:message code="nav.grails3plugins" default="Current Plugins (Grails 3 & 4 & 5)"/></a></li>
        <li class="${activeLegacy}"><g:link controller="plugin" action="legacyPlugins"><g:message code="nav.grails2plugins" default="Legacy Plugins (Grails 1 & 2)"/></g:link></li>
        <li><a href="https://bintray.com/grails/plugins"><g:message code="nav.bintray" default="Bintray Repository"/></a></li>
        <li><a href="https://medium.com/@benorama/how-to-publish-your-grails-3-plugin-to-bintray-c341b24f567d"><g:message code="nav.publishingguide" default="Publishing Guide"/></a></li>
        <li><a href="https://blog.agileorbit.com/2015/10/07/Publishing-Grails-3-Plugins.html"><g:message code="nav.publishingfaq" default="Publishing FAQ"/></a></li>
        <li><a href="https://github.com/grails/grails3-plugins"><g:message code="nav.portalongithub" default="Portal on Github"/></a></li>
    </ul>
</nav>
