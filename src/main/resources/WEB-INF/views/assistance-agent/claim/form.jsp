<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textarea code="agent.claim.form.label.description" path="description" />
    <acme:input-moment code="agent.claim.form.label.registrationMoment" path="registrationMoment" />
    <acme:input-email code="agent.claim.form.label.passengerEmail" path="passengerEmail" />
    <acme:input-select code="agent.claim.form.label.type" path="type" choices="${type}" />
    <acme:input-textbox code="agent.claim.form.label.indicator" path="indicator" readonly="true" />
    <acme:input-checkbox code="assistance-agent.claim.label.draftMode" path="draftMode"/> 
        <acme:input-select code="assistance-agent.claim.label.leg" path="leg" choices="${legs}"/>
    <acme:input-moment code="assistance-agent.claim.label.agent" path="assistanceAgent" readonly="true"/>  

    <jstl:choose>	 
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">  
            <acme:submit code="agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
            <acme:submit code="agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
            <acme:submit code="agent.claim.form.button.publish" action="/assistance-agent/claim/publish"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
        </jstl:when>		
    </jstl:choose>
</acme:form>
