<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="flight-crew-member.flight-assignment.form.label.last-update" path="lastUpdate" readonly="true"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.flight-crew-member" path="flightCrewMember" choices= "${crewMembers}"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.duty" path="duty" choices= "${duties}"/>	
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.flightLeg" path="flightLeg" choices= "${legs}"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.status" path="status" choices= "${statuses}"/>
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks"/>
		
	<jstl:if test="${_command != 'create'}">
    	<acme:button code="flight-crew-member.flight-assignment.form.button.activity-log" action="/flight-crew-member/activity-log/list?masterId=${id}"/>
	</jstl:if>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flight-crew-member/flight-assignment/delete"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.publish" action="/flight-crew-member/flight-assignment/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>