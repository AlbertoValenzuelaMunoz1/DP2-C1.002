<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airline.list.label.name" path="name" />
	<acme:list-column code="administrator.airline.list.label.iataCode" path="iataCode" />
	<acme:list-column code="administrator.airline.list.label.website" path="website" />
	<acme:list-column code="administrator.airline.list.label.foundationMoment" path="foundationMoment" />
	<acme:list-column code="administrator.airline.list.label.type" path="type" />
	<acme:list-column code="administrator.airline.list.label.email" path="email" />
	<acme:list-column code="administrator.airline.list.label.phoneNumber" path="phoneNumber" />
</acme:list>
<acme:button code="customer.booking.list.button.create" action="/administrator/airline/create"/>