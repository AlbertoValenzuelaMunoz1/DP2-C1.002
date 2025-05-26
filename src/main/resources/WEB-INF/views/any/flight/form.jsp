<%@page%>
 
 <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
 <acme:form readonly="${readonly}">
 
 	<input type="hidden" name="id" value="${id}" />
	<input type="hidden" name="version" value="${version}" />
 	
 
 	
	<jstl:if test="${not empty numberOfLayovers and numberOfLayovers != 0}">
		<acme:input-textbox code="any.flight.form.label.origin" path="origin" readonly="true"/>
		 <acme:input-textbox code="any.flight.form.label.destiny" path="destiny" readonly="true"/>
		 <acme:input-moment code = "any.flight.form.label.departureDate" path= "departureDate" readonly="true"/>
		 <acme:input-moment code = "any.flight.form.label.arrivalDate" path= "arrivalDate" readonly="true"/>
		 <acme:input-textbox code = "any.flight.form.label.numberOfLayovers" path= "numberOfLayovers" readonly="true"/>
	</jstl:if>  
	
 	<acme:input-textbox code="any.flight.form.label.tag" path="tag"/>
 	<acme:input-checkbox code="any.flight.form.label.transfer" path="transfer" />
 	<acme:input-money code="any.flight.form.label.cost" path="cost"/>
 	<acme:input-textarea code="any.flight.form.label.description" path="description" />
 	<acme:input-textbox code = "any.flight.form.label.draftMode" path= "draftMode" readonly="true"/>
 	
 	
 	<jstl:choose>	 
 	
 		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="any.flight.form.button.legs" action="/any/leg/list?masterId=${id}"/>			
		</jstl:when>
		
	</jstl:choose>	 
 	
 </acme:form>