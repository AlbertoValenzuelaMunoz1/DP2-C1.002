<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="technician.task.form.label.description" path="description" />
	<acme:input-textbox code="technician.task.form.label.priority" path="priority" />
	<acme:input-textbox code="technician.task.form.label.estimatedDuration" path="estimatedDuration" />
	<acme:input-select code="technician.task.form.label.task-type" path="taskType" choices="${types}" />

	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.task.form.button.create" action="/technician/task/create" />
		</jstl:when>

		<jstl:when test="${_command == 'createRecord'}">
			<acme:submit code="technician.task.form.button.createRecord" action="/technician/task/createRecord?recordId=${id}" />
		</jstl:when>

		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete')}">
			<acme:submit code="technician.task.form.button.update" action="/technician/task/update" />
			<acme:submit code="technician.task.form.button.delete" action="/technician/task/delete" />
			<acme:submit code="technician.task.form.button.publish" action="/technician/task/publish" />
		</jstl:when>
	</jstl:choose>
</acme:form>
