
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="false">
	
	<acme:list-column sortable = "true" code = "any.leg.list.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:list-column sortable = "true" code="any.leg.list.label.scheduledArrival" path="scheduledArrival"/>
	<acme:list-column sortable = "false" code="any.leg.list.label.departureAirport" path="departureAirport"/>
	<acme:list-column sortable = "false" code="any.leg.list.label.arrivalAirport" path="arrivalAirport"/>
	<acme:list-payload path="payload"/>
</acme:list>


