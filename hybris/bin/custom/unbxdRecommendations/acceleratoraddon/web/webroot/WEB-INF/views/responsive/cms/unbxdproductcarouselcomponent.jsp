    <%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:choose>
	<c:when test="${not empty productData}">
		<div class="carousel__component">
			<div class="carousel__component--headline">${fn:escapeXml(title)}</div>

			<c:choose>
				<c:when test="${component.popup}">
					<div class="carousel__component--carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference">
						<div id="quickViewTitle" class="quickView-header display-none">
							<div class="headline">
								<span class="headline-text"><spring:theme code="popup.quick.view.select"/></span>
							</div>
						</div>
						<c:forEach items="${productData}" var="product">

							<c:url value="${product.url}/quickView" var="productQuickViewUrl"/>
							<div class="carousel__item">
								<a href="${productQuickViewUrl}" class="js-reference-item">
									<div class="carousel__item--thumb">
										<product:productPrimaryReferenceImage product="${product}" format="product"/>
									</div>
									<div class="carousel__item--name">${fn:escapeXml(product.name)}</div>
									<div class="carousel__item--price"><format:fromPrice priceData="${product.price}"/></div>
								</a>
							</div>
						</c:forEach>
					</div>
				</c:when>
				<c:otherwise>
					<div class="carousel__component--carousel js-owl-carousel js-owl-default">
						<c:forEach items="${productData}" var="product">

							<c:url value="${product.url}" var="productUrl"/>

							<div class="carousel__item">
								<a href="${productUrl}">
									<div class="carousel__item--thumb">
										<product:productPrimaryImage product="${product}" format="product"/>
									</div>
									<div class="carousel__item--name">${fn:escapeXml(product.name)}</div>
									<div class="carousel__item--price"><format:fromPrice priceData="${product.price}"/></div>
								</a>
							</div>
						</c:forEach>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</c:when>

	<script type="text/javascript">
	    _unbxd_getRecommendations({
                widgets: {
                    widget1: {
                        name: "home_recommendations1"
                    },
                    widget2: {
                        name: "home_recommendations2"
                    },
                    widget3: {
                        name: "home_recommendations3"
                    }
                },
                userInfo: {
                    userId: 'uidValue',
                    siteKey: 'siteKeyValue',
                    apiKey: 'apiKeyValue''
                },
                pageInfo: {
                    pageType: 'HOME'
                },
                itemClickHandler: function (product) {
                    //do what you want to do with product that has been clicked here
                    alert(JSON.stringify(product));
                    }
        });

    </script>

	<c:otherwise>
		<component:emptyComponent/>
	</c:otherwise>
</c:choose>

