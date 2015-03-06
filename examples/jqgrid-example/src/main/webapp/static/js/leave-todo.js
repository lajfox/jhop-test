

/**
 * 加载详细信息
 * @param {Object} id
 */
function loadDetail(id, withVars, callback) {
    var dialog = this;
    $.getJSON(ctx + '/oa/leave/generic/get/' + id, function(data) {
       
        $.each(data.userdata, function(k, v) {			
	         $('.view-info td[name=' + k + ']', dialog).text(v);			
        });
		if ($.isFunction(callback)) {
			callback(data);
		}
    });
}

/**
 * 加载详细信息，同时读取流程任务变量
 * @param {Object} id
 */
function loadDetailWithTaskVars(leaveId, taskId, callback) {
    var dialog = this;
    $.getJSON(ctx + '/oa/leave/generic/task/vars/' +leaveId+'/'+taskId, function(data) {
       
        $.each(data.userdata.business, function(k, v) {            
	         $('.view-info td[name=' + k + ']', dialog).text(v);			
        });
		if ($.isFunction(callback)) {
			callback(data);
		}
    });
}

/**
 * 完成任务
 * @param {Object} taskId
 */
function complete(slef,businessKey,processInstanceId,taskId, variables) {
   	
	// 发送任务完成请求
	$.ajax({
		url: ctx + '/oa/leave/generic/task/complete/'+businessKey+'/'+ taskId,
		type: "POST",
		dataType: "json",	
		contentType:"application/json",
		data:JSON.stringify(variables),
		success : function (data) {
	        if (data.success) {            
	        	$.jgrid.info_dialog2('提示',data.message,{zIndex:2000});
	            try{
	            	reload(data);
	            }catch(e){  } 
	            $(slef).dialog('close');
	        } else {           
	            $.jgrid.error_dialog2('提示',data.message,{zIndex:2000});
	        }
		},
		error:function(){
			 $.jgrid.error_dialog2('提示','系统错误，操作失败',{zIndex:2000});
		}
	});	
	
}


/*
 * 使用json方式定义每个节点的按钮
 * 以及按钮的功能
 * 
 * open:打开对话框的时候需要处理的任务
 * btns:对话框显示的按钮
 */
var handleOpts = {
	deptLeaderAudit: {
		width: 300,
		height: 300,
		open: function(id) {
			
			// 打开对话框的时候读取请假内容
			loadDetail.call(this, id);
		},
		btns: [{
			text: '同意',
			click: function() {
				var businessKey = $(this).data('businessKey');
				var processInstanceId = $(this).data('processInstanceId');
				var taskId = $(this).data('taskId');
								
				// 设置流程变量
				complete(this,businessKey,processInstanceId,taskId, [{
					key: 'deptLeaderPass',
					value: true,
					type: 'B'
				},
				{
					key: 'deptLeaderAction',
					value: '同意',
					type: 'S'
				}				
				]);
			}
		}, {
			text: '驳回',
			click: function() {
				
				var businessKey = $(this).data('businessKey');
				var processInstanceId = $(this).data('processInstanceId');
				var taskId = $(this).data('taskId');
				
				$('<div/>', {
					title: '填写驳回理由',
					html: "<textarea id='leaderBackReason' style='width: 250px; height: 60px;'></textarea>"
				}).dialog({
					modal: true,
					open: function() {
						
					},
					buttons: [{
						text: '驳回',
						click: function() {
							var leaderBackReason = $.trim($('#leaderBackReason').val());
							if (!leaderBackReason) {								
								$.jgrid.error_dialog2('提示','请输入驳回理由！',{zIndex:2000});
								return;
							}
							
							// 设置流程变量
							complete(this,businessKey,processInstanceId,taskId, [{
								key: 'deptLeaderPass',
								value: false,
								type: 'B'
							}, {
								key: 'leaderBackReason',
								value: leaderBackReason,
								type: 'S'
							},{
								key: 'deptLeaderAction',
								value: '驳回',
								type: 'S'
							}	
							]);
						}
					}, {
						text: '取消',
						click: function() {
							$(this).dialog('close');
							$('#deptLeaderAudit').dialog('close');
						}
					}]
				});
			}
		}, {
			text: '取消',
			click: function() {
				$(this).dialog('close');
			}
		}]
	},
	hrAudit: {
		width: 300,
		height: 300,
		open: function(id) {
			// 打开对话框的时候读取请假内容
			loadDetail.call(this, id);
		},
		btns: [{
			text: '同意',
			click: function() {
				
				var businessKey = $(this).data('businessKey');
				var processInstanceId = $(this).data('processInstanceId');
				var taskId = $(this).data('taskId');
				
				// 设置流程变量
				complete(this,businessKey,processInstanceId,taskId, [{
					key: 'hrPass',
					value: true,
					type: 'B'
				},{
					key: 'hrAction',
					value: '同意',
					type: 'S'
				}
				]);
			}
		}, {
			text: '驳回',
			click: function() {
				
				var businessKey = $(this).data('businessKey');
				var processInstanceId = $(this).data('processInstanceId');
				var taskId = $(this).data('taskId');
				
				$('<div/>', {
					title: '填写驳回理由',
					html: "<textarea id='hrBackReason' style='width: 250px; height: 60px;'></textarea>"
				}).dialog({
					modal: true,
					buttons: [{
						text: '驳回',
						click: function() {
							var hrBackReason = $.trim($('#hrBackReason').val());
							if (!hrBackReason) {
								$.jgrid.error_dialog2('提示','请输入驳回理由！',{zIndex:2000});
								return;
							}
							
							// 设置流程变量
							complete(this,businessKey,processInstanceId,taskId, [{
								key: 'hrPass',
								value: false,
								type: 'B'
							}, {
								key: 'hrBackReason',
								value: hrBackReason,
								type: 'S'
							},{
								key: 'hrAction',
								value: '驳回',
								type: 'S'
							}
							]);
						}
					}, {
						text: '取消',
						click: function() {
							$(this).dialog('close');
							$('#deptLeaderAudit').dialog('close');
						}
					}]
				});
			}
		}, {
			text: '取消',
			click: function() {
				$(this).dialog('close');
			}
		}]
	},
	modifyApply: {
		width: 500,
		height: 470,
		open: function(id, taskId) {
			var dialog = this;
			
			$('#startTime,#endTime', this).datetimepicker({
	            stepMinute: 5
	        });
			
			// 打开对话框的时候读取请假内容
			loadDetailWithTaskVars.call(this, id, taskId, function(data) {
				
				// 显示驳回理由
				$('.info').show().html("<p><b>领导：</b>" + (data.userdata.process.leaderBackReason || "") + "<p/><p><b>人事：</b>" + (data.userdata.process.hrBackReason || "")+"</p>");
								
				// 读取原请假信息
				$('#modifyApplyContent #leaveType option[value=' + data.userdata.business.leaveType + ']').attr('selected', true);
				$('#modifyApplyContent #startTime').val(data.userdata.business.startTime);
				$('#modifyApplyContent #endTime').val(data.userdata.business.endTime);
				$('#modifyApplyContent #reason').val(data.userdata.business.reason);
				
			});
			
			// 切换状态
			$("#radio").buttonset().change(function(){
				var type = $(':radio[name=reApply]:checked').val();
				if (type == 'true') {
					$('#modifyApplyContent').show();
				} else {
					$('#modifyApplyContent').hide();
				}
			});
		},
		btns: [{
			text: '提交',
			click: function() {
				
				var businessKey = $(this).data('businessKey');
				var processInstanceId = $(this).data('processInstanceId');
				var taskId = $(this).data('taskId');
				var reApply = $(':radio[name=reApply]:checked').val();
				var reApply_text = $('#reApply_'+reApply).html();
				
				// 提交的时候把变量
				complete(this,businessKey,processInstanceId,taskId, [{
					key: 'reApply',
					value: reApply,
					type: 'B'
				}, 
				{
					key: 'reApply_text',
					value: reApply_text,
					type: 'S'
				}, 
				{
					key: 'leaveType',
					value: $('#modifyApplyContent #leaveType').val(),
					type: 'S'
				}, {
					key: 'startTime',
					value: $('#modifyApplyContent #startTime').val(),
					type: 'D'
				}, {
					key: 'endTime',
					value: $('#modifyApplyContent #endTime').val(),
					type: 'D'
				}, {
					key: 'reason',
					value: $('#modifyApplyContent #reason').val(),
					type: 'S'
				}]);
			}
		},{
			text: '取消',
			click: function() {
				$(this).dialog('close');
			}
		}]
	},
	reportBack: {
		width: 400,
		height: 400,
		open: function(id, taskId) {
			// 打开对话框的时候读取请假内容
			loadDetail.call(this, id, taskId);
			$('#realityStartTime,#realityEndTime').datetimepicker({
	            stepMinute: 5
	        });
		},
		btns: [{
			text: '提交',
			click: function() {
				var realityStartTime = $('#realityStartTime').val();
				var realityEndTime = $('#realityEndTime').val();
				
				if (realityStartTime == '') {
					//alert('请选择实际开始时间！');
					$.jgrid.info_dialog2('提示','请选择实际开始时间！');	
					return;
				}
				
				if (realityEndTime == '') {
					//alert('请选择实际结束时间！');
					$.jgrid.info_dialog2('提示','请选择实际结束时间！');
					return;
				}
				
				var businessKey = $(this).data('businessKey');
				var processInstanceId = $(this).data('processInstanceId');
				var taskId = $(this).data('taskId');
				complete(this,businessKey,processInstanceId,taskId, [{
					key: 'realityStartTime',
					value: realityStartTime,
					type: 'D'
				}, {
					key: 'realityEndTime',
					value: realityEndTime,
					type: 'D'
				}]);
			}
		},{
			text: '取消',
			click: function() {
				$(this).dialog('close');
			}
		}]
	}
};

/**
 * 办理流程
 * @param {Object} tkey 当前节点的英文名称
 * @param {Object} tname 当前节点的中文名称
 * @param {Object} rowId 请假记录ID
 * @param {Object} pid 流程实例ID
 * @param {Object} taskId 任务ID
 */
function handle(tkey,tname,rowId,pid,taskId) {
	
	// 使用对应的模板
	$('#' + tkey).data({
		businessKey:rowId,
		processInstanceId:pid,
		taskId: taskId
	}).dialog({
		title: '流程办理[' + tname + ']',
		modal: true,
		width: handleOpts[tkey].width,
		height: handleOpts[tkey].height,
		open: function() {
			handleOpts[tkey].open.call(this, rowId, taskId);
		},
		buttons: handleOpts[tkey].btns
	});
}

