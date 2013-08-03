<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/head.jsp"%>
	<div id="box">
		<div id="mid">
			<div id="denglu">
				<form action="user/login" method="post">
					<table width="100%" height="94" border="0" cellpadding="0"
						cellspacing="0">
						<tr>
							<td width="70" height="35" align="right">会员名21111：</td>
							<td width="121" align="left"><input name="username"
									class="bian" size="18"></input></td>
						</tr>
						<tr>
							<td height="35" align="right">密 码：</td>
							<td align="left"><input type="password" name="password" class="bian"
									size="18"></input></td>
						</tr>
						<tr>
							<td height="24" colspan="2" align="center"><submit
									value="登　录" type="image"
									src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/dl_06.gif" /> <a href="http://www.baidu.com"> <img
									src="<%=request.getContextPath()%>/webapp/WEB-INF/css/images/dl_08.gif" width="68"
									height="24" />
							</a></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<div id="foot"></div>
	</div>
</body>
</html>