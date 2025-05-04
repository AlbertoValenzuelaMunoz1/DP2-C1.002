<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textarea code="agent.claim.form.label.description" path="description" />
    <acme:input-moment code="agent.claim.form.label.registrationMoment" path="registrationMoment" readonly = "true" />
    <acme:input-email code="agent.claim.form.label.passengerEmail" path="passengerEmail" />
    <acme:input-select code="agent.claim.form.label.type" path="type" choices="${type}" />
    <acme:input-textbox code="agent.claim.form.label.indicator" path="indicator" readonly="true" />
    <acme:input-select code="agent.claim.label.leg" path="leg" choices="${legs}"/>

    <jstl:choose>	 
    	<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="agent.claim.list.label.tracking-log" action="/assistance-agent/tracking-log/list?claimId=${id}"/>		
		</jstl:when>
		
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">  
        	<acme:button code="agent.claim.list.label.tracking-log" action="/assistance-agent/tracking-log/list?claimId=${id}"/>
            <acme:submit code="agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
            <acme:submit code="agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
            <acme:submit code="agent.claim.form.button.publish" action="/assistance-agent/claim/publish"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
        </jstl:when>		
    </jstl:choose>
    
	
	
	
</acme:form>
