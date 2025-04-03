<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="agent.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" />
	<acme:input-textbox code="agent.tracking-log.form.label.stepUndergoing" path="stepUndergoing" />
	<acme:input-double code="agent.tracking-log.form.label.resolutionPercentage" path="resolutionPercentage" />
	<acme:input-select code="agent.tracking-log.form.label.claimStatus" path="claimStatus" choices="${claimStatus}" />
	<acme:input-textarea code="agent.tracking-log.form.label.resolutionDetails" path="resolutionDetails" />
	<acme:input-select code="assistanceAgent.trackingLog.form.label.claim" path="claim" choices="${claim}"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete')}">
			<acme:input-checkbox code="agent.tracking-log.form.label.confirmation" path="confirmation"/>
			<acme:submit code="agent.tracking-log.form.button.update" action="/assistance-agent/tracking-log/update"/>
			<acme:submit code="agent.tracking-log.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
            <acme:submit code="agent.tracking-log.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="agent.tracking-log.form.label.confirmation" path="confirmation"/>
			<acme:submit code="agent.tracking-log.form.button.create" action="/assistance-agent/tracking-log/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
