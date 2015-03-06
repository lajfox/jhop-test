/**
 * 工作流程处理公共函数库
 */
(function($) {

	 /**
	  * form对应的string/date/long/enum/boolean类型表单组件生成器
	  * fp_的意思是form paremeter
	  */
	 var formFieldCreator = {
	 	string: function(formData, prop, className) {
	 		var val = $.workflow.defaultString(prop.value);	 	
	 		var result = "<td width='120' class='CaptionTD'>" + prop.name + "：</td>";
	 		if (prop.writable === true) {
	 			result += "<td class='DataTD'><input type='text' id='" + prop.id + "' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all' value='"+val+"'/>";
			} else {
				result += "<td class='DataTD'>" + val;
			}
	 		return result;
	 	},
	 	date: function(formData, prop, className) {
	 		var val = $.workflow.defaultString(prop.value);	 		
	 		var result = "<td class='CaptionTD'>" + prop.name + "：</td>";
	 		if (prop.writable === true) {
	 			result +="<td class='DataTD'><input type='text' id='" + prop.id + "' name='fp_" + prop.id + "' class='dateISO " + className + " FormElement ui-widget-content ui-corner-all' value='"+val+"' />";
			} else {
				result += "<td class='DataTD'>" + val;
			}
	 		return result;
	 	},
	 	datetime: function(formData, prop, className) {
	 		var val = $.workflow.defaultString(prop.value);	
	 		var result = "<td class='CaptionTD'>" + prop.name + "：</td>";
	 		if (prop.writable === true) {
	 			result +="<td class='DataTD'><input type='text' id='" + prop.id + "' name='fp_" + prop.id + "' class='datetimeISO " + className + " FormElement ui-widget-content ui-corner-all' value='"+val+"'/>";
			} else {
				result += "<td class='DataTD'>" + val;
			}
	 		return result;
	 	},		
	 	'enum': function(formData, prop, className) {
	 		
	 		var result = "<td width='120' class='CaptionTD'>" + prop.name + "：</td>";
	 		if(prop.writable === true) {
	 			result += "<td class='DataTD'><select id='" + prop.id + "' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all'>";
	 						
	 			$.each(formData[prop.id], function(k, v) {
	 				if(k == prop.value){
	 					result += "<option value='" + k + "' selected='selected' >" + v + "</option>";
	 				}else{
	 					result += "<option value='" + k + "'>" + v + "</option>";
	 				}
	 			});
	 			 
	 			result += "</select>";
	 		} else {
	 			result += "<td class='DataTD'>" + formData[prop.id][prop.value];
	 		}
	 		return result;
	 	},
	 	'select': function(formData, prop, className) {
	 		
	 		var result = "<td width='120' class='CaptionTD'>" + prop.name + "：</td>";
	 		if(prop.writable === true) {
	 			result += "<td class='DataTD'><select id='" + prop.id + "' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all'>";
	 						
	 			$.each(formData[prop.id], function(k, v) {
	 				if(k == prop.value){
	 					result += "<option value='" + k + "' selected='selected' >" + v + "</option>";
	 				}else{
	 					result += "<option value='" + k + "'>" + v + "</option>";
	 				}
	 			});
	 			 
	 			result += "</select>";
	 		} else {
	 			result += "<td class='DataTD'>" + formData[prop.id][prop.value];
	 		}
	 		return result;
	 	},	
	 	'multipleselect': function(formData, prop, className) {
	 		
	 		var result = "<td width='120' class='CaptionTD'>" + prop.name + "：</td>";
	 		if(prop.writable === true) {
	 			result += "<td class='DataTD'><select multiple='true' style='width:100px;' id='" + prop.id + "' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all'>";
	 						
				var keys = prop.value ? prop.value.split(',') : [];	
				
				$.each(formData[prop.id], function(k, v) {
					if( $.inArray(k,keys) != -1 ){
						result += "<option selected='selected' value='" + k + "'>" + v + "</option>";
					}else{
						result += "<option value='" + k + "'>" + v + "</option>";
					}
				});
	 			 
	 			result += "</select>";
	 		} else {
	 			var keys = prop.value.split(',');
	 			var v = $.map(keys,function(key){
	 				return formData[prop.id][key];
	 			}); 
	 			result += "<td>" + v;
	 		}
	 		return result;
	 	},	
	 	'radio': function(formData, prop, className) {
	 		
	 		var result = "<td width='120' class='CaptionTD'>" + prop.name + "：</td>";
	 		if(prop.writable === true) {
	 			result += "<td class='DataTD'>";
	 						
	 			$.each(formData[prop.id], function(k, v) {
					if(k == prop.value){
						result += "<input type='radio' checked='checked' id='" + prop.id + "' value='"+k+"' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all' />"+ v;
					}else{
						result += "<input type='radio' id='" + prop.id + "' value='"+k+"' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all' />"+ v;
					}
	 			});
	 			 
	 			
	 		} else {
	 			result += "<td>" + formData[prop.id][prop.value];
	 		}
	 		return result;
	 	},	
	 	'checkbox': function(formData, prop, className) {
	 		
	 		var result = "<td width='120' class='CaptionTD'>" + prop.name + "：</td>";
	 		if(prop.writable === true) {
	 			result += "<td class='DataTD'>";
	 						
	 			var keys = prop.value ? prop.value.split(',') : [];	
				
				$.each(formData[prop.id], function(k, v) {
					if( $.inArray(k,keys) != -1 ){
						result += "<input type='checkbox' checked='checked' value='"+k+"' id='" + prop.id + "' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all' />"+ v;
					}else{
						result += "<input type='checkbox' value='"+k+"' id='" + prop.id + "' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all' />"+ v;
					}
				});
	 			 
	 			
	 		} else {
	 			var keys = prop.value.split(',');
	 			var v = $.map(keys,function(key){
	 				return formData[prop.id][key];
	 			}); 
	 			result += "<td>" + v;
	 		}
	 		return result;
	 	},	
	 	hidden: function(formData, prop, className) {
	 		var val = $.workflow.defaultString(prop.value);	
	 		var result = "<td sytle='display:none;' colspan='2'><input type='hidden' id='" + prop.id + "' name='fp_" + prop.id + "' value='"+val+"' />";
	 		return result;
	 	},
	 	textarea: function(formData, prop, className) {
	 		var val = $.workflow.defaultString(prop.value);	
	 		var result = "<td width='120' class='CaptionTD'>" + prop.name + "：</td>";
	 		if(prop.writable === true) {
	 			result +="<td class='DataTD'><textarea  id='" + prop.id + "' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all' >"+val+"</textarea>";
	 		}else{
	 			result += "<td class='DataTD'>" + val;
	 		}	 		
	 		return result;
	 	},
	 	'users': function(formData, prop, className) {
	 		var val = $.workflow.defaultString(prop.value);	
	 		var result = "<td width='120' class='CaptionTD'>" + prop.name + "：</td>";
	 		if(prop.writable === true) {
	 			result +="<td class='DataTD'><input type='text' id='" + prop.id + "' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all' value='"+val+"'/>";
	 		}else{
	 			result += "<td class='DataTD'>" + val;
	 		}
	 		return result;
	 	},
	 	'user': function(formData, prop, className) {
	 		var val = $.workflow.defaultString(prop.value);	
	 		var result = "<td width='120' class='CaptionTD'>" + prop.name + "：</td>";
	 		if(prop.writable === true) {
	 			result +="<td class='DataTD'><input type='text' id='" + prop.id + "' name='fp_" + prop.id + "' class='" + className + " FormElement ui-widget-content ui-corner-all' value='"+val+"'/>";
	 		}else{
	 			result += "<td class='DataTD'>" + val;
	 		}
	 		return result;
	 	},
	 	'custom': function(formData, prop, className) {	 
	 		
	 		var method = 'customFormField_'+prop.id;	 		
	 		var fn = null;	 		
	 		try{
	 			fn = this[method] || window[method] || eval(method);
	 		}catch(e){}
	 		
	 		if(fn && $.isFunction(fn)){
	 			return fn.apply(window,[formData, prop, className]);
	 		}else {
	 			var method = 'customFormField';	 			
		 		fn = this[method] || window[method] || eval(method);		 		
	 			if(fn && $.isFunction(fn)){
		 			return fn.apply(window,[formData, prop, className]);
		 		}
	 		}	 		
	 	}	 	
	 };
	 
	 $.workflow = {
			 
		showSubmitButtons:function(){
			$('#submitbtn').show();	
			$('#cancelbtn').show();		
		},
		
		hideSubmitButtons:function(){							
			$('#submitbtn').hide();	
			$('#cancelbtn').hide();
		},			 
			 
		defaultString:function(str){
			if(!str || str == null || str == "null" || str == undefined || str == "undefined"){
				str = "";
			}
			return str;
		},
		
		showButtons:function(buttons){
			if(buttons){
				$.each(buttons,function(i,item){
					if(item.show){
						$('#'+item.name).show();
					}else{
						$('#'+item.name).hide();
					}
				});
			}			
		},
		
		hideButtons:function(buttons){
			if(buttons){
				$.each(buttons,function(i,item){					
					$('#'+item.name).hide();					
				});
			}			
		},		
		
		getButtonName:function(buttons,type){
			var name;
			if(buttons){
				$.each(buttons,function(i,item){
					if(item.type == type){
						name = item.name;
						return false;
					}
				});
			}
			return name;
		},
		
		showButtons2 :function(buttons,type){  
			$('#'+$.workflow.getButtonName(buttons,type)).show();
			
			buttons = $.grep(buttons,function(val,i){				
	    		return val.type != type;
	    	});	
			
			$.workflow.hideButtons(buttons);
		},
		
		/**
		 * 判断当前用户是否有权限外理
		 * 参数 opt
		 * {
		 * gridId:'',
		 * businessKey:'',
		 * processDefinitionKey:'',//流程定义唯一标识
		 * buttons:[
		 * { name:'',type:'start'},
		 * { name:'',type:'complete'},
		 * { name:'',type:'unclaim'},
		 * { name:'',type:'delegate'},
		 * { name:'',type:'resolve'},
		 * { name:'',type:'claim'}
		 * ],
		 * start:true|false //启动流程是否需要权限		
		 * }
		 */
		decision : function(opt){ 
			var gird = $('#'+opt.gridId);
			var ret = gird.jqGrid('getRowData',opt.businessKey);	
			if(ret.processInstanceId){ 
				var name = $.workflow.getButtonName(opt.buttons,'start');
				$('#'+name).hide();	 
				if(ret.tasks){
					//确定用户是否有权处理当前任务
					$.workflow.task.decision(opt.gridId, opt.businessKey, {
						success:function(data){	 
							gird.jqGrid('setRowData',opt.businessKey,data.userdata);  
							if (parseInt(data.statusCode,10) == 1){					
								//办理任务					
								$('#'+$.workflow.getButtonName(opt.buttons,'complete')).show();	
								
								ret = gird.jqGrid('getRowData',opt.businessKey);	
								
								name = $.workflow.getButtonName(opt.buttons,'unclaim');
								if(name && $.workflow.task.isUnclaim(ret.taskinfo)){
									$('#'+name).show();								
								}
								
								name = $.workflow.getButtonName(opt.buttons,'delegate');
								if(name && $.workflow.task.isDelegate(ret.taskinfo)){
									$('#'+name).show();
								}
								
								//name = $.workflow.getButtonName(opt.buttons,'resolve');
								//if(name && $.workflow.task.isResolve(ret.taskinfo)){
								//	$('#'+name).show();
								//}
								
								name = $.workflow.getButtonName(opt.buttons,'claim'); 
								$('#'+name).hide();
								
							}else if(parseInt(data.statusCode,10) == 2){
								//签收任务	
								$.workflow.showButtons2(opt.buttons,'claim');								
							}else if(parseInt(data.statusCode,10) == 3){
								//取消委派	
								$.workflow.showButtons2(opt.buttons,'resolve');								
							}else{
								//无权处理	
								$.workflow.hideButtons(opt.buttons);								
							}
						}
					});				
				}else{
					//流程己结束
					$.workflow.hideButtons(opt.buttons);
				}
				
			}else{ 
				if(opt.start){
					//确定用户是否有权启动流程	
					$.workflow.procdef.decision(opt.processDefinitionKey, {
						success:function(data){			
							
							if (data.success){					
								//有权启动流程	
								$.workflow.showButtons2(opt.buttons,'start');							
							}else{
								//没有有权启动流程					
								$.workflow.hideButtons(opt.buttons);
							}
						}
					});						
				}else{
					$.workflow.showButtons2(opt.buttons,'start');	
				}		
			}		
		}
	 };  
	 
	 $.workflow.task = {
		 /**
		  * 反序列化任务
		  * 
		  * @param tasks
		  *            任务json串
		  */
		 deserializeTasks : function(tasksJson) {
		 	return tasksJson ? $.parseJSON(tasksJson) : null;
		 },	
		 
		 /**
		  * 是否可以取消签收任务
		  */
		 isUnclaim : function(taskJson){			
			var task = $.workflow.task.deserializeTasks(taskJson);			
			return ( task && task.claim  && (!task.owner || task.owner == task.assignee));
		 },
		 
		 
		 /**
		  * 是否可以委派任务给他人
		  */
		 isDelegate : function(taskJson){
			var task = $.workflow.task.deserializeTasks(taskJson);
			return (task && (!task.delegationState || task.delegationState == 'RESOLVED') );
		 },
		 
		 /**
		  * 是否可以取消委派任务
		  */
		 isResolve : function(taskJson){
			var task = $.workflow.task.deserializeTasks(taskJson);
			return (task && task.delegationState && task.delegationState == 'PENDING' );
		 },			 
	 
		 /**
		  *任务当前处理节点
		  */
	 	 getActivityNames : function(value, options, rData){
			var val = "";
			var tasks = rData['tasks'];
			if(tasks){
				var vals  = new Array();
				tasks = $.parseJSON(tasks);   						
				$.each(tasks,function(i,item){   							
					if(item.name && $.inArray(item.name,vals) == -1){   							
						vals.push(item.name);
					}
				});
				
				if(vals.length > 0){
					val = vals.join(",");
				}
			}
			return val;
		},
		 
		/**
		 * 当前处理人／处理角色
		 */
		  getAssignees : function(value, options, rData){
				var val = "";	
				var tasks = rData['tasks'];
				if(tasks){
					var vals  = new Array();
					tasks = $.parseJSON(tasks);   						
					$.each(tasks,function(i,item){   							
						if(item.displayAssignee && $.inArray(item.displayAssignee,vals) == -1){   							
							vals.push(item.displayAssignee);
						}else if(item.candidateGroups){   								
							$.each(item.candidateGroups,function(j,group){
								if($.inArray(group,vals) == -1){
									vals.push(group);
								}
							});
						}else if(item.candidateUsers){   								
							$.each(item.candidateUsers,function(k,user){
								if($.inArray(user,vals) == -1){
									vals.push(user);
								}
							});
						}
					});
					
					if(vals.length > 0){
						val = vals.join(",");
					}
				}	 				
									
				return val;
			},
			
			/**
			 * 任务执行ＩＤ
			 */
			 getExecutionId:function(tasksJson){
				var val = "";
				var tasks = $.workflow.task.deserializeTasks(tasksJson);	
				if(tasks){
					var vals  = new Array();
					$.each(tasks,function(i,item){   	
						if(item.executionId  && $.inArray(item.executionId,vals) == -1){   							
							vals.push(item.executionId);
						}
					});
					
					if(vals.length > 0){
						val = vals.join(",");
					}
				}
				return val;
			},
			
			/**
			 * 确定用户是否有权处理当前任务
			 */
			decision:function(gridId,rowid,ajaxoptions){ 
				var ret = $("#"+gridId).jqGrid('getRowData',rowid);	
				if(ret.tasks){
					$.ajax($.extend({
						url:ctx+'/workflow/task/decision',
						data:{tasks:ret.tasks},
						type: "post",
						dataType: "json"
					},ajaxoptions || {}));
				}
			},
			
			/**
			 * 签收任务
			 * 参数：opt
			 * {
			 * gridId:'', //jqgrid 表格ID				 * 
			 * bussessKey:'',		
			 * path:'', //业务Controller配置url
			 * buttons:[{name:'',show:false},{name:'',show:true},...] //操作图标ID
			 * ajaxopts:{},//jquery ajax请求参数
			 * callback:function(data){} //回调函数，成功时调用
			 * }
			 */
			claim : function (opt){
				
				var grid = $("#"+opt.gridId);
				
				var rowid = grid.jqGrid('getGridParam','selrow');
				var bussessKey = opt.bussessKey ? opt.bussessKey : rowid; 				
				
				if(rowid){	
					var ret = grid.jqGrid('getRowData',rowid);	
					var task = $.workflow.task.deserializeTasks(ret.taskinfo);
					
					$.ajax($.extend({
						url: ctx+'/'+opt.path+'/generic/task/claim/'+bussessKey+'/'+task.id,
						type: "POST",
						dataType: "json",				
						success : function (data) {
							if(data.success){
								$.jgrid.info_dialog2('提示',data.message);
								
								grid.jqGrid('setRowData',rowid,data.userdata);
								
								$.workflow.showButtons(opt.buttons);
								
 								if (opt.callback && $.isFunction(opt.callback)) {
 									opt.callback(data);
 								}						
							}else{
								$.jgrid.error_dialog2('提示',data.message);
							}
						},
 						error:function(xhr, textStatus, errorThrown){
 							$.jgrid.error_dialog2('提示',"系统错误，签收任务失败！");
 						}
					},opt.ajaxopts || {}));			
				} else {			
					$.jgrid.info_dialog2('注意',"请选择记录");	
				}					
			},
			
			/**
			 * 取消签收任务
			 * 参数：opt
			 * {
			 * gridId:'', //jqgrid 表格ID			
			 * path:'', //业务Controller配置url
			 *  bussessKey:'',	
			 * buttons:[{name:'',show:false},{name:'',show:true},...] //操作图标ID
			 * ajaxopts:{},//jquery ajax请求参数
			 * callback:function(data){} //回调函数，成功时调用
			 * }
			 */
			unclaim : function (opt){
				
				var grid = $("#"+opt.gridId);
				var rowid = grid.jqGrid('getGridParam','selrow');
				var bussessKey = opt.bussessKey ? opt.bussessKey : rowid; 	
				
				if(rowid){	
					var ret = grid.jqGrid('getRowData',rowid);	
					var task = $.workflow.task.deserializeTasks(ret.taskinfo); 
					$.ajax($.extend({
						url: ctx+'/'+opt.path+'/generic/task/unclaim/'+bussessKey+'/'+task.id,
						type: "POST",
						dataType: "json",				
						success : function (data) {
							if(data.success){
								$.jgrid.info_dialog2('提示',data.message);
								
								grid.jqGrid('setRowData',rowid,data.userdata);
								
								$.workflow.showButtons(opt.buttons);
								
 								if (opt.callback && $.isFunction(opt.callback)) {
 									opt.callback(data);
 								}						
							}else{
								$.jgrid.error_dialog2('提示',data.message);
							}
						},
 						error:function(xhr, textStatus, errorThrown){
 							$.jgrid.error_dialog2('提示',"系统错误，取消签收任务失败！");
 						}
					},opt.ajaxopts || {}));			
				} else {			
					$.jgrid.info_dialog2('注意',"请选择记录");	
				}					
			},
			
			/**
			 * 取消委派
			 * 参数：opt
			 * {
			 * gridId:'', //jqgrid 表格ID			
			 * path:'', //业务Controller配置url
			 * buttons:[{name:'',show:false},{name:'',show:true},...] //操作图标ID
			 * ajaxopts:{},//jquery ajax请求参数
			 * callback:function(data){} //回调函数，成功时调用
			 * }
			 */
			resolve : function (opt){
				
				var grid = $("#"+opt.gridId);
				var rowid = grid.jqGrid('getGridParam','selrow');
				
				if(rowid){	
					var ret = grid.jqGrid('getRowData',rowid);	
					var task = $.workflow.task.deserializeTasks(ret.taskinfo);
					
					$.ajax($.extend({
						url: ctx+'/'+opt.path+'/generic/task/resolve/'+rowid+'/'+task.id,
						type: "POST",
						dataType: "json",				
						success : function (data) {
							if(data.success){
								$.jgrid.info_dialog2('提示',data.message);
								
								grid.jqGrid('setRowData',rowid,data.userdata);
								
								$.workflow.showButtons(opt.buttons);
								
 								if (opt.callback && $.isFunction(opt.callback)) {
 									opt.callback(data);
 								}						
							}else{
								$.jgrid.error_dialog2('提示',data.message);
							}
						},
 						error:function(xhr, textStatus, errorThrown){
 							$.jgrid.error_dialog2('提示',"系统错误，取消委派任务失败！");
 						}
					},opt.ajaxopts || {}));			
				} else {			
					$.jgrid.info_dialog2('注意',"请选择记录");	
				}					
			},
			
			/**
			 * 委派任务，将任务委派给别一用户办理
			 * 参数：opt
			 * {
			 * gridId:'', //jqgrid 表格ID			
			 * path:'', //业务Controller配置url
			 * buttons:[{name:'',show:false},{name:'',show:true},...] //操作图标ID
			 * ajaxopts:{},//jquery ajax请求参数
			 * dialogopts:{} //dialog参数
			 * callback:function(data){} //回调函数，成功时调用
			 * }
			 */
			delegate : function (opt){
				
				var grid = $("#"+opt.gridId);
				var rowid = grid.jqGrid('getGridParam','selrow');				
									
				if(rowid){					
					
					var ret = grid.jqGrid('getRowData',rowid);	
					var task = $.workflow.task.deserializeTasks(ret.taskinfo);
					
					$('<div/>', {
						title: '委派任务['+task.name+']',
						html: "选择用户：<select id='delegateUser'></select>"
					}).dialog($.extend({
						modal: true,
						open: function() {
							$.ajax({
								url:ctx + '/'+opt.path+'/user/search',
								type:'POST',
								dataType:'json',
								success : function (data) {
							        if (data.success) { 
							        	var vars = $('#delegateUser');
							        	var option = $("<option>").text('请选择').val('') ;
							        	vars.append(option);
							        	
							        	 $.each(data.content, function(i, item) {					        		
							        		 option = $("<option>").text(item.name).val(item.loginName) ; 
							        		 vars.append(option);
							            });
							        } 
								}
							});	
						},
						close : function(event, ui){
							 $( this ).dialog("destroy"); 
							 $( this ).remove();
							 $('#delegateUser').remove();
						},
						buttons: [{
							text: '提交',
							click: function() {
								var slef = this;
								var delegateUser = $.trim($('#delegateUser').val());
								if (!delegateUser) {								
									$.jgrid.info_dialog2('提示','请选择用户！',{zIndex:2000});							
								}else{
																
									$.ajax($.extend({
										url: ctx + '/'+opt.path+'/generic/task/delegate/'+rowid+'/'+ task.id+'/'+delegateUser,
										type: "POST",
										dataType: "json",
										success : function (data) {
									        if (data.success) {   
									        	$(slef).dialog('close');
									        	$.jgrid.info_dialog2('提示',data.message);												
									        	grid.jqGrid('setRowData',rowid,data.userdata);		
									        	
												$.workflow.showButtons(opt.buttons);
												
				 								if (opt.callback && $.isFunction(opt.callback)) {
				 									opt.callback(data);
				 								}									        	
									        }else{
												$.jgrid.error_dialog2('提示',data.message);
											}
										},
										error:function(xhr, textStatus, errorThrown){
											 $.jgrid.error_dialog2('提示','系统错误，委派任务失败',{zIndex:2000});
										}
									},opt.ajaxopts || {}));							
								
								}
							}
						}, {
							text: '取消',
							click: function() {
								$(this).dialog('close');						
							}
						}]
					} ),opt.dialogopts ||{} );						
				} else {			
					$.jgrid.info_dialog2('注意',"请选择记录");	
				}					
			},
			
			/**
			 * 办理任务
			 * 参数：opt
			 * {
			 * gridId:'', //jqgrid 表格ID			
			 * path:'', //业务Controller配置url
			 * buttons:[{name:'',show:false},{name:'',show:true},...] //操作图标ID
			 * ajaxopts:{},//jquery ajax请求参数
			 * callback:function(data){} //回调函数，成功时调用
			 * }
			 */
			complete : function (opt){
				
				var grid = $("#"+opt.gridId);
				var rowid = grid.jqGrid('getGridParam','selrow');
				var bussessKey = opt.bussessKey ? opt.bussessKey : rowid; 	
				
				if(rowid){	
					var ret = grid.jqGrid('getRowData',rowid);	
					var task = $.workflow.task.deserializeTasks(ret.taskinfo);
					
					$.ajax($.extend({
						url: ctx+'/'+opt.path+'/generic/task/complete/'+bussessKey+'/'+task.id,
						type: "POST",
						dataType: "json",
						contentType:"application/json",
						success : function (data) {
							if(data.success){
								$.jgrid.info_dialog2('提示',data.message,{zIndex:2000});
								
								grid.jqGrid('setRowData',rowid,data.userdata.business);
								grid.jqGrid('setRowData',rowid,data.userdata.process);
								
								$.workflow.showButtons(opt.buttons);
								
 								if (opt.callback && $.isFunction(opt.callback)) {
 									opt.callback(data);
 								}						
							}else{
								$.jgrid.error_dialog2('提示',data.message,{zIndex:2000});
							}
						},
 						error:function(xhr, textStatus, errorThrown){
 							$.jgrid.error_dialog2('提示',"系统错误，签收任务失败！",{zIndex:2000});
 						}
					},opt.ajaxopts || {}));			
				} else {			
					$.jgrid.info_dialog2('注意',"请选择记录");	
				}					
			}			
			
			
	 };
	 
	 $.workflow.task.realtime = {
		/**
		 * 实时确定用户是否有权处理当前任务
		 */
		decision:function(gridId,rowid,ajaxoptions){
			var ret = $("#"+gridId).jqGrid('getRowData',rowid);
			if(ret.processInstanceId && ret.tasks){						
				$.ajax($.extend({
					url:ctx+'/workflow/task/realtime/decision/'+ret.processInstanceId,								
					type: "post",
					dataType: "json"
				},ajaxoptions || {}));
			}
		}				
	 };
	 
	 $.workflow.procdef = {
			/**
			 * 确定用户是否有权启动流程
			 */
			decision:function(processDefinitionKey,ajaxoptions){
					$.ajax($.extend({
						url:ctx+'/workflow/procdef/decision/'+processDefinitionKey,								
						type: "post",
						dataType: "json"
					},ajaxoptions || {}));					
			},
			
			/**
			 * 启动流程
			 * 参数：opt
			 * {
			 * gridId:'', //jqgrid 表格ID
			 * processDefinitionKey:'',//流程定义唯一标识
			 * path:'', //业务Controller配置url
			 * buttons:[{name:'',show:true},...] //操作图标ID
			 * ajaxopts:{},//jquery ajax请求参数
			 * callback:function(data){} //回调函数，成功时调用
			 * }
			 */
			start:function(opt){
				var rowid = jQuery("#"+opt.gridId).jqGrid('getGridParam','selrow');			
				if(rowid){						
					$.ajax($.extend({
						url: ctx+'/'+opt.path+'/generic/start/'+rowid+'/'+opt.processDefinitionKey,
						type: "POST",
						dataType: "json",	
						contentType:"application/json",							
						success : function (data) {
							if(data.success){
								$.jgrid.info_dialog2('提示',data.message);	
																
								var grid = $('#'+opt.gridId)
 								grid.jqGrid('setRowData',data.userdata.business.id,data.userdata.business);
 								grid.jqGrid('setRowData',data.userdata.process.id,data.userdata.process);								
								
								$.workflow.showButtons(opt.buttons);
								
 								if (opt.callback && $.isFunction(opt.callback)) {
 									opt.callback(data);
 								}
							}else{
								$.jgrid.error_dialog2('提示',data.message);
							}
						},
 						error:function(xhr, textStatus, errorThrown){
 							$.jgrid.error_dialog2('提示',"系统错误，启动流程失败！");
 						}
					},opt.ajaxopts || {}));			
				} else {			
					$.jgrid.info_dialog2('注意',"请选择记录");	
				}							
			}
	 };


	 $.workflow.historic = {
		
		 /**
		  * 查看流程历史
		  */
		 view : function(gridId,options){
				
			var gsr = jQuery('#'+gridId).jqGrid('getGridParam','selrow');
			if(gsr){
				var ret = $('#'+gridId).jqGrid('getRowData',gsr);
				var task = $.workflow.task.deserializeTasks(ret.taskinfo);
				
				var vw = document.documentElement.clientWidth;
				var vh = document.documentElement.clientHeight;
				var vt = (new Date()).getTime();
				var executionId;
				if(ret.tasks){
					executionId = task ? task.executionId : null;
					if(!executionId){
						executionId = $.workflow.task.getExecutionId(ret.tasks);
					}
				}
								
				$.jgrid.form_dialog(gridId+"diagram",
				{	title:'流程历史',
					width:vw*0.7,
					height:vh*0.5,	
					closeOnEscape : true 						 						
				},
				{
					url: ctx+'/workflow/historic',
					data:$.extend({processDefinitionKey:ret.processDefinitionKey,processDefinitionId:ret.processDefinitionId,processInstanceId:ret.processInstanceId,executionId:executionId,time:vt},options||{})
					
				});			
			} else {			
				$.jgrid.info_dialog2('注意',"请选择记录");	
			}		
		}
	 };
	 
	 /**
	  * 动态表单
	  */
	 $.workflow.dynamic = {
			 
		 /**
		  * 打开启动流程表单
		
		 * 参数：opt
		 * {
		 * gridId:'', //jqgrid 表格ID	
		 * processDefinitionKey:'', 流利定义唯一标识		
		 * path:'', //业务Controller配置url
		 * buttons:[{name:'',show:false},{name:'',show:true},...] //操作图标ID
		 * ajaxopts:{},//jquery ajax请求参数
		 * dialogopts:{} //dialog参数
		 * callback:function(data){} //提交回调函数，成功时调用
		 * afterShowForm:function(data) //表单打开后回调函数
		 * btnHide:false是否隐藏按钮
		 *  }
		 */
		 showStartupProcessDialog : function (opt) {
		 	
			var grid = $('#'+opt.gridId);
			var rowid = grid.jqGrid('getGridParam','selrow');			
			if(rowid){	
					
				 //var vw = document.documentElement.clientWidth * 0.6;
				 //var vh = document.documentElement.clientHeight * 0.5;
				 
				 var submitAction = ctx + '/'+opt.path+'/generic/submit/start/form/'+rowid+'/' + opt.processDefinitionKey;
				 var getAction = ctx +'/'+opt.path+'/generic/dynamic/get/start/form/' + opt.processDefinitionKey;
				 
			 	$('<div/>', {
			 		'class': 'dynamic-form-dialog ui-jqdialog-content',
			 		title: '启动流程',
			 		html: '<span class="ui-loading">正在读取表单……</span>'
			 	}).dialog($.extend({
			 		modal: true,
			 		//width: vw,
			 		//height: vh,
			 		open: function() {	
			 			if(opt.btnHide){
			 				$.workflow.hideSubmitButtons();
			 			}	
			 			$.workflow.dynamic.readFormFields.call(this,submitAction,getAction,opt.afterShowForm);
			 		},
					close : function(event, ui){
						 $( this ).dialog("destroy");
						 $( this ).remove();
					},			 		
			 		buttons: [{
			 			id:'submitbtn',
			 			text: '启动',
			 			click: function(){
			 				var self = this;
			 				if($(".dynamic-form").valid()) {
			 					
			 					$('.dynamic-form').ajaxSubmit($.extend( {
			 						type : 'post',
			 						dataType : 'json',
			 						beforeSubmit:function(forms,jq,options){			 							
			 							$('#submitbtn').attr('disabled',true); 
			 							$.workflow.hideSubmitButtons(); 
			 						},
			 						success : function(data, statusText, xhr, $form) {
			 							if(data.success){
			 								$.jgrid.info_dialog2('提示',data.message,{zIndex:2000});			 								
			 								
			 								grid.jqGrid('setRowData',data.userdata.business.id,data.userdata.business);
			 								grid.jqGrid('setRowData',data.userdata.process.id,data.userdata.process);				 								
			 								
			 								$.workflow.showButtons(opt.buttons);
											
			 								if (opt.callback && $.isFunction(opt.callback)) {
			 									opt.callback(data);
			 								}
			 								
			 								$(self).dialog('close');
			 							}else{
			 								$.workflow.showSubmitButtons();
			 								$('#submitbtn').removeAttr("disabled");
			 								$.jgrid.error_dialog2('提示',data.message,{zIndex:2000});
			 							}
			 							 
			 						},
			 						error:function(xhr, textStatus, errorThrown){
			 							$.workflow.showSubmitButtons();
			 							$('#submitbtn').removeAttr("disabled");
			 							$.jgrid.error_dialog2('提示',"系统错误，启动流程失败！",{zIndex:2000});
			 						}
			 					} ,opt.ajaxopts||{}  ));
			 				}				
			 			}
			 		}, {
			 			id:'cancelbtn',
						text: '取消',
						click: function() {
							$(this).dialog('close');						
						}
					}]
			 	}) ,opt.dialogopts||{}  );
		 	
			} else {			
				$.jgrid.info_dialog2('注意',"请选择记录");	
			}
		 },
		 
		 
		 /**
		  * 打开任务表单
		
		 * 参数：opt
		 * {
		 * gridId:'', //jqgrid 表格ID
		 * path:'', //业务Controller配置url
		 * buttons:[{name:'',show:false},{name:'',show:true},...] //操作图标ID
		 * ajaxopts:{},//jquery ajax请求参数
		 * dialogopts:{} //dialog参数
		 * callback:function(data){} //提交回调函数，成功时调用
		 * afterShowForm:function(data) //表单打开后回调函数
		 * btnHide:false
		 *	}		  
		 */
		 showTaskDialog : function (opt) {
		 	
			 var grid = $('#'+opt.gridId);
			var rowid = grid.jqGrid('getGridParam','selrow');			
			if(rowid){	
					
				var ret = grid.jqGrid('getRowData',rowid);
				var task = $.workflow.task.deserializeTasks(ret.taskinfo);
				
				 //var vw = document.documentElement.clientWidth * 0.6;
				 //var vh = document.documentElement.clientHeight * 0.5;
				 
				 var submitAction = ctx + '/'+opt.path+'/generic/submit/task/form/'+rowid+'/' + task.id;
				 var getAction = ctx +'/'+opt.path+'/generic/dynamic/get/task/form/' + task.id;
				 
			 	$('<div/>', {
			 		'class': 'dynamic-form-dialog ui-jqdialog-content',
			 		title: '办理任务[' + task.name + ']',
			 		html: '<span class="ui-loading">正在读取表单……</span>'
			 	}).dialog($.extend({
			 		modal: true,
			 		//width: vw,
			 		//height: vh,
			 		open: function() {	
			 			if(opt.btnHide){
			 				$.workflow.hideSubmitButtons();
			 			}			 			
			 			$.workflow.dynamic.readFormFields.call(this,submitAction,getAction,opt.afterShowForm);
			 		},
					close : function(event, ui){
						 $( this ).dialog("destroy");
						 $( this ).remove();
					},			 		
			 		buttons: [{
			 			id:'submitbtn',
			 			text: '提交',
			 			click: function(){
			 				var self = this;
			 				if($(".dynamic-form").valid()) {
			 					
			 					$('.dynamic-form').ajaxSubmit($.extend( {
			 						type : 'post',
			 						dataType : 'json',
			 						beforeSubmit:function(forms,jq,options){
			 							$('#submitbtn').attr('disabled',true); 
			 							$.workflow.hideSubmitButtons(); 
			 						},
			 						success : function(data, statusText, xhr, $form) {
			 							if(data.success){
			 								$.jgrid.info_dialog2('提示',data.message,{zIndex:2000});			 								
			 									
			 								grid.jqGrid('setRowData',data.userdata.business.id,data.userdata.business);
			 								grid.jqGrid('setRowData',data.userdata.process.id,data.userdata.process);			 								
			 								
			 								$.workflow.showButtons(opt.buttons);
											
			 								if (opt.callback && $.isFunction(opt.callback)) {
			 									opt.callback(data);
			 								}
			 								
			 								$(self).dialog('close');
			 							}else{
			 								$.workflow.showSubmitButtons(); 
			 								$('#submitbtn').removeAttr("disabled");
			 								$.jgrid.error_dialog2('提示',data.message,{zIndex:2000});
			 							}
			 						},
			 						error:function(xhr, textStatus, errorThrown){
			 							$.workflow.showSubmitButtons();
			 							$('#submitbtn').removeAttr("disabled");
			 							$.jgrid.error_dialog2('提示',"系统错误，任务办理失败！",{zIndex:2000});
			 						}
			 					} ,opt.ajaxopts||{}  ));
			 				}				
			 			}
			 		}, {
			 			id:'cancelbtn',
						text: '取消',
						click: function() {
							$(this).dialog('close');						
						}
					}]
			 	}) ,opt.dialogopts||{}  );
		 	
			} else {			
				$.jgrid.info_dialog2('注意',"请选择记录");	
			}
		 },		 

		 /**
		  * 读取表单字段
		  */

		 readFormFields : function (submitAction,getAction,afterShowForm) {		 	

		 	// 清空对话框内容
		 	$('.dynamic-form-dialog').html("<span><form class='dynamic-form FormGrid' method='post' style='width: 100%; overflow: auto; position: relative; height: auto;'><table class='dynamic-form-table EditTable' cellspacing='0' cellpadding='0' border='0'></table></form></span>");
		 	var $form = $('.dynamic-form');

		 	// 设置表单提交id
		 	$form.attr('action', submitAction);

		 	// 读取启动时的表单
		 	$.getJSON(getAction, function(data) {
		 		var trs = "";
		 		data = data.userdata;
		 		
		 		$.each(data.formData.formProperties, function() {
		 			
		 			var id = "json_" +this.id;
		 			if(data[id]){				
		 				trs += "<input type='hidden' id='" +id+ "' name='" +id+ "' value='" +data[id]+ "'>";
		 			}
		 					
		 		});
		 		
		 		$.each(data.formData.formProperties, function() {
		 			var className = this.required === true ? "required" : "";
		 			trs += "<tr class='FormData'>" + $.workflow.dynamic.createFieldHtml(data, this, className)
		 			if(this.required === true) {
		 				trs += "<span style='color:red'>*</span>";
		 			}
		 			trs += "</td></tr>";
		 		});

		 		// 添加table内容
		 		if($.support.leadingWhitespace){
		 			//not ie
			 		$('.dynamic-form-table').html(trs).find('tr').hover(function() {
			 			$(this).addClass('ui-state-hover');
			 		}, function() {
			 			$(this).removeClass('ui-state-hover');
			 		});		 		
		 		}else{	
		 			//is ie
			 		var div = document.createElement("div");
			 		div.innerHTML="<table><tbody>"+trs+"</tbody></table>";
			 		$('.dynamic-form-table')[0].appendChild(div.firstChild.firstChild);
			 		
			 		$('.dynamic-form-table').find('tr').hover(function() {
			 			$(this).addClass('ui-state-hover');
			 		}, function() {
			 			$(this).removeClass('ui-state-hover');
			 		});
		 		} 

		 		// 初始化日期组件
		 		$form.find('.dateISO').datepicker();
		 		
		 		// 初始化日期组件
		 		$form.find('.datetimeISO').datetimepicker();

		 		// 表单验证
		 		//$form.validate($.extend({}, $.common.plugin.validator));
		 		
		 		if (afterShowForm && $.isFunction(afterShowForm)) {
		 			afterShowForm(data);
				}
		 	});
		 },


		 /**
		  * 生成一个field的html代码
		  */

		 createFieldHtml : function (formData, prop, className) {
		 	
		 	return formFieldCreator[prop.type.name](formData, prop, className);
		 }							
	};
	 
	 /**
	  * 外置表单
	  */
	 $.workflow.formkey = {
			 
		 /**
		  * 打开启动流程
		 * 参数：opt
		 * {
		 * gridId:'', //jqgrid 表格ID	
		 * processDefinitionKey:'', 流利定义唯一标识		
		 * path:'', //业务Controller配置url
		 * buttons:[{name:'',show:false},{name:'',show:true},...] //操作图标ID
		 * ajaxopts:{},//jquery ajax请求参数
		 * dialogopts:{} //dialog参数
		 * callback:function(data){} //回调函数，成功时调用
		 * afterShowForm:function(data) //表单打开后回调函数
		 * }
		 */
		 showStartupProcessDialog : function(opt) {
		 	
		 var grid = $('#'+opt.gridId);
		 var rowid = grid.jqGrid('getGridParam','selrow');			
		 if(rowid){	
				
			 var submitAction = ctx + '/'+opt.path+'/generic/submit/start/form/'+rowid+'/' + opt.processDefinitionKey;
			 var getAction = ctx +'/'+opt.path+'/generic/formkey/get/start/form/' + opt.processDefinitionKey;
						
			 	$('<div/>', {
			 		'class': 'formkey-form-dialog  ui-jqdialog-content',
			 		title: '启动流程',
			 		html: '<span class="ui-loading">正在读取表单……</span>'
			 	}).dialog($.extend( {
			 		modal: true,
			 		//width: document.documentElement.clientWidth * 0.4,
			 		//height: document.documentElement.clientHeight * 0.5,
			 		open: function() {	
			 			if(opt.btnHide){
			 				$.workflow.hideSubmitButtons();
			 			}	
			 			$.workflow.formkey.readForm.call(this, submitAction,getAction,opt.afterShowForm);
			 		},
					close : function(event, ui){
						 $( this ).dialog("destroy");
						 $( this ).remove();
					},	
			 		buttons: [{
			 			id:'submitbtn',
			 			text: '启动',
			 			click: function(){
			 				var self = this;
			 				if($(".formkey-form").valid()) {
			 					
			 					var data = "";	
			 					
			 					$(".formkey-form").find("select").each(function(){
			 						var id = "json_"+this.id;					
			 						var vs = "";
			 						$(this).find("option").each(function(){							
			 							vs +=',"'+this.value+'":"'+this.text+'"';
			 						});
			 						
			 						if(vs != ""){
			 							vs = "{"+ vs.substring(1)+"}";							
			 							data += "&"+id+"="+	encodeURIComponent(vs);								
			 						}						
			 					});	
			 					if(data != ""){
			 						data = data.substring(1);
			 					}
			 					
			 					$('.formkey-form').ajaxSubmit( $.extend({
			 						url:$('.formkey-form').attr('action')+'?'+data,
			 						type : 'post',
			 						dataType : 'json',	
			 						beforeSubmit:function(forms,jq,options){
			 							$('#submitbtn').attr('disabled',true); 
			 							$.workflow.hideSubmitButtons();
			 						},
			 						success : function(data, statusText, xhr, $form) {
			 							if(data.success){
			 								$.jgrid.info_dialog2('提示',data.message,{zIndex:2000});
			 								
			 								//grid.jqGrid('setRowData',rowid,data.userdata);	
			 								grid.jqGrid('setRowData',data.userdata.business.id,data.userdata.business);
			 								grid.jqGrid('setRowData',data.userdata.process.id,data.userdata.process);
			 								
			 								$.workflow.showButtons(opt.buttons);
											
			 								if (opt.callback && $.isFunction(opt.callback)) {
			 									opt.callback(data);
			 								}
			 								
			 								$(self).dialog('close');
			 							}else{
			 								$.workflow.showSubmitButtons(); 
			 								$('#submitbtn').removeAttr("disabled");
			 								$.jgrid.error_dialog2('提示',"启动流程失败！",{zIndex:2000});
			 							}
			 						},
			 						error:function(xhr, textStatus, errorThrown){
			 							$.workflow.showSubmitButtons(); 
			 							$('#submitbtn').removeAttr("disabled");
			 							$.jgrid.error_dialog2('提示',"系统错误，启动流程失败！",{zIndex:2000});
			 						}
			 					}  ,opt.ajaxopts || {}));
			 				}				
			 			}
			 		}, {
			 			id:'cancelbtn',
						text: '取消',
						click: function() {
							$(this).dialog('close');						
						}
					}]
			 	} ,opt.dialogopts||{}   ) );
			 	
			} else {			
				$.jgrid.info_dialog2('注意',"请选择记录");	
			}			 	
		},
		
		
		 /**
		  * 打开任务
		 * 参数：opt
		 * {
		 * gridId:'', //jqgrid 表格ID			
		 * path:'', //业务Controller配置url
		 * buttons:[{name:'',show:false},{name:'',show:true},...] //操作图标ID
		 * ajaxopts:{},//jquery ajax请求参数
		 * dialogopts:{} //dialog参数
		 * callback:function(){data} //回调函数，成功时调用
		 * afterShowForm:function(data) //表单打开后回调函数
		 * 	}
		 */
		 showTaskDialog : function(opt) {
		 	
		 var grid = $('#'+opt.gridId);
		 var rowid = grid.jqGrid('getGridParam','selrow');			
		 if(rowid){	
			 
			var ret = grid.jqGrid('getRowData',rowid);
			var task = $.workflow.task.deserializeTasks(ret.taskinfo);
			 
			 var submitAction = ctx + '/'+opt.path+'/generic/submit/task/form/'+rowid+'/' + task.id;
			 var getAction = ctx +'/'+opt.path+'/generic/formkey/get/task/form/' + task.id;
						
			 	$('<div/>', {
			 		'class': 'formkey-form-dialog  ui-jqdialog-content',
			 		title: '办理任务[' + task.name + ']',
			 		html: '<span class="ui-loading">正在读取表单……</span>'
			 	}).dialog($.extend( {
			 		modal: true,
			 		//width: document.documentElement.clientWidth * 0.4,
			 		//height: document.documentElement.clientHeight * 0.5,
			 		open: function() {	
			 			if(opt.btnHide){
			 				$.workflow.hideSubmitButtons();
			 			}	
			 			$.workflow.formkey.readForm.call(this, submitAction,getAction,opt.afterShowForm);
			 		},
					close : function(event, ui){
						 $( this ).dialog("destroy");
						 $( this ).remove();
					},	
			 		buttons: [{
			 			id:'submitbtn',
			 			text: '提交',
			 			click: function(){
			 				var self = this;
			 				if($(".formkey-form").valid()) {
			 					
			 					var data = "";	
			 					
			 					$(".formkey-form").find("select").each(function(){
			 						var id = "json_"+this.id;					
			 						var vs = "";
			 						$(this).find("option").each(function(){							
			 							vs +=',"'+this.value+'":"'+this.text+'"';
			 						});
			 						
			 						if(vs != ""){
			 							vs = "{"+ vs.substring(1)+"}";							
			 							data += "&"+id+"="+	encodeURIComponent(vs);								
			 						}						
			 					});	
			 					if(data != ""){
			 						data = data.substring(1);
			 					}
			 					
			 					$('.formkey-form').ajaxSubmit( $.extend({
			 						url:$('.formkey-form').attr('action')+'?'+data,
			 						type : 'post',
			 						dataType : 'json',
			 						beforeSubmit:function(forms,jq,options){
			 							$('#submitbtn').attr('disabled',true); 
			 							$.workflow.hideSubmitButtons(); 
			 						},
			 						success : function(data, statusText, xhr, $form) {
			 							if(data.success){
			 								$.jgrid.info_dialog2('提示',data.message,{zIndex:2000});
			 								
			 								grid.jqGrid('setRowData',data.userdata.business.id,data.userdata.business);
			 								grid.jqGrid('setRowData',data.userdata.process.id,data.userdata.process);			 								
			 								
			 								$.workflow.showButtons(opt.buttons);
											
			 								if (opt.callback && $.isFunction(opt.callback)) {
			 									opt.callback(data);
			 								}
			 								
			 								$(self).dialog('close');
			 							}else{
			 								$.workflow.showSubmitButtons();
			 								$('#submitbtn').removeAttr("disabled");
			 								$.jgrid.error_dialog2('提示',"任务办理失败！",{zIndex:2000});
			 							}
			 						},
			 						error:function(xhr, textStatus, errorThrown){			 							
			 							$.workflow.showSubmitButtons();
			 							$('#submitbtn').removeAttr("disabled");
			 							$.jgrid.error_dialog2('提示',"系统错误，任务办理失败！",{zIndex:2000});
			 						}
			 					}  ,opt.ajaxopts || {}));
			 				}				
			 			}
			 		}, {
			 			id:'cancelbtn',
						text: '取消',
						click: function() {
							$(this).dialog('close');						
						}
					}]
			 	} ,opt.dialogopts||{}   ) );
			 	
			} else {			
				$.jgrid.info_dialog2('注意',"请选择记录");	
			}			 	
		},		

		 /**
		  * 读取流程启动表单
		  */
		  readForm : function (submitAction,getAction,afterShowForm) {
			 	var dialog = this;
	
			 	// 读取启动时的表单
			 	$.ajax({
			 		url:getAction,
			 		type:'post',
			 		dataType:'html',
			 		success:function(form){
			 			
			 			// 获取的form是字符行，html格式直接显示在对话框内就可以了，然后用form包裹起来
			 			$(dialog).html(form).wrap("<span><form class='formkey-form FormGrid' method='post' style='width: 100%; overflow: auto; position: relative; height: auto;'/>");
	
			 			var $form = $('.formkey-form');
	
			 			// 设置表单action
			 			$form.attr('action', submitAction);
	
			 			// 初始化日期组件
			 			$form.find('.datetime').datetimepicker({
			 		            stepMinute: 5
			 		        });
			 			$form.find('.date').datepicker();
			 			
				 		// 初始化日期组件
				 		$form.find('.dateISO').datepicker();
				 		
				 		// 初始化日期组件
				 		$form.find('.datetimeISO').datetimepicker();
				 		
				 		if (afterShowForm && $.isFunction(afterShowForm)) {
				 			afterShowForm(form);
						}
			 		}
			 	});		 	
		 }			 
	 };

})(jQuery);
