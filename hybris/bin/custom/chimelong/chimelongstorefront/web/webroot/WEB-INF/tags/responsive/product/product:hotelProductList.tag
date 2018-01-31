<%@ tag language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="row">
	<form class="form-horizontal" role="form">
		<div class="form-group">
		    <label for="firstname" class="col-sm-2 control-label">游玩时间:</label>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="timeFrom" placeholder="请输入游行时间从">
		    </div>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="timeTo" placeholder="请输入游行时间至">
		    </div>
		     <div class="col-sm-1">
		    </div>
	  	</div>
		<div class="form-group">
		    <label for="lastname" class="col-sm-2 control-label">酒店信息</label>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="hotelInfo1" placeholder="请输入酒店信息">
		    </div>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="hotelInfo2" placeholder="请输入酒店信息">
		    </div>
		    <div class="col-sm-1">
		      <button type="submit" class="btn btn-primary">查询</button>
		    </div>
	  	</div>
	</form>
</div>

<div class="row">
	<div class="col-xs-3">
	<ul class="nav nav-tabs nav-justified">
		<li class="active"><a href="#">马戏票</a></li>
		<li><a href="#">套票</a></li>
	</ul>
	</div>
</div>

<div class="row">
	<table class="table table-bordered">
		<tr>
			<th width="335" ><spring:theme code="text.productList.table.name" /></th>
			<th width="325"><spring:theme code="text.productList.table.officialPrice" /></th>
			<th class="" style="padding-right:20px;cursor:pointer;"></th>
		</tr>
		<tr>
			<td>p1</td>
			<td>p1</td>
			<td>buy</td>
		</tr>
		<tr>
			<td>p1</td>
			<td>p1</td>
			<td>buy</td>
		</tr>
	</table>
</div>







  