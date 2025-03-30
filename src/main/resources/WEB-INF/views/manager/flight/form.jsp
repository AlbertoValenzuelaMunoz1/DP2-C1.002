<%@page%>
 
 <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
 <acme:form readonly="${readonly}">
 	<acme:input-textbox code="manager.flight.form.label.tag" path="tag"/>
 	<acme:input-textbox code="manager.flight.form.label.transfer" path="transfer" />
 	<acme:input-money code="manager.flight.form.label.cost" path="cost"/>
 	<acme:input-textarea code="manager.flight.form.label.description" path="description" />	
 	<jstl:if test="${!readonly}">
 		<acme:input-checkbox code="manager.flight.form.label.confirmation" path="confirmation"/>		
 	</jstl:if>
 	<jstl:if test="${_command == 'create'}">
 		<acme:submit code="manager.flight.form.button.create" action="/manager/flight/create"/>		
 	</jstl:if>
 	<jstl:if test="${acme:anyOf(_command, 'show|update|publish') && !readonly}">
 		<acme:submit code="manager.flight.form.button.update" action="/manager/flight/update"/>		
 		<acme:submit code="manager.flight.form.button.publish" action="/manager/flight/publish"/>
 	</jstl:if>
 	
 </acme:form>