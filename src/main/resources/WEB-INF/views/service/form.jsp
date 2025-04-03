<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form >
	<acme:input-textbox code="any.service.form.label.name" path="name"/>
	<acme:input-textbox code="any.service.form.label.promotionCode" path="promotionCode"/>
	<acme:input-integer code="any.service.form.label.averageDwellTime" path="averageDwellTime"/>
	<acme:input-double code="any.service.form.label.money" path="money"/>
	<div class="panel-body" style="margin: 1em 0em 1em 0em; text-align: center;">	
		<a href="./any/service/show?id=${id}" target="_blank">
			<img src="${picture}" alt="${name}" class="img-fluid rounded" style="border-style: solid;"/>
		</a>
	</div>
</acme:form>