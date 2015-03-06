/**
 * Ajax请求全局事件设置
 * 401 (Unauthorized)
 * 浏览器试图在没有提供恰当的授权报头的情况下,访
 * 问受密码保护的页面。
 * 当jqXHR.status=＝401时，登录超时
 * 弹出登录窗口，重新登录
 */
$(document).ajaxError(function(event, jqXHR, ajaxSettings, thrownError){
	if( parseInt(jqXHR.status,10) == 401){
		//$.jgrid.info_dialog('登录','登录超时，请重新登录！');		
		//$("#ajaxlogin_dialog").dialog('open');
		$.jgrid.form_dialog("ajaxlogin_dialog",
		{	title:'登录',
			width:400,
			height:300 						 						
		},
		{
			url: ctx+'/ajaxlogin'
		});			
	}
});


function form_dialog(gid,p,ajaxo){
	$.jgrid.form_dialog(gid,p,ajaxo);	
}

function grid_dialog(gid,p,ajaxo){
	$.jgrid.grid_dialog(gid,p,ajaxo);	
}

function iframeInnerGridReload(tabsid,gid,data) {	

	var refresh_tab = $('.ui-tabs-panel:not(.ui-tabs-hide)','#'+tabsid);	
	if (refresh_tab && refresh_tab.find('iframe').length > 0) {
		var _refresh_ifram = refresh_tab.find('iframe')[0];
		if (_refresh_ifram.contentWindow.reloadGrid) {			
			_refresh_ifram.contentWindow.reloadGrid(gid,data);
		}
	}
}

/**
 * 设置grid高度
 * @param gridid
 */
function setGridHeight(gridid,p){	
	 var gview = "#gview_"+gridid;	
	 var gbox = "#gbox_"+gridid;	
	 var vw =  $('.jqgtabs',window.parent.document).innerWidth();	 
	 $('.ui-jqgrid-titlebar',gview).width(vw-10);
	 $('.ui-jqgrid-pager',gbox).width(vw-3);	 
	
	$("#"+gridid).jqGrid('gridHeight2',p);  
}

function getPasswordStrength(pwd){
  if (pwd.length < 6)
  { 
	  return 0;
  }else{
    var ls = 0;
    if (pwd.match(/[a-z]/ig)) ls++;
    if (pwd.match(/[0-9]/ig)) ls++;
    if (pwd.match(/(.[^a-z0-9])/ig)) ls++;
    if (pwd.length < 6 && ls > 0) ls--;
    return ls;
  }
}

(function($){
	

$.extend($.jgrid.defaults,
	{
		mtype:'POST',	
		rowNum:50,
	   	rowList:[10,20,30,40,50,60,70,80,90,100],		
		ajaxSelectOptions:{},
		jsonReader: {
			root:"content",
			page: "number",
			total: "totalPages",
			records: "totalElements",		
			repeatitems : false,
			subgrid:{
				root:"content",
				 //page: "number",
			     // total: "totalPages",
			     // records: "totalElements",		
				repeatitems : false			
			}    					
		},
	    xmlReader: { 
	    	root : "Page", 
	    	row: "content", 
	    	page: "number",
		    total: "totalPages",
		    records: "totalElements",	    	
	    	repeatitems: false,
	    	subgrid:{
		    	root : "Response", 
		    	row: "content", 
		    	// page: "number",
			    //  total: "totalPages",
			    //  records: "totalElements",	    	
		    	repeatitems: false
			}    			    	
	    } ,
	    loadError:function(xhr,status,error){	    	
	    	$.jgrid.error_dialog2('注意','<strong>错误：</strong> Error status: ' + xhr.statusText + ', Error code: ' + xhr.status,{zIndex:2000});
	    }
	}
);	

 $.extend($.jgrid.nav,
		{searchtext:'查找',
	refreshtext:'刷新',
	edittext: "编辑",
	addtext:"添加",
	deltext: "删除",
	savetext: "保存",
	canceltext: "取消",
	viewtext: "查看"});
 

$.extend($.fn.fmatter, {
   radio: function(cellData, options){
     if (cellData == null) return "";
     return "<input type='radio' rowId='" + options.rowId + "'>" + $.jgrid.htmlEncode(cellData);
   },
   
   mydate :function(cellValue, options) {
       if(cellValue) {
           return $.fmatter.util.DateFormat(
               '', 
               new Date(cellValue), 
               'Y-m-d', 
               $.extend({}, $.jgrid.formatter.date, options)
           );
       } else {
           return '';
       }
   },
   mydatetime :function(cellValue, options) {
       if(cellValue) {
           return $.fmatter.util.DateFormat(
               '', 
               new Date(cellValue), 
               'Y-m-d H:i:s', 
               $.extend({}, $.jgrid.formatter.date, options)
           );
       } else {
           return '';
       }
   } ,
   
   myshowlink : function(cellval, opts,rowObject) {
	  
		var op = {baseLinkUrl: opts.baseLinkUrl,
				showAction:opts.showAction, 
				pathVariables: opts.pathVariables || [],			
				addParams: opts.addParams || [], 
				addParamMap: opts.addParamMap || [],
				target: opts.target, 
				idName: opts.idName},		
		target = "", idUrl;
		
		if(opts.colModel !== undefined && !$.fmatter.isUndefined(opts.colModel.formatoptions)) {
			op = $.extend({},op,opts.colModel.formatoptions);
		}
		
		if(op.target) {target = 'target=' + op.target;}
		
		idUrl = op.baseLinkUrl;
		
		if(op.pathVariables){
			$.each(op.pathVariables,function(i,item){
				idUrl += '/'+rowObject[item];
			});
		}
		
		if(idUrl.indexOf('?') == -1){
			idUrl += '?'+ op.idName+'='+opts.rowId;
		}else{
			idUrl += '&'+ op.idName+'='+opts.rowId;
		}
		
		if(op.addParams){
			$.each(op.addParams,function(i,item){
				idUrl += '&'+item+'='+rowObject[item];
			});
		}
		
		if(op.addParamMap){
			$.each(op.addParamMap,function(i,item){
				idUrl += '&'+item.name+'='+rowObject[item.value];
			});
		}
		
		idUrl += op.showAction;		
		
		if($.fmatter.isString(cellval) || $.fmatter.isNumber(cellval)) {	//add this one even if its blank string
			return "<a "+target+" href=\"" + idUrl + "\">" + cellval + "</a>";
		}
		return $.fn.fmatter.defaultFormat(cellval,opts);
	}
   
 });

 var oldfilterToolbar = $.fn.jqGrid.filterToolbar;
 //var oldeditGridRow = $.fn.jqGrid.editGridRow;
 $.jgrid.extend({	
	 
	 
	 /*
	  * 合并单元格
	  * 使用方法,在gridComplete事件中使用
	  * 
	  * gridComplete: function() { 
	  * 	$("#grid").jqGrid('mergeCells',{name:cellName,colspan: 2,colnames:['subname']});
	  * }
	  * 
	  * cellName：合并行的列名称 	
	  */	
	mergeCells : function (o) {	
		
		o =  $.extend({},o  || {});
		
		return this.each(function(){
			var $t = this;			
			if (!$t.grid) {return;}
			
			//得到显示到界面的id集合
			var mya = $($t).jqGrid("getDataIDs");            
            //var mya = $($t).getDataIDs();
            //当前显示多少条
            var length = mya.length;
            
            var ids = new Array();           
            var k = 0;            
            while (k < length) {
            	
            	
                //从上到下获取一条信息
                var before = $($t).jqGrid('getRowData', mya[k]);
                
               	ids = [];               
                ids.push( mya[k]);              
                
                //定义合并行数
                var rowSpanTaxCount = 1;
                for (var j = k + 1; j <= length; j++) {
                	
                    //和上边的信息对比 如果值一样就合并行数+1 然后设置rowspan 让当前单元格隐藏
                    var end = $($t).jqGrid('getRowData', mya[j]);                    
                  
                    if (before[o.name] == end[o.name]) { 
                    	k++;                    
                        rowSpanTaxCount++;
                        ids.push( mya[j]);                        
                    } else { 
                    	k++;                    	
                        break;
                    }
                }                
                
                if(ids.length == 1 && o.colspan && o.colnames){
                	$("#" +o.name + ids[0] ).attr("colspan", o.colspan);
                	$.each(o.colnames,function(i,item){
                		$("#" +item + ids[0]).remove();
                	});                	
                }else if(ids.length > 1){                	
                	$("#" +o.name + ids[0] ).attr("rowspan", rowSpanTaxCount);
                	for(var i = 1; i < ids.length;i++){
                		$("#" +o.name + ids[i]).remove();
                	}
                }
            }
		});
	},
	
	
	mergeHeaders : function ( o ) {
		o = $.extend({},o  || {});
		return this.each(function(){
			
			
			var ts = this,
			i, j=0, th, $th, 
			ths = ts.grid.headers,
			colModel = ts.p.colModel,
			cml = colModel.length;
			
			if(o.index ){
				firstIdx = o.index[0];	
				
				for (i = 0; i < cml; i++) {
					th = ths[i].el;
					$th = $(th);
					
					if(i == firstIdx){						
						$th.attr("colspan", o.index.length);
						$th.width($th.width()*o.index.length+(o.index.length-1)*5);
						j++;
					}else if(i==o.index[j] && i != firstIdx){
						$th.remove();
						j++;
					}
					
					if(j == o.index.length){
						break;
					}
				}
			}
			
		});				
	},	
	
	
	 
	qlsearch : function () {			
		
		return this.each(function(){
			var $t = this;			
			if (!$t.grid) {return;}
			
			var gcolMod = $($t).jqGrid("getGridParam",'colModel');
			if(gcolMod) {
				var sorttypes = "";
				var searchfields = "";
				$.each(gcolMod, function (i,n) {
					this.search = this.search == false ? false : true;
					if(this.search){
						this.index = this.index ? this.index : this.name;
						this.sorttype = this.sorttype ? this.sorttype : 'string';
						sorttypes += ','+'"'+this.index+'":"'+this.sorttype+'"';
						searchfields += ','+this.index;
					}
				});
				if(sorttypes != ""){
					sorttypes = sorttypes.substring(1);
					sorttypes = "{"+sorttypes+"}";
					
					searchfields = searchfields.substring(1);
					
					$($t).jqGrid('setGridParam',{postData:{sorttypes:sorttypes,searchfields:searchfields}});
				}
			}
		});
	},
	
	/**
	 * 复写$.jqgrid.filterToolbar方法，将defaultSearch默认值bw改为cn
	 * @param p
	 * @returns
	 */
	filterToolbar :function(p){
		p =  $.extend({
			defaultSearch : "cn"
		},p  || {});		
		
		return oldfilterToolbar.call (this, p);
	},	


	/**
	 * 设置grid高度
	 * @param p :{height:200,rate:0.5}
	 * @returns
	 */
	gridHeight : function (p) {
				
		p =  $.extend({
			height : 0
		},p  || {});
		
		return this.each(function(){
			var $t = this;			
			if (!$t.grid) {return;}			
						
			var vh = $.jgrid.getGridHeight($.jgrid.jqID($t.p.id),p);			
			$($t).jqGrid('setGridHeight',vh);			
		});
	},	
	
	gridHeight2 : function (p) {
		
		p =  $.extend({
			height : 0
		},p  || {});
		
		return this.each(function(){
			var $t = this;			
			if (!$t.grid) {return;}			
						
			var vh = $.jgrid.getGridHeight2($.jgrid.jqID($t.p.id),p);
			$($t).jqGrid('setGridHeight',vh);			
		});
	}	
	
 });

 $.extend($.jgrid,{
	 
	 
	 /**
	  * 给dataurl增加参数:edit_id=rowid
	  * @param p
	  */
	 setDataUrl :function(p){
	 
		if(p && p.gid && p.colnames){			
			var gid = p.gid.indexOf('#') == -1 ? '#'+p.gid : p.gid;						
			var gcolMod = $(gid).jqGrid("getGridParam",'colModel');
			
			var id ;
			if(p.rowid){
				var ret = $(gid).jqGrid('getRowData',p.rowid);
				id = ret.parent;
			}else{
				id = $(gid).jqGrid("getGridParam",'selrow');
			}
			
			if(gcolMod) {		
	   			
				$.each(gcolMod, function (i,n) {
					var $self = this;
					if($self.editoptions && $self.editoptions.dataUrl){
						$.each(p.colnames,function(j,k){						
							if($self.name == k){
								var date = new Date();
								var dataUrl = $self.editoptions.dataUrl;	
								var param =  'edit_id='+(p.rowid || '')+'&_parentid='+id+'&_time='+date.getTime();
								
								var idx = dataUrl.indexOf('edit_id=') ;
								if(idx != -1){
									dataUrl = dataUrl.substr(0,idx-1);								
								}						
								dataUrl = dataUrl.indexOf('?') == -1 ? dataUrl +'?'+param : dataUrl +'&'+param;	
								
								$(gid).jqGrid('setColProp',k,$.extend($self.editoptions,{"dataUrl":dataUrl}));						
							}						
						});
					}
				});				
			}			
		}
	},	
	
	/**
	 * 修改editrules
	 * @param p
	 */
	xeditrules :function(p){		
		if(p && p.gid && p.colnames){
			var gid = p.gid.indexOf('#') == -1 ? '#'+p.gid : p.gid;						
			var gcolMod = $(gid).jqGrid("getGridParam",'colModel');
			if(gcolMod) {				
				$.each(gcolMod, function (i,n) {
					var $self = this;						
					$.each(p.colnames,function(j,k){						
						if($self.name == k){													
							$(gid).jqGrid('setColProp',k,$.extend($self.editrules||{},p.editrules||{}));						
						}						
					});						
				});				
			}			
		}
	},		
		
	 checkexists :function(p,ajaxso){
		 	if(!ajaxso.url){
		 		return [false,"没有提供url",""];
		 	}
		 	
			var id = $('#id_g','#FrmGrid_'+p.gid,'#editmod'+p.gid).val();
   			id = (id == "_empty") ? "":id;   
   			
   			var result = $.ajax($.extend({                  
                   dataType:"json",
                   data:{"name":p.value,"id":id},
                   async:false,
                   cache:false,
                   type:"post"
               },ajaxso||{})).responseText; 
   			var json = $.parseJSON(result);
   			if(json.success){
   				return [true,"",""];
   			}else{
   				return [false,p.name+"己存在",""];
   			}		 
	 },
	 
	 checkPassword :function(p){
		var $password = $.trim($('#'+p.password).val());
		 
	 	if(!$.trim(p.value) || !$password){
	 		return [true,"",""];
	 	}
	 	if($.trim(p.value) != $password){
	 		return [false,"两次输入密码不一致！",""];
	 	}
	 	
		var v = getPasswordStrength(p.value); 
		if(v > 1){
			return [true,"",""];
		}else{
			return [false,"密码太弱，有被盗风险，至少６个字符，必须使用字母、<br>数字或符号的组合，建议设置多种字符组成的复杂密码！",""];
		} 
	 },	 
	 
	 getGridNames : function(selector){
		  var grids=[];
		  selector.each(function(i){
          	var id = this.id;
          	var index = id.indexOf("gbox_");
          	if(index != -1){            		
          		var gridid = id.substring(index+5);					            		
          		grids.push(gridid);
          	}
          });
		  return grids;
	 },
	 
	 info_dialog2 :function(caption, content,p){
		 var positions = $.jgrid.findPositions(290,50);
			p =  $.extend({left:positions[0],top:positions[1]}	,p  || {});			
		$.jgrid.info_dialog(caption,content,'',p);	
	 },	 
	 
	 error_dialog :function(caption, content,c_b, modalopt){
		 var positions = $.jgrid.findPositions(290,50);
		 modalopt =  $.extend({left:positions[0],top:positions[1]}	, modalopt || {});	
		 var $align = modalopt.align || "center";
    	var ret = '<div align="'+$align+'" class="ui-widget" >';
    	ret += '<div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">';
    	ret += '<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>';    	
    	ret += content;
    	ret += '</p>';
    	ret += '</div>';
    	ret += '</div>';		 
		 $.jgrid.info_dialog(caption, ret,c_b, modalopt); 
	 },
	 
	 error_dialog2 :function(caption, content, modalopt){
		 $.jgrid.error_dialog(caption, content,'', modalopt);	
	 },
	 
	 form_dialog : function(gridid,p,ajaxso){
			p =  $.extend({				
				width:500,
				height:400,
				autoOpen: false,
				align: 'center',
				modal: true,
				jqModal:true,
				closeOnEscape : true
			},p  || {});		
			
			$.ajax($.extend({	
				type: "GET",
				dataType: "html",
				complete : function (req, err) {
					if(parseInt(req.status,10) == 200){
							
						var elem  = document.createElement('div');
						$(elem).attr("id","editcnt"+gridid);
						$(elem).attr("name","editcnt"+gridid);
						$(elem).addClass("ui-jqdialog-content");
						$('body').append(elem);
						
						$(elem).html(req.responseText);	// 打开对话框	
						
						
						$(elem).dialog($.extend({						
							close : function(event, ui){
								 $( this ).dialog("destroy"); 
								 $(elem).remove();
							}
							//resizeStop: function( event, ui ) {
								//让其中的form随其大小的改变而改变
								//var originalSize = ui.originalSize;
								//var size = ui.size;	

								//var vh1 = $(elem).parent().find('.ui-dialog-titlebar').height()||0;
								//var vh2 = $(elem).parent().find('.ui-dialog-buttonpane').height()||0;
								
								//$('#'+o.formid).width($('#'+o.formid).width()+(size.width-originalSize.width));
								//$('#'+o.formid).height(size.height-(vh1+vh2)*2);								
							//}						
						},p)).dialog('open');
						
						var $parent = $(elem).parent('.ui-dialog');
						$parent.attr('id',"editmodc"+gridid);
						$parent.addClass('ui-jqdialog');	
						if(p.zIndex){
							$parent.css('z-index',p.zIndex);
						}
						$('.ui-dialog-titlebar',$parent).attr('id',"edithdc"+gridid);
						$('.ui-dialog-titlebar',$parent).addClass('ui-jqdialog-titlebar');							
					}
				}
			},ajaxso || {}));
				
		},	 
	 
	 grid_dialog : function(targetgrid,p,ajaxso){
		p =  $.extend({
			title:'',
			width:500,
			height:400,
			autoOpen: false,
			align: 'center',
			modal: true			
		},p  || {});		
				
		$.ajax($.extend({	
			type: "GET",
			dataType: "html",
			complete : function (req, err) {
				if(parseInt(req.status,10) == 200){
					
					var elem  = document.createElement('div');
					$(elem).attr("id",targetgrid+"_dialog");
					$(elem).attr("name",targetgrid+"_dialog");
					$(elem).addClass("ui-jqdialog-content");
					$('body').append(elem);
					
					$(elem).html(req.responseText);	// 打开对话框					
					$(elem).dialog($.extend({						
						close : function(event, ui){
							 $( this ).dialog("destroy"); 
							 $(elem).remove();
						},
						resizeStop: function( event, ui ) {
							//让其中的grid随其大小的改变而改变
							var originalSize = ui.originalSize;
							var size = ui.size;							
							
							var vh1 = $(elem).parent().find('.ui-dialog-titlebar').height()||0;
							var vh2 = $(elem).parent().find('.ui-dialog-buttonpane').height()||0;
							var vh3 = $.jgrid.getGridPagerHeight(targetgrid)||0;//grid 分页栏高度	
							var vh4 = $.jgrid.getGridTitleBarHeight(targetgrid);//grid title 高度			
							var vh5 = $.jgrid.getGridHdivHeight(targetgrid);//grid 标题栏高度	
							var vh6 = $.jgrid.getSummaryHeight(targetgrid);
							var vh7 = $.jgrid.getTToolBarHeight(targetgrid);//自定义工具栏高度
							var vh8 = $.jgrid.getTbToolBarHeight(targetgrid);//自定义工具栏高度							
							
							$('#'+targetgrid).jqGrid('setGridWidth',$('#'+targetgrid).width()+(size.width-originalSize.width));
							$('#'+targetgrid).jqGrid('setGridHeight',size.height-(vh1+vh2+vh3+vh4+vh5+vh6+vh7+vh8)*2);
						}						
					},p)).dialog('open');
					
					var $parent = $(elem).parent('.ui-dialog');					
					$parent.addClass('ui-jqdialog');
					if(p.zIndex){
						$parent.css('z-index',p.zIndex);
					}
					$('.ui-dialog-titlebar',$parent).addClass('ui-jqdialog-titlebar');					
				}
			}
		},ajaxso || {}));
			
	 },
	 
	 /**
	  * 得到grid数据显示的实际高度
	  * @param gridid
	  * @returns
	  */
	 getGridHeight :function(gridid,p){	
		
		//var gbox = "#gbox_"+gridid;
		 
		var tabsnav = $.jgrid.getTabsNavHeight(gridid);//tab标签高度			
		//var jqgridtitlebar = $.jgrid.getGridTitleBarHeight(gridid);//grid title 高度			
		//var jqgridhdiv = $.jgrid.getGridHdivHeight(gridid);//grid 标题栏高度
		var toolbarhdiv = $.jgrid.getToolbarHeight(gridid);//查询工具栏高度		
		var jqgridpager = $.jgrid.getGridPagerHeight(gridid);//grid 分栏高度			
		var ttoolbarH = $.jgrid.getTToolBarHeight(gridid);//自定义工具栏高度
		var tbtoolbarH = $.jgrid.getTbToolBarHeight(gridid);//自定义工具栏高度
		var summaryH = $.jgrid.getSummaryHeight(gridid);
		
		
		
	   var vh_north =  $('.ui-state-default-north').height();
	   var vh_south =  $('.ui-state-default-south').height();
	   var vh_boths = (vh_north||0)+(vh_south||0); 			
	
		var vh = tabsnav+jqgridpager+toolbarhdiv+ttoolbarH+tbtoolbarH+vh_boths+summaryH;
		
		var ovh = 0;
		if(p && p.ogridid){
			var tabsnav = $.jgrid.getTabsNavHeight(p.ogridid);//tab标签高度	
			var jqgridtitlebar = $.jgrid.getGridTitleBarHeight(p.ogridid);//grid title 高度			
			var jqgridhdiv = $.jgrid.getGridHdivHeight(p.ogridid);//grid 标题栏高度
			var jqgridpager = $.jgrid.getGridPagerHeight(p.ogridid);//grid 分栏高度
			var ttoolbarH = $.jgrid.getTToolBarHeight(p.ogridid);//自定义工具栏高度
			var tbtoolbarH = $.jgrid.getTbToolBarHeight(p.ogridid);//自定义工具栏高度	
			var summaryH = $.jgrid.getSummaryHeight(gridid);
			
			ovh = tabsnav+jqgridtitlebar+jqgridhdiv+jqgridpager+ttoolbarH+tbtoolbarH+vh_boths+summaryH;			
			vh = vh+ovh;
		}	
	
		var vhh = 0;
		if(p && p.height){
			vhh = p.height;
		}
		
		vh = $('#westPane').innerHeight() - $('#southPane').innerHeight() -vh + vhh;
		if(p && p.rate && p.rate > 0){
			vh = vh * p.rate;
		}
		
		return vh;
	 },
	 
	 getGridHeight2 :function(gridid,p){
					
			var jqgridtitlebar = $.jgrid.getGridTitleBarHeight(gridid);//grid title 高度			
			var jqgridhdiv = $.jgrid.getGridHdivHeight(gridid);//grid 标题栏高度
			var toolbarhdiv = $.jgrid.getToolbarHeight(gridid);//查询工具栏高度		
			var jqgridpager = $.jgrid.getGridPagerHeight(gridid);//grid 分栏高度			
			var ttoolbarH = $.jgrid.getTToolBarHeight(gridid);//自定义工具栏高度
			var tbtoolbarH = $.jgrid.getTbToolBarHeight(gridid);//自定义工具栏高度
			var summaryH = $.jgrid.getSummaryHeight(gridid);			
			
		   var vh_north =  $('.ui-state-default-north',window.parent.document).height();
		   var vh_south =  $('.ui-state-default-south',window.parent.document).height();
		   var vh_boths = (vh_north||0)+(vh_south||0);		  
		
		   var vh = jqgridtitlebar+jqgridhdiv+jqgridpager+toolbarhdiv+ttoolbarH+tbtoolbarH+vh_boths+summaryH;		 
			
			var ovh = 0;
			if(p && p.ogridid){				
				var jqgridtitlebar = $.jgrid.getGridTitleBarHeight(p.ogridid);//grid title 高度			
				var jqgridhdiv = $.jgrid.getGridHdivHeight(p.ogridid);//grid 标题栏高度
				var toolbarhdiv = $.jgrid.getToolbarHeight(gridid);//查询工具栏高度	
				var jqgridpager = $.jgrid.getGridPagerHeight(p.ogridid);//grid 分栏高度
				var ttoolbarH = $.jgrid.getTToolBarHeight(p.ogridid);//自定义工具栏高度
				var tbtoolbarH = $.jgrid.getTbToolBarHeight(p.ogridid);//自定义工具栏高度	
				var summaryH = $.jgrid.getSummaryHeight(gridid);
				
				ovh = jqgridtitlebar+jqgridhdiv+jqgridpager+ttoolbarH+tbtoolbarH+vh_boths+summaryH+toolbarhdiv;			
				vh = vh+ovh;
			}	
		
			var vhh = 0;
			if(p && p.height){
				vhh = p.height;
			}
			
			var vhe = $('#centerPane',window.parent.document).innerHeight()-$('#tabs',window.parent.document).find(".ui-tabs-nav").outerHeight();			
			vh = vhe - vh + vhh;
			if(p && p.rate && p.rate > 0){
				vh = vh * p.rate;
			}			
			
			return vh;
		 },	 
	 
	 getSummaryHeight :function(gridid){
		 var gview = "#gview_"+gridid;		 
		 var vh = $('.ui-jqgrid-sdiv',gview).height();//grid summay汇总栏高度	
		 return vh ? vh : 0;		 
	 },
	 
	 /**
	  * 自定义工具栏高度
	  * @param gridid
	  * @returns
	  */
	 getTbToolBarHeight :function(gridid){
		 var gview = "#gview_"+gridid;
		var tbtoolbar = "#tb_"+gridid;
		var vh = $(tbtoolbar,gview).height();//自定义工具栏高度
		 return vh ? vh : 0;
	 },	 
	 
	 /**
	  * 自定义工具栏高度
	  * @param gridid
	  * @returns
	  */
	 getTToolBarHeight :function(gridid){
		 var gview = "#gview_"+gridid;
		var ttoolbar = "#t_"+gridid;
		var vh = $(ttoolbar,gview).height();//自定义工具栏高度
		 return vh ? vh : 0;
	 },
	 
	 /**
	  * grid 分页栏高度
	  * @param gridid
	  * @returns
	  */
	 getGridPagerHeight:function(gridid){
		 var gbox = "#gbox_"+gridid;
		 var vh = $('.ui-jqgrid-pager',gbox).height();//grid 分页栏高度
		 return vh ? vh : 0;
	 },
	 
	 /**
	  * grid 查询工具栏高度
	  * @param gridid
	  * @returns
	  */
	 getToolbarHeight:function(gridid){
		 var gview = "#gview_"+gridid;		 
		 var vh = $('.ui-search-toolbar',gview).height();		 
		 return vh ? vh : 0;		 	
	 },
	 
	 /**
	  * grid 标题栏高度
	  * @param gridid
	  * @returns
	  */
	 getGridHdivHeight:function(gridid){
		 var gview = "#gview_"+gridid; 
		 var vh = $('.ui-jqgrid-hdiv',gview).height();//grid 标题栏高度		 
		 return vh ? vh : 0;
	 },
	 
	 /**
	  * grid title 高度
	  * @param gridid
	  * @returns
	  */
	 getGridTitleBarHeight :function(gridid){
		 var gview = "#gview_"+gridid;
		 var vh = $('.ui-jqgrid-titlebar',gview).height();//grid title 高度		
		 return vh ? vh : 0;
	 },
	 
	 /**
	  * 获取grid父级tab标签高度
	  * @param gridid
	  * @returns
	  */
	 getTabsNavHeight:function(gridid){
		 var gbox = "#gbox_"+gridid;
		 //gbox = gbox.indexOf('#') == -1 ? '#'+gbox : gbox;
		 var vh = $(gbox).parent('.ui-tabs-panel').parent().find('.ui-tabs-nav').height();//tab标签高度
		 
		 var ovh = 0;
		 if($(gbox).parent('.ui-tabs-panel').parent().parent('.ui-tabs-panel').parent().find('.ui-tabs-nav')){
			 ovh = $(gbox).parent('.ui-tabs-panel').parent().parent('.ui-tabs-panel').parent().find('.ui-tabs-nav').height();
			 vh = vh+ovh;
		 }
		 
		 return  vh ? vh : 0;
	 },
	 
	 /**
	  * 根据元素的width,height找出元素居中位置
	  * @param width
	  * @param height
	  * @returns {Array}
	  */
	 findPositions:function(width,height){
		 var curleft = 0, curtop = 0;
		if (typeof window.innerWidth != 'undefined') {
			curleft = window.innerWidth;
			curtop = window.innerHeight;
		} else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined' && document.documentElement.clientWidth !== 0) {
			curleft = document.documentElement.clientWidth;
			curtop = document.documentElement.clientHeight;
		} else {
			curleft=1024;
			curtop=768;
		}
		//var width = $(obj).width() || 0;
		//var height = $(obj).height() || 0;		
		return [curleft/2-width/2,curtop/2-height/2];
	 }
 });
	 
})(jQuery);
 
