<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/shared/analytics" %>
<%@ taglib prefix="unbxdanalytics" tagdir="/WEB-INF/tags/addons/unbxdanalytics/shared/analytics" %>
<script type="text/javascript" src="${sharedResourcePath}/js/analyticsmediator.js"></script>
<analytics:googleAnalytics/>
<unbxdanalytics:piwikAnalytics/>
<unbxdanalytics:unbxdAnalytics/>