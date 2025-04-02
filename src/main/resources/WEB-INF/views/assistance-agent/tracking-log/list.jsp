<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<acme:list>
    <acme:list-column code="agent.tracking-log.list.label.lastUpdateMoment" path="lastUpdateMoment" width="20%" />
    <acme:list-column code="agent.tracking-log.list.label.stepUndergoing" path="stepUndergoing" width="25%" />
    <acme:list-column code="agent.tracking-log.list.label.resolutionPercentage" path="resolutionPercentage" width="15%" />
    <acme:list-column code="agent.tracking-log.list.label.claimStatus" path="claimStatus" width="20%" />
    <acme:list-column code="agent.tracking-log.list.label.resolutionDetails" path="resolutionDetails" width="20%" />

    <acme:list-payload path="payload" />
</acme:list>

<jstl:if test="${_command == 'list'}">
    <acme:button code="agent.tracking-log.list.button.create" action="/assistance-agent/tracking-log/create"/>
</jstl:if>
