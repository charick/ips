<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="<%=request.getContextPath()%>/webapp/WEB-INF/css/style_index.css" rel="stylesheet"
	type="text/css" />
<style>
<!--
td {
	font-size: 12px;
}
-->
</style>
<div id="box">
<div id="dark"><a href="">
	<img src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/index_03.gif" width="28"
		height="14" /> 我的购物车</a> | </a>我的订单</a>　
	欢迎 　
</div>
<div id="logo">
<form action="product_findByName" method="post" namespace="/product">
<div id="sou">
	<input name="name"></input><br>
	<div style="margin-top: 5px;">
		<b>热搜商品：</b>
		<a action="product_getByCategoryId" namespace="/product">
			软件
		</a>
	</div>
</div>
<div id="sou_zi">
	<input type="image" src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/index_09.gif" value="搜索"/>
</div>
</form>
<div id="sou_zi01">高级搜索<br />

使用帮助</div>
</div>
<div id="menu">
	<a action="index" namespace="/">
<img src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/index_12.gif"
	width="92" height="33" /></a>
	<a action="product_findNewProduct" namespace="/product"><img
	src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/index_13.gif" width="100" height="33" /></a>
	<a action="product_findSellProduct" namespace="/product"><img
	src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/index_14.gif" width="99" height="33" /></a>
	<a action="product_findCommendProduct" namespace="/product"><img
	src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/index_15.gif" width="98" height="33" /></a>
	<a action="product_findEnjoyProduct" namespace="/product"><img
	src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/index_16.gif" width="100" height="33" /></a><img
	src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/index_19.gif" width="144" height="33"
	id="z300" /></div>
</div>

