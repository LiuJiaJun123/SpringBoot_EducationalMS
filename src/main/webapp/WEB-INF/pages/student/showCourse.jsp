<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
	<title>课程信息显示</title>

	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- 引入bootstrap -->
	<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
	<!-- 引入JQuery  bootstrap.js-->
	<script src="/js/jquery-3.2.1.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>

	<%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>

</head>
<body>
	<!-- 顶栏 -->
	<jsp:include page="top.jsp"></jsp:include>
	<!-- 中间主体 -->
	<div class="container" id="content">
		<div class="row">
			<jsp:include page="menu.jsp"></jsp:include>
			<div class="col-md-10">
				<div class="panel panel-default">
				    <div class="panel-heading">
						<div class="row">
					    	<h1 class="col-md-5">课程列表</h1>
							<form class="bs-example bs-example-form col-md-5" role="form" style="margin: 20px 0 10px 0;" action="/student/searchCourse?page=1&pageSize=4" id="form1" method="post">
								<div class="input-group">
									<input type="text" class="form-control" placeholder="请输入课程名" name="findByName" id="findByName">
									<span class="input-group-addon btn" id="sub">搜索</span>
								</div>
							</form>

						</div>
				    </div>
				    <table class="table table-bordered">
					        <thead>
					            <tr>
									<th>课程号</th>
									<th>课程名称</th>
									<th>授课老师编号</th>
									<th>上课时间</th>
									<th>上课地点</th>
									<th>周数</th>
									<th>课程类型</th>
									<th>学分</th>
									<th>操作</th>
					            </tr>
					        </thead>
					        <tbody>
							<c:forEach  items="${CoursePageInfo.list}" var="item">
								<tr>
									<td>${item.courseid}</td>
									<td>${item.coursename}</td>
									<td>${item.teacherid}</td>
									<td>${item.coursetime}</td>
									<td>${item.classroom}</td>
									<td>${item.courseweek}</td>
									<td>${item.coursetype}</td>
									<td>${item.score}</td>
									<td>
										<button class="btn btn-default btn-xs btn-info" onclick="selectConfirmd(${item.courseid})">选课</button>
										<!--弹出框-->
									</td>
								</tr>
							</c:forEach>
					        </tbody>
				    </table>

					<div class="panel-footer">
						<div class="pull-left">
							<div class="form-group form-inline">
								总共${CoursePageInfo.pages} 页，共${CoursePageInfo.total} 条数据。 每页
								<select class="form-control" id="changPageSize" onchange="changPageSize()">
									<option>请选择</option>
									<option>1</option>
									<option>2</option>
									<option>3</option>
									<option>4</option>
									<option>5</option>
								</select> 条
							</div>
						</div>
					</div>

					<%--分页--%>
					<div class="panel-footer">
						<nav style="text-align: center">
							<ul class="pagination">
								<li>
									<a aria-label="Previous" href="${pageContext.request.contextPath}/student/showCourse?page=1&pageSize=${CoursePageInfo.pageSize}">首页</a>
								</li>
								<li><a href="${pageContext.request.contextPath}/student/showCourse?page=${CoursePageInfo.pageNum-1}&pageSize=${CoursePageInfo.pageSize}">上一页</a></li>

								<c:forEach begin="1" end="${CoursePageInfo.pages}" var="i">
									<c:if test="${i==CoursePageInfo.pageNum}">
										<li><a style="background-color: #2aabd2" href="${pageContext.request.contextPath}/student/showCourse?page=${i}&pageSize=${CoursePageInfo.pageSize}">${i}</a></li>
									</c:if>
									<c:if test="${i!=CoursePageInfo.pageNum}">
										<li><a href="${pageContext.request.contextPath}/student/showCourse?page=${i}&pageSize=${CoursePageInfo.pageSize}">${i}</a></li>
									</c:if>
								</c:forEach>

								<li><a href="${pageContext.request.contextPath}/student/showCourse?page=${CoursePageInfo.pageNum+1}&pageSize=${CoursePageInfo.pageSize}">下一页</a></li>
								<li>
									<a href="${pageContext.request.contextPath}/student/showCourse?page=${CoursePageInfo.pages}&pageSize=${CoursePageInfo.pageSize}" aria-label="Next">尾页</a>
								</li>
							</ul>
						</nav>
					</div>

				</div>

			</div>
		</div>
	</div>
	<div class="container" id="footer">
		<div class="row">
			<div class="col-md-12"></div>
		</div>
	</div>
</body>
	<script type="text/javascript">

		<%--设置菜单--%>
		$("#nav li:nth-child(1)").addClass("active")

        //改变每页显示条数
        function changPageSize(){
            var pageSize=$("#changPageSize").val();
            location.href="${pageContext.request.contextPath}/student/showCourse?page=1&pageSize="+pageSize;
        }

        // 选课确认
        function selectConfirmd(courseid) {
            var msg = "您确定要选修该门课程吗？！";
            if (confirm(msg)==true){
                location.href='/student/stuSelectedCourse?id='+courseid;
                return true;
            }else{
                return false;
            }
        };


        function sleep(numberMillis) {
            var now = new Date();
            var exitTime = now.getTime() + numberMillis;
            while (true) {
                now = new Date();
                if (now.getTime() > exitTime)
                    return;
            }
        }


        $("#sub").click(function () {
            findByName();
        });

        //按键盘的回车键也执行搜索
        $("#findByName").keydown(function () {
            if(event.keyCode==13) {
                findByName();
            }
        });


        // 发送ajax请求，将要查询的名字存入到session中
        function findByName() {
            var username = $("#findByName").val();
            // alert(username)
            var allData={
                username:username
            };

            $.ajax({
                url:"/student/searchCourseName",
                contentType:"application/json;charset=UTF-8",
                data:JSON.stringify(allData),//'{"username":username}',
                dataType:"json",
                type:"post"
            });

            sleep(500)

            $("#form1").submit();
        }
	</script>
</html>