<nav class="thirdmenu">
    <ul>
        <g:set var="activePlugins" value="active" />
        <g:set var="activeLegacy" value="" />

        <g:if test="${active == 'legacy'}">
            <g:set var="activePlugins" value="" />
            <g:set var="activeLegacy" value="active" />
        </g:if>

        <li class="${activePlugins}"><a href="https://plugins.grails.org/"><g:message code="nav.grails3plugins" default="Current Plugins (Grails 3+)"/></a></li>
        <li class="${activeLegacy}"><g:link controller="plugin" action="legacyPlugins"><g:message code="nav.grails2plugins" default="Legacy Plugins (Grails 1 & 2)"/></g:link></li>
        <li><a href="https://repo.grails.org/ui/repos/tree/General/plugins"><g:message code="nav.mavenrepo" default="Maven Repository"/></a></li>
        <li><a href="https://grails.org/blog/2021-04-07-publish-grails-plugin-to-maven-central.html"><g:message code="nav.publishingguide" default="Publishing Guide"/></a></li>
        <li><a href="https://github.com/grails/grails3-plugins"><g:message code="nav.portalongithub" default="Portal on Github"/></a></li>
    </ul>
</nav>
