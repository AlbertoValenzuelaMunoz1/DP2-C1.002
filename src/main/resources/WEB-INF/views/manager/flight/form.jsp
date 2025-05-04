<%@page%>
 
 <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
 <acme:form readonly="${readonly}">
 
 	<input type="hidden" name="id" value="${id}" />
	<input type="hidden" name="version" value="${version}" />
 	
 
 	
	<jstl:if test="${not empty numberOfLayovers and numberOfLayovers != 0}">
		<acme:input-textbox code="manager.flight.form.label.origin" path="origin" readonly="true"/>
		 <acme:input-textbox code="manager.flight.form.label.destiny" path="destiny" readonly="true"/>
		 <acme:input-moment code = "manager.flight.form.label.departureDate" path= "departureDate" readonly="true"/>
		 <acme:input-moment code = "manager.flight.form.label.arrivalDate" path= "arrivalDate" readonly="true"/>
		 <acme:input-textbox code = "manager.flight.form.label.numberOfLayovers" path= "numberOfLayovers" readonly="true"/>
	</jstl:if>  
	
 	<acme:input-textbox code="manager.flight.form.label.tag" path="tag"/>
 	<acme:input-checkbox code="manager.flight.form.label.transfer" path="transfer" />
 	<acme:input-money code="manager.flight.form.label.cost" path="cost"/>
 	<acme:input-textarea code="manager.flight.form.label.description" path="description" />
 	<acme:input-textbox code = "manager.flight.form.label.draftMode" path= "draftMode" readonly="true"/>
 	
 	
 	<jstl:choose>	 
 	
 		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="manager.flight.form.button.legs" action="/manager/leg/list?masterId=${id}"/>			
		</jstl:when>
 		
 		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="manager.flight.form.button.legs" action="/manager/leg/list?masterId=${id}"/>
 			<acme:submit code="manager.flight.form.button.update" action="/manager/flight/update"/>
 			<acme:submit code="manager.flight.form.button.delete" action="/manager/flight/delete"/>
 			<acme:submit code="manager.flight.form.button.publish" action="/manager/flight/publish"/>
 		</jstl:when>
 		
 		<jstl:when test="${_command == 'create'}">
 			<acme:submit code="manager.flight.form.button.create" action="/manager/flight/create"/>
 		</jstl:when>		
 	</jstl:choose>
 	
 </acme:form>