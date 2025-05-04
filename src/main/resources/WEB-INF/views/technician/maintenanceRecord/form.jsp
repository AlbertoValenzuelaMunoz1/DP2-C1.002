<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-moment code="technician.maintanence-record.form.label.maintanenceMoment" path="maintanenceMoment"/>
    <acme:input-money code="technician.maintanence-record.form.label.estimatedCost" path="estimatedCost"/>
    <acme:input-select  code="technician.maintenance-record.form.label.status" path="status" choices="${status}"/>
    <acme:input-select  code="technician.maintenance-record.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
    <acme:input-moment code="technician.maintanence-record.form.label.nextMaintanence" path="nextMaintanence"/>
    <acme:input-textbox code="technician.maintanence-record.form.label.notes" path="notes"/>
    
    <jstl:choose>
        
        <jstl:when test="${_command == 'create'}">
            <!-- Botón para crear mantenimiento -->
            <acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintanence-record/create"/>
        </jstl:when>

        <jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && status == 'PENDING'}">
            <!-- Si el mantenimiento está en estado PENDING -->
            <acme:button code="technician.task.list.button.task" action="/technician/task/taskList?recordId=${id}"/>
            <acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintanence-record/update"/>
            <acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintanence-record/publish"/>
            <acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintanence-record/delete"/>
            <acme:button code="technician.task.form.button.createRecord" action="/technician/task/createRecord?recordId=${id}"/>
        </jstl:when>

        <jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && status != 'PENDING'}">
            <!-- Si el mantenimiento no está en estado PENDING -->
            <acme:button code="technician.task.list.button.task" action="/technician/task/taskList?recordId=${id}"/>
            <acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintanence-record/update"/>
            <acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintanence-record/publish"/>
            <acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintanence-record/delete"/>
        </jstl:when>

    </jstl:choose>
</acme:form>
