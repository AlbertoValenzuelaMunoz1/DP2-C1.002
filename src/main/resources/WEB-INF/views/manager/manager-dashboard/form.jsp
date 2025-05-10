<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:input-textbox code="manager.dashboard.form.label.ranking" path="ranking"/>
	<acme:input-textbox code="manager.dashboard.form.label.yearsToRetire" path="yearsToRetire"/>
	<acme:input-textbox code="manager.dashboard.form.label.ratio" path="ratio"/>
	<acme:input-textbox code = "manager.dashboard.form.label.mostPopularAirports" path="mostPopularAirports"/>
	<acme:input-textbox code = "manager.dashboard.form.label.lessPopularAirports" path="lessPopularAirports"/>
	<acme:input-textbox code="manager.dashboard.form.label.landed" path="landedLegs"/>
	<acme:input-textbox code="manager.dashboard.form.label.onTime" path="onTimeLegs"/>
	<acme:input-textbox code="manager.dashboard.form.label.delayed" path="delayedLegs"/>
	<acme:input-textbox code="manager.dashboard.form.label.canceled" path="canceledLegs"/>
	<acme:input-double code="manager.dashboard.form.label.averageCost" path="average"/>
	<acme:input-double code="manager.dashboard.form.label.maxCost" path="max"/>
	<acme:input-double code="manager.dashboard.form.label.minCost" path="min"/>
	<acme:input-double code="manager.dashboard.form.label.standardDesviation" path="standardDesviation"/>
	</acme:form>