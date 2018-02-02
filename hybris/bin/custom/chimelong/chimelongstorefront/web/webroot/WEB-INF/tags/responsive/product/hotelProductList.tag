<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<c:choose>
	<c:when test="${net}">
		<form id="searchNetForm" class="form-horizontal" role="form"
			method="get">
			<div class="form-group">
				<label class="col-sm-2 control-label">游玩时间:</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="date" id="date"
						value="${date}" placeholder="yyyy-MM-dd">
				</div>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="edate" id="edate"
						value="${edate}" placeholder="yyyy-MM-dd">
				</div>
				<input name="net" type="hidden" value="true" />
				<div class="col-sm-1">
					<button type="submit" class="btn btn-primary">查询</button>
				</div>
			</div>
		</form>
	</c:when>
	<c:when test="${not net}">
		<form id="searchBundleForm" class="form-horizontal" role="form"
			method="get">
			<div class="form-group">
				<label class="col-sm-2 control-label">游玩时间:</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="date" id="date"
						placeholder="yyyy-MM-dd">
				</div>
				<input name="net" type="hidden" value="false" />
				<div class="col-sm-1">
					<button type="submit" class="btn btn-primary">查询</button>
				</div>
			</div>
		</form>
	</c:when>
</c:choose>

<div class="row">
	<div class="col-xs-3">
		<ul class="nav nav-tabs nav-justified">
			<li <c:if test="${net}">class="active"</c:if>><a
				href="${searchUrl}?net=true">净房价</a></li>
			<li <c:if test="${not net}">class="active"</c:if>><a
				href="${searchUrl}?net=false">套票</a></li>
		</ul>
	</div>
</div>

<c:if test="${net}">
	<table>
		<tr>
			<th width="335" style="text-align: left; padding-left: 45px">名称</th>
			<th width="325">官网价</th>
			<th class="blue align-right"
				style="padding-right: 20px; cursor: pointer;"></th>
		</tr>
		<c:forEach items="${productDatas}" var="productData">
			<tr>
				<td>${productData.name}</td>
				<td><format:fromPrice priceData="${productData.price}" /></td>
				<td>购买</td>
			</tr>
		</c:forEach>
	</table>
</c:if>
<c:if test="${not net}">
	<table>
		<tr>
			<th width="335" style="text-align: left; padding-left: 45px">名称</th>
			<th width="325">官网价</th>
			<th class="blue align-right"
				style="padding-right: 20px; cursor: pointer;"></th>
		</tr>
		<c:forEach items="${productDatas}" var="productData">
			<tr>
				<th rowspan="${productData.bundleTemplates.size()}">${productData.name}</th>
				<c:forEach var="bundleTemplate"
					items="${productData.bundleTemplates}" begin="0" end="0" step="1">
					<td>${bundleTemplate.name}</td>
					<td><format:fromPrice priceData="${bundleTemplate.price}" /></td>
					<td>购买</td>
				</c:forEach>
			</tr>
			<c:forEach var="bundleTemplate2"
				items="${productData.bundleTemplates}" begin="1" step="1">
				<td>${bundleTemplate2.name}</td>
				<td><format:fromPrice priceData="${bundleTemplate2.price}" /></td>
				<td>购买</td>
			</c:forEach>
		</c:forEach>
	</table>
</c:if>