<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		//打开模态窗口操作
		$("#addBtn").click(function () {


			//查所有用户信息列表，为了注入模态窗口
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data) {
					//data:[{用户1 id name ……},{用户2},……]

					var html = ""
					$.each(data,function (i,n) {
						//n：每个元素
						html += "<option value='"+ n.id +"'>"+ n.name +"</option>"
					})
					$("#create-owner").html(html);
					$("#create-owner").val("${sessionScope.user.id}")		//下拉列表默认

					$("#createActivityModal").modal("show");			//展示模态窗口
				}
			})

		})

		//模态窗口的保存操作
		$("#saveBtn").click(function () {



			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner":$.trim($("#create-owner").val()),
					"name":$.trim($("#create-name").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val()),
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					//data:{success:true/false}
					if(data.success){
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));//添加成功刷新市场活动列表2.
						$("#activityAddForm")[0].reset()			//重置表单
						$("#createActivityModal").modal("hide");//关闭添加操作模态窗口
					}else {
						alert("添加失败")
					}
				}
			})
		})

		//点击搜索
		$("#searchBtn").click(function () {
			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-startDate").val($.trim($("#search-startDate").val()))
			$("#hidden-endDate").val($.trim($("#search-endDate").val()))
			pageList(1,2)		//6.


		})

		//点击全选按钮
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked)
		})
		//反全选		动态生的元素不能以普通绑定事件的形式操作，要以on的形式触发数据	$(需要绑定事件元素的有效外层元素).on(绑定时间的方式,需要绑定的元素的jquery对象,回调)
		$("#activityBody").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length)
		})

		//点击删除按钮
		$("#deleteBtn").click(function () {
			var $xz = $("input[name=xz]:checked");
			if($xz.length == 0){
				alert("请选择需要删除的记录")
			}else {
				if(confirm("确定删除选择的信息吗")){
					var param = ""
					for (var i=0;i<$xz.length;i++){
						param += "id=" + $($xz[i]).val()
						if(1<$xz.length-1){
							param += "&"
						}
					}
					$.ajax({
						url:"workbench/activity/delete.do" ,
						data:param,
						type:"post",
						dataType:"json",
						success:function (data) {
							//{"success":true/false}
							if(data.success){
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));//删除成功刷新市场活动列表 4.
							}else {
								alert("删除失败")
							}
						}
					})
				}
			}
		})


		//点击修改模态窗口操作
		$("#editBtn").click(function () {
			var $xz = $("input[name=xz]:checked")
			if($xz.length == 0){
				alert("请选择需要修改的信息")
			}else if($xz.length >1){
				alert("只能选择一条信息修改")
			}else {
				var id =  $xz.val()
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						"id":id
					},
					type:"get",
					dataType:"json",
					success:function (data) {
						//{"userList":[{用户1},{2},……]	"activity":{市场活动}    }


						/*处理下拉框*/
						var html = ""
						$.each(data.userList,function (i,n) {
							//n：每个元素
							html += "<option value='"+ n.id +"'>"+ n.name +"</option>"
						})
						$("#edit-owner").html(html);


						/*处理单条activity*/
						$("#edit-name").val(data.activity.name)
						$("#edit-owner").val(data.activity.owner)
						$("#edit-startDate").val(data.activity.startDate)
						$("#edit-endDate").val(data.activity.endDate)
						$("#edit-cost").val(data.activity.cost)
						$("#edit-description").val(data.activity.description)
						$("#edit-id").val(data.activity.id)




						//展示模态窗口
						$("#editActivityModal").modal("show");


					}
				})
			}
		})

		//点击更新按钮
		$("#updateBtn").click(function () {
			if(confirm("确定修改信息吗")){
				$.ajax({
					url:"workbench/activity/update.do",
					data:{
						"owner":$.trim($("#edit-owner").val()),
						"name":$.trim($("#edit-name").val()),
						"startDate":$.trim($("#edit-startDate").val()),
						"endDate":$.trim($("#edit-endDate").val()),
						"cost":$.trim($("#edit-cost").val()),
						"description":$.trim($("#edit-description").val()),
						"id":$.trim($("#edit-id").val()),
					},
					type:"post",
					dataType:"json",
					success:function (data) {
						//data:{success:true/false}
						if(data.success){
							pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
									,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));//修改成功刷新市场活动列表 3.

							$("#editActivityModal").modal("hide");//关闭修改操作模态窗口
						}else {
							alert("修改失败")
						}
					}
				})
			}

		})


		pageList(1,2)		//刷新展示数据	1.


		
	});


	/*pageNo:当前页码			pageSize:每页展示几条
		1	点击市场活动	.
		2.	添加信息	.
		3.	修改信息	.
		4.	删除信息	.
		5.	点击分页		.
		6.	查询信息  .
	*/
	function pageList(pageNo,pageSize){
		$("#qx").prop("checked",false)			//全选取消

		$("#search-name").val($.trim($("#hidden-name").val()))
		$("#search-owner").val($.trim($("#hidden-owner").val()))
		$("#search-startDate").val($.trim($("#hidden-startDate").val()))
		$("#search-endDate").val($.trim($("#hidden-endDate").val()))

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"endDate":$.trim($("#search-endDate").val()),
				"startDate":$.trim($("#search-startDate").val()),
			},
			type:"get",
			dataType:"json",
			success:function (data) {
				//data		{"total":100,"dataList":[{市场活动1},{市场活动2},……]}
				var html = "";
				$.each(data.dataList,function (i,n) {
					html +=( '<tr class="active">'+
							'<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>'+
							'<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'+
							'<td>'+n.owner+'</td>'+
							'<td>'+n.startDate+'</td>'+
							'<td>'+n.endDate+'</td>'+
							'</tr>')
				})

				$("#activityBody").html(html)

				var totalPages = data.total%pageSize ==0 ?data.total/pageSize : parseInt(data.total/pageSize)+1;
				//分页插件使用
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数
					visiblePageLinks: 3, // 显示几个卡片
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})

	}
	
</script>
</head>
<body>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">


	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label ">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">



								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" readonly >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" readonly>
							</div>
						</div>

						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate"  />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus" ></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
				<%--		<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>