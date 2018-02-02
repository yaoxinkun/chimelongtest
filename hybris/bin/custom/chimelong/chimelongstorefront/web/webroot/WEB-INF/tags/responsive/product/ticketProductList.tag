<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<form class="form-horizontal" role="form" method="get">
	<div class="form-group">
		<label for="firstname" class="col-sm-2 control-label">游玩时间:</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" name="date" id="date"
				value="${date}" placeholder="yyyy-MM-dd">
		</div>
		<div class="col-sm-1">
			<button type="submit" class="btn btn-primary">查询</button>
		</div>
	</div>
</form>
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







