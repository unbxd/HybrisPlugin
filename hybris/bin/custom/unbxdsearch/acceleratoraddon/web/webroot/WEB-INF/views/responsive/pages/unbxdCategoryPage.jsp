<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="unbxdtags" tagdir="/WEB-INF/tags/addons/unbxdsearch/responsive/" %>


<template:page pageTitle="${pageTitle}">

	<div class="row">
		<cms:pageSlot position="Section1" var="feature" element="div"
			class="product-list-section1-slot">
			<cms:component component="${feature}" element="div"
				class="col-xs-12 yComponentWrapper product-list-section1-component" />
		</cms:pageSlot>
	</div>

	<div class="row">
		<unbxdtags:search></unbxdtags:search>
	</div>

</template:page>