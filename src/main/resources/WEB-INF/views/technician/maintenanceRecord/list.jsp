<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <!-- Columnas que muestran datos de los registros de mantenimiento -->
    <acme:list-column code="technician.maintanence-record.form.label.maintanenceMoment" path="maintanenceMoment" width="5%"/>
    <acme:list-column code="technician.maintanence-record.form.label.status" path="status" width="5%"/>
    <acme:list-column code="technician.maintanence-record.form.label.estimatedCost" path="estimatedCost" width="5%"/>
    <acme:list-column code="technician.maintanence-record.form.label.nextMaintanence" path="nextMaintanence" width="5%"/>
    <acme:list-column code="technician.maintanence-record.form.label.notes" path="notes" width="20%"/>
    
    <!-- Columna para las acciones -->
    <acme:list-column code="technician.maintanence-record.form.label.actions" path="actions" width="10%"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
    <acme:button code="technician.maintanence-record.list.button.create" action="/technician/maintanence-record/create"/>
</jstl:if>	
