<!DOCTYPE html>
<html>
<head>
    <meta name='viewport' content='width=device-width, initial-scale=1' />
    <meta charset='UTF-8' />
    <asset:stylesheet src="screen.css"/>
    <asset:javascript src="navigation.js"/>

    <title><g:layoutTitle default="Grails Plugins"/></title>

    <g:if test="${plugin instanceof Map}">
        <meta name="description" content="${plugin.desc}" />
        <meta property="og:type" content="website" />
        <meta property="og:image" content="${asset.assetPath(src: 'grails-logo-light.png', absolute: true)}" />
        <meta property="og:site_name" content="Grails Plugin Portal">
        <meta property="og:title" content="${plugin.name}" />
        <meta property="og:description" content="${plugin.desc}" />
        <meta name="twitter:title" value="${plugin.name}" />
        <meta name="twitter:description" value="${plugin.desc}" />
        <meta name="twitter:site" content="@grails_plugins" />
        <meta name="twitter:card" content="summary">
        <meta name="twitter:image" content="${asset.assetPath(src: 'grails-logo-light.png', absolute: true)}" />

        <g:if test="${plugin.latestVersion}">
            <meta name="twitter:label1" value="Latest Version" />
            <meta name="twitter:data1" value="${plugin.latestVersion}" />
            <meta name="twitter:label2" value="Published" />
            <meta name="twitter:data2" value="${lastUpdated}" />
        </g:if>
    </g:if>
    <g:else>
        <g:set var="name" value="Grails Plugin Portal" />
        <g:set var="description" value="A portal for searching Grails plugins!" />
        <meta name="description" content="${description}" />
        <meta property="og:type" content="website" />
        <meta property="og:image" content="${asset.assetPath(src: 'grails-logo-light.png', absolute: true)}" />
        <meta property="og:site_name" content="grails.org">
        <meta property="og:title" content="${name}" />
        <meta property="og:description" content="${description}" />
        <meta name="twitter:title" value="${name}" />
        <meta name="twitter:description" value="${description}" />
        <meta name="twitter:site" content="@grails_plugins" />
        <meta name="twitter:card" content="summary">
        <meta name="twitter:image" content="${asset.assetPath(src: 'grails-logo-light.png', absolute: true)}" />
    </g:else>

    <g:layoutHead/>
</head>
<body class='grails3plugins'>
<grailsnavigation:mainHeader active="${pageProperty(name:'page.title') == 'plugins' ? true : false}" page="plugins"/>
<div class='content'>
    <g:layoutBody/>
</div>
<grailsnavigation:footer/>
<grailsnavigation:scriptAtClosingBody/>
</body>
</html>
