<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>项目管理平台</title>
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="icon" href="static/images/logo.ico" type="image/x-icon" />
	
</head>
<body>
	<!--head start-->
	<div class="topbar">
		<div class="logoBox">
			<img src="static/images/logo.png" />
		</div>
	</div>
	<!--head end-->

	<!--content start-->

	<div class="content">
		<div class="standardBox">
			<div class="leftImage">
				<img src="static/images/content_left-image.png" />
			</div>
			<div class="rightLoginBox">
				<!--自定义样式-->
				<form id="loginForm" action="index/login" method="post">
					<p class="title">用户登录</p>
					<div class="rowBox margin-top-20">
						<div class="iconBox leftRadius">
							<label> <span class="glyphicon glyphicon-user"
								aria-hidden="true"></span>
							</label>
						</div>

						<div class="inputBox">
							<input type="text" class="rightRadius" id="userName"
								name="userName" placeholder="请输入用户名" />
						</div>

					</div>

					<div class="rowBox margin-top-20">
						<div class="iconBox leftRadius">
							<label> <span class="glyphicon glyphicon-lock"
								aria-hidden="true"></span>
							</label>
						</div>

						<div class="inputBox">
							<input type="password" class="rightRadius" id="password"
								name="password" placeholder="请输入密码" />
						</div>

					</div>

					<div class="loginButtonbox margin-top-20">
						<button type="button" id="btn-login">
							<b>登 录</b>
						</button>
					</div>

					<div class="findPass margin-top-20">
						<span>如登录异常,请清理浏览器缓存后再试。</span>
						<button type="button"  id="btn-resetpwd">忘记密码？</button>
					</div>

				</form>

			</div>
		</div>
	</div>

	<!--content end-->

	<div class="footbar">
		<p>技术支持：广东健康在线信息技术有限公司</p>
	</div>
<div class="row">

	<div class="modal fade" id="datamodal">
		<form id="dataform" method="post" class="form-horizontal" action="">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">重置密码</h4>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<div class="form-group-input">
								<label for="userName" class="col-xs-3 control-label">用户名</label>
								<div class="col-xs-5">
									<input autocomplete="off" type="text" name="userName"
										class="form-control" placeholder="请输入用户名" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="form-group-input">
								<label for="mail" class="col-xs-3 control-label">邮箱：</label>
								<div class="col-xs-5">
									<input autocomplete="off" type="text" name="mail"
										class="form-control" placeholder="请输入邮箱信息" />
								</div>
							</div>
						</div>
					</div>

					<div class="modal-footer">
						<div class="form-group" style="text-align: center">
							<button type="button" id="submit2" class="btn btn-success"
								id="btnUserSave">确认</button>
							<button type="button" class="btn btn-info" data-dismiss="modal">
								取消</button>
						</div>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</form>
	</div>
	<!-- /.modal --></div>

</body>
<script type="text/javascript" src="static/js/modular.js" data-config="static/js/config.js" data-main="login"></script>

</html>