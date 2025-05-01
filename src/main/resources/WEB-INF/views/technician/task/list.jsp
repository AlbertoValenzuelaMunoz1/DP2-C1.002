<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.task.list.label.description" path="description" width="50%" />
	<acme:list-column code="technician.task.list.label.priority" path="priority" width="10%" />
	<acme:list-column code="technician.task.list.label.estimated-duration" path="estimatedDuration" width="15%" />
	<acme:list-column code="technician.task.list.label.task-type" path="taskType" width="15%" />
	<acme:list-payload path="payload" />
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.task.form.button.create" action="/technician/task/create" />
</jstl:if>
