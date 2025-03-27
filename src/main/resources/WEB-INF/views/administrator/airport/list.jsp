<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<acme:list>
    <acme:list-column code="administrator.airport.list.label.name" path="name" width="15%" />
    <acme:list-column code="administrator.airport.list.label.iataCode" path="iataCode" width="10%" />
    <acme:list-column code="administrator.airport.list.label.operationalScope" path="operationalScope" width="10%" />
    <acme:list-column code="administrator.airport.list.label.city" path="city" width="10%" />
    <acme:list-column code="administrator.airport.list.label.country" path="country" width="10%" />
    <acme:list-column code="administrator.airport.list.label.website" path="website" width="10%" />
    <acme:list-column code="administrator.airport.list.label.email" path="email" width="10%" />
    <acme:list-column code="administrator.airport.list.label.address" path="address" width="15%" />
    <acme:list-column code="administrator.airport.list.label.contactPhoneNumber" path="contactPhoneNumber" width="10%" />
    
    <acme:list-payload path="payload" />
</acme:list>


<acme:button code="administrator.airport.list.button.create" action="/administrator/aircraft/create"/>
