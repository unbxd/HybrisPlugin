<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">

	<div class="row">
		<cms:pageSlot position="Section1" var="feature" element="div"
			class="product-list-section1-slot">
			<cms:component component="${feature}" element="div"
				class="col-xs-12 yComponentWrapper product-list-section1-component" />
		</cms:pageSlot>
	</div>

	<div class="row">
		<cms:pageSlot position="ProductGridSlot" var="feature" element="div"
				class="unbxd-product-grid-slot">
				<cms:component component="${feature}" element="div"
					class="product__list--wrapper yComponentWrapper product-list-right-component" />
		</cms:pageSlot>
	</div>

</template:page>