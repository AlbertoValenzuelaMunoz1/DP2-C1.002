
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column  code="any.list.label.tag" path="tag"/>	
	<acme:list-column code="any.list.label.transfer" path="transfer"/>
	<acme:list-column code="any.list.label.cost" path="cost"/>
	<acme:list-column code="any.list.label.draftMode" path="draftMode"/>
	<acme:list-payload path="payload"/>
</acme:list>