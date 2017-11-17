<!DOCTYPE html>
<html>
<head>
    <meta name='viewport' content='width=device-width, initial-scale=1' />
    <meta charset='UTF-8' />
    <title><g:layoutTitle default="Grails 3 Plugins"/></title>
    <asset:stylesheet src="screen.css"/>
    <asset:javascript src="navigation.js"/>
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