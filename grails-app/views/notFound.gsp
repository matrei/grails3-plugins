<%@ page import="grails.converters.JSON" %>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>404 - Page not found</title>

    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <asset:stylesheet src="manifest.css"/>
    <asset:link rel="shortcut icon" href="favicon.ico" type="image"/>
</head>
<body>

<g:render template="/plugin/socializemenu" />
<g:render template="/plugin/header" />

<div class="main-content page-width">
    <asset:image src="404.png" class="notFoundImage" />
</div>

</body>
</html>
