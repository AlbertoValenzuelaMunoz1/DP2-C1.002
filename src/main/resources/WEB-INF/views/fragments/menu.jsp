<%--
- menu.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link" action="http://www.example.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-Rafa" action="https://www.youtube.com/watch?v=ML6r5pwKX1E"/> 
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-Alberto" action="https://ev.us.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-Antonio" action="https://i.redd.it/0p0d4a8etnl51.jpg"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-Fernando" action="https://www.futbin.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-Guillermo" action="https://www.instagram.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.any">
			<acme:menu-suboption code="master.menu.any.flightsPublished" action="/any/flight/list"/>

		</acme:menu-option>
		
		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airport" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.airline" action="/administrator/airline/list"/>
			<acme:menu-suboption code="master.menu.administrator.aircrafts" action="/administrator/aircraft/list"/>
			<acme:menu-suboption code="master.menu.administrator.bookings" action="/administrator/booking/list" />
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.agent" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.agent.list-claim" action="/assistance-agent/claim/list"/>
		</acme:menu-option>


		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.bookings" action="/customer/booking/list" />
			<acme:menu-suboption code="master.menu.customer.passengers" action="/customer/passenger/list" />
			<acme:menu-suboption code="master.menu.customer.customer-dashboard" action="/customer/customer-dashboard/show" />
		</acme:menu-option>
		<acme:menu-option code="master.menu.flightCrewMember" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.flightCrewMember.flightAssignmentCompleted" action="/flight-crew-member/flight-assignment/list-completed"/>
			<acme:menu-suboption code="master.menu.flightCrewMember.flightAssignmentMineCompleted" action="/flight-crew-member/flight-assignment/list-mine-completed"/>
			<acme:menu-suboption code="master.menu.flightCrewMember.flightAssignmentPlanned" action="/flight-crew-member/flight-assignment/list-planned"/>
			<acme:menu-suboption code="master.menu.flightCrewMember.flightAssignmentMinePlanned" action="/flight-crew-member/flight-assignment/list-mine-planned"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
			<acme:menu-suboption code="master.menu.manager.flights" action="/manager/flight/list" />
			<acme:menu-suboption code="master.menu.manager.manager-dashboard" action="/manager/manager-dashboard/show" />
		</acme:menu-option>
	</acme:menu-left>
	

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-customer" action="/authenticated/customer/create" access="!hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.customer-profile" action="/authenticated/customer/update" access="hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/manager/create" access="!hasRealm('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.manager-profile" action="/authenticated/manager/update" access="hasRealm('Manager')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>