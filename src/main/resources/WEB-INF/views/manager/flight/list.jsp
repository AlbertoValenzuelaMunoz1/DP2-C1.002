
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column  code="manager.list.label.tag" path="tag"/>	
	<acme:list-column code="manager.list.label.transfer" path="transfer"/>
	<acme:list-column code="manager.list.label.cost" path="cost"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="manager.flight.list.button.create" action="/manager/flight/create"/>
