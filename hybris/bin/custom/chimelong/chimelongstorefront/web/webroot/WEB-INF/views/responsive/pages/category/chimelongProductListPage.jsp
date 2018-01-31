<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">
	<div class="container">
		<div class="row">	
			<div class="col-xs-3">	
				<cms:pageSlot position="Section1" var="feature" element="div" class="product-list-section1-slot">
					<cms:component component="${feature}" element="div" class="col-xs-12 yComponentWrapper product-list-section1-component"/>
				</cms:pageSlot>	
			</div>	
			<div class="col-sm-12 col-md-9" >
				<!-- add c:if -->
				<%-- <c:if test="aa"> --%>
					<product:hotelProductList />
				<%-- </c:if> --%>
				<!-- else -->
				<%-- <product:ticketProductList /> --%>
			</div>
		</div>
	</div>
</template:page>