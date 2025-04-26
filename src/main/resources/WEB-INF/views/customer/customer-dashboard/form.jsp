<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:input-textbox code="customer.dashboard.form.label.destination1" path="0"/>
	<acme:input-textbox code="customer.dashboard.form.label.destination2" path="1"/>
	<acme:input-textbox code="customer.dashboard.form.label.destination3" path="2"/>
	<acme:input-textbox code="customer.dashboard.form.label.destination4" path="3"/>
	<acme:input-textbox code="customer.dashboard.form.label.destination5" path="4"/>
	<acme:input-double code="customer.dashboard.form.label.moneySpent" path="moneySpent"/>
	<acme:input-integer code="customer.dashboard.form.label.business" path="business"/>
	<acme:input-integer code="customer.dashboard.form.label.economy" path="economy"/>
	<acme:input-integer code="customer.dashboard.form.label.countBookings" path="costStatistics.count"/>
	<acme:input-double code="customer.dashboard.form.label.costMin" path="costStatistics.min"/>
	<acme:input-double code="customer.dashboard.form.label.costMax" path="costStatistics.max"/>
	<acme:input-double code="customer.dashboard.form.label.costAverage" path="costStatistics.average"/>
	<acme:input-double code="customer.dashboard.form.label.costStandardDesviation" path="costStatistics.standardDesviation"/>
	<acme:input-integer code="customer.dashboard.form.label.countPassengers" path="passengerStatistics.count"/>
	<acme:input-integer code="customer.dashboard.form.label.passengerMin" path="passengerStatistics.min"/>
	<acme:input-integer code="customer.dashboard.form.label.passengerMax" path="passengerStatistics.max"/>
	<acme:input-double code="customer.dashboard.form.label.passengerAverage" path="passengerStatistics.average"/>
	<acme:input-double code="customer.dashboard.form.label.passengerStandardDesviation" path="passengerStatistics.standardDesviation"/>
</acme:form>