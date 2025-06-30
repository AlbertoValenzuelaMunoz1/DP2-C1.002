
<%@page%>
 
 <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
 <acme:form readonly="${readonly}">
 
 	<input type="hidden" name="id" value="${id}" />
	<input type="hidden" name="version" value="${version}" />
 		
 		
  	<jstl:choose>
  		<jstl:when test="${_command == 'create'}">
  			<acme:input-integer code="manager.leg.form.label.flightNumberDigits" path="flightNumberDigits" placeholder="XXXX"/>
		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">
  			<acme:input-double 	code="manager.leg.form.label.flightNumberDigitsUpdateAndPublish" path="flightNumberDigits" readonly="true"/>
		</jstl:when>
  	</jstl:choose>
	<acme:input-moment code="manager.leg.form.label.scheduledDeparture" path="scheduledDeparture" />
	<acme:input-moment code="manager.leg.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-double  code="manager.leg.form.label.durationInHours" path="durationInHours" readonly="true"/>
	<acme:input-select code="manager.leg.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-select code="manager.leg.form.label.departureAirport" path="departureAirport" choices="${departureAirports}"/>
	<acme:input-select code="manager.leg.form.label.arrivalAirport" path="arrivalAirport" choices="${arrivalAirports}"/>
	<acme:input-select code="manager.leg.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<acme:input-textbox code="manager.leg.form.label.flight" path="flight.tag"  readonly="true"/>
 	<acme:input-textbox code = "manager.flight.form.label.draftMode" path= "draftMode" readonly="true"/>

 	
 	
 	<jstl:choose>	 
 	
 		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="manager.leg.form.button.update" action="/manager/leg/update"/>
 			<acme:submit code="manager.leg.form.button.delete" action="/manager/leg/delete"/>
 			<acme:submit code="manager.leg.form.button.publish" action="/manager/leg/publish"/>
 		</jstl:when>
 		
 		<jstl:when test="${_command == 'create'}">
 			<acme:submit code="manager.leg.form.button.create" action="/manager/leg/create?masterId=${masterId}"/>
 		</jstl:when>		
 	</jstl:choose>
 	
 </acme:form>