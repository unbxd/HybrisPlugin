<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="unbxdtags" tagdir="/WEB-INF/tags/addons/unbxdsearch/responsive/" %>

<template:page pageTitle="${pageTitle}">

	<div class="row">
		<cms:pageSlot position="SearchResultsListSlot" var="feature" element="div" class="search-list-page-right-result-list-slot">
				<cms:component component="${feature}" element="div" class="search-list-page-right-result-list-component"/>
		</cms:pageSlot>
	</div>


</template:page>