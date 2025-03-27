<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.airport.form.label.name" path="name" readonly="true" />
	<acme:input-textbox code="administrator.airport.form.label.iataCode" path="iataCode" readonly="true" />
	<acme:input-textbox code="administrator.airport.form.label.operationalScope" path="operationalScope" />
	<acme:input-textbox code="administrator.airport.form.label.city" path="city" readonly="true" />
	<acme:input-textbox code="administrator.airport.form.label.country" path="country" readonly="true" />
	<acme:input-url code="administrator.airport.form.label.website" path="website" readonly="true" />
	<acme:input-email code="administrator.airport.form.label.email" path="email" readonly="true" />
	<acme:input-textbox code="administrator.airport.form.label.address" path="address" readonly="true" />
	<acme:input-textbox code="administrator.airport.form.label.contactPhoneNumber" path="contactPhoneNumber" readonly="true" />
</acme:form>

<acme:button code="administrator.airport.form.button.update" action="/administrator/aircraft/create"/>
