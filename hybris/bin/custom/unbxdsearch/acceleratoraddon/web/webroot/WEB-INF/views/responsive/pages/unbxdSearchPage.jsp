<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="unbxdtags" tagdir="/WEB-INF/tags/addons/unbxdsearch/responsive/" %>

<template:page pageTitle="${pageTitle}">

	<div class="row">
		<unbxdtags:search></unbxdtags:search>
	</div>


</template:page>