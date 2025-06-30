
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	
	<acme:list-column sortable = "true" code = "manager.leg.list.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:list-column sortable = "true" code="manager.leg.list.label.scheduledArrival" path="scheduledArrival"/>
	<acme:list-column sortable = "false" code="manager.leg.list.label.departureAirport" path="departureAirport"/>
	<acme:list-column sortable = "false" code="manager.leg.list.label.arrivalAirport" path="arrivalAirport"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${showCreate == true}">
	<acme:button code="manager.leg.list.button.create" action="/manager/leg/create?masterId=${masterId}"/>
</jstl:if>
	
