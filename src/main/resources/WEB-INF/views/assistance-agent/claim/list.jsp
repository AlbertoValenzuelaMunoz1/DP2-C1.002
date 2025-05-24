<%@page%>

<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<acme:list>
    <acme:list-column code="agent.claim.list.label.type" path="type" />
    <acme:list-column code="agent.claim.list.label.indicator" path="indicator" />
    <acme:list-column code="agent.claim.list.label.registrationMoment" path="registrationMoment" />
    <acme:list-column code="agent.claim.list.label.passengerEmail" path="passengerEmail" />
    <acme:list-column code="agent.claim.list.label.description" path="description" />
    <acme:list-column code="agent.claim.label.leg" path="leg"/>  
    
    <acme:list-payload path="payload" />
</acme:list>

<jstl:if test="${_command == 'list'}">
    <acme:button code="agent.claim.list.button.create" action="/assistance-agent/claim/create"/>
</jstl:if>
