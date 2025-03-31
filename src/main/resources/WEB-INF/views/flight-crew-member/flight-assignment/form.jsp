<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.flight-crew-member" path="member" choices= "${members}"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.Duty" path="Duty" choices= "${duties}"/>	
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.leg" path="leg" choices= "${legs}"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.status" path="status" choices= "${statuses}"/>
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks"/>
	
	
	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="flight-crew-member.flight-assignment.form.button.leg" action="/flight-crew-member/flight-assignment/show?masterId=${id}"/>	
			<acme:button code="flight-crew-member.flight-assignment.form.button.member" action="/flight-crew-member/flight-assignment/show?masterId=${id}"/>					
		</jstl:when> 
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
			<acme:input-checkbox code="administrator.aircraft.form.label.confirmation" path="confirmation"/>
			<acme:button code="flight-crew-member.flight-assignment.form.button.leg" action="/flight-crew-member/flight-assignment/show?masterId=${id}"/>	
			<acme:button code="flight-crew-member.flight-assignment.form.button.member" action="/flight-crew-member/flight-assignment/show?masterId=${id}"/>	
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.publish" action="/flight-crew-member/flight-assignment/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="flight-crew-member.flight-assignment.form.label.confirmation" path="confirmation"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>