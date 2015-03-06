require(["jquery", "jquery-ui", "jquery-ui-zh-CN","jquery-layout",
        "jqgrid-cn","jqgrid-ui.multiselect","jqqrid","jqqrid-extend","jquery-ui-tabs-paging",
        "themeswitchertool","jquery-ui-tabs-extend","jquery-resize",
        "multiselect2side","jquery-zTree","jquery-ui-timepicker","jquery-form"], function($) {
   
   
	var myLayout;
	var innerLayouts = [];
	var tabgrids = [];
	jQuery(document).ready(function(){
	    
		myLayout = $('body').layout({
			resizerClass: 'ui-state-default',
			slidable:true,
			livePaneResizing:true,
			resizerTip:"调整大小",//鼠标移到边框时，提示语 
			sliderTip:"显示/隐藏",//在某个Pane隐藏后，当鼠标移到边框上显示的提示语。
			togglerTip_open:"关闭",//pane打开时，当鼠标移动到边框上按钮上，显示的提示语  
		    togglerTip_closed:"打开",//pane关闭时，当鼠标移动到边框上按钮上，显示的提示语 
		    
			north__resizable:false,
			//north__size:129,
			
			south__resizable:false,
			south__size:50,

			west__size:220,
			west__minSize:100,
			west__maxSize:300,
			west__togglerLength_closed:	100,
			west__togglerAlign_closed:	"top",
			west__togglerContent_closed:"导<BR>航<BR>菜<BR>单",
			west__togglerTip_closed:	"打开导航菜单",
			//west__sliderTip:			"Slide Open Menu",
			//west__slideTrigger_open:	"click",
			west__spacing_closed:	20,			// wider space when closed
	        west__onresize: function (pane, $pane, state, options) {        	
	            $("#west-grid").jqGrid('setGridWidth',$pane.innerWidth()-2);
	            
	     	   var vh_north =  $('.ui-state-default-north').height();
	    	   var vh_south =  $('.ui-state-default-south').height();
	    	   var vh_boths = (vh_north||0)+(vh_south||0); 	 
	    	   
	            $("#west-grid").jqGrid('setGridHeight',$pane.innerHeight()- $('#southPane').innerHeight()-vh_boths);
			},
			center__onresize:	function (pane, $pane, state, options) {						
				var $jqgtabs = $pane.children('.jqgtabs') ;			
				$(".autolayoutwidth").jqGrid('setGridWidth',$jqgtabs.innerWidth()-2);
				$(".autowidth").jqGrid('setGridWidth',$jqgtabs.innerWidth()-2);
				$(".autowidth50").jqGrid('setGridWidth',$jqgtabs.innerWidth()*0.5-5);					
				
				$("iframe",$jqgtabs).contents().find(".autolayoutwidth").jqGrid('setGridWidth',$jqgtabs.innerWidth()-2);			
				//$("iframe",$jqgtabs).width($jqgtabs.innerWidth());			
				$("iframe",$jqgtabs).contents().find(".autowidth").jqGrid('setGridWidth',$jqgtabs.innerWidth()-2);			
				$("iframe",$jqgtabs).contents().find(".autowidth50").jqGrid('setGridWidth',$jqgtabs.innerWidth()*0.5-5);
				
				resetPage();			
			}		
		});		
		
		
		function resizeLayout(){
			
			if(innerLayouts && innerLayouts.length > 0){
				$.each(innerLayouts,function(i,item){
					var layout = item.layout;
					if(layout){
						layout.resizeAll();
					}
				});
			}		
			
			 $(".autolayoutheight").each(function(i){
				 var layoutid = this.id;
				 if(layoutid){
					 $(this).height($('#centerPane').innerHeight()- $.jgrid.getTabsNavHeight(layoutid));
				 }
			 });		
		}
		
		function resizeGridHeight(){
			
			 $(".autoheight").each(function(i){
				 var gridid = this.id;				
				if(gridid){					
					//var vh = $.jgrid.getGridHeight(gridid);						
					$(this).jqGrid('gridHeight');
				}				 
			 });	
			 
			 $("iframe",'#tabs','#centerPane').each(function(i){
				var _iframe = this;
				var vh = $('#centerPane').innerHeight()-$(".ui-tabs-nav",'#tabs').outerHeight();		
				$(this).height(vh);	
				
				 $(this).contents().find(".autoheight").each(function(i){
					var gridid = this.id;				
					if(gridid && _iframe.contentWindow.setGridHeight){
						_iframe.contentWindow.setGridHeight(gridid);
					}				 
				 });			
			 });
			 
			 
			 var ids =[];
			 $(".autoheight50").each(function(i){
				 var gridid = this.id;				
				ids.push( gridid);				
			 });
			 if(ids && ids.length > 0){				
				 for(var i =0; i< ids.length;i=i+2){					
					 if((i+1) <= (ids.length -1)){
						 $("#"+ids[i]).jqGrid('gridHeight',{ogridid:ids[i+1],rate:0.5});
						 $("#"+ids[i+1]).jqGrid('gridHeight',{ogridid:ids[i],rate:0.5}); 
					 }else{
						 $("#"+ids[i]).jqGrid('gridHeight',{rate:0.5}); 
					 }					 
				 }				 
			 }			
		}
		
		function resetPage(){
			// reinit paging whenever new theme is selected    			
			jQuery('#tabs','#centerPane').tabs('pagingDestroy');		
			initPaging();
			resizeGridHeight();
			resizeLayout();			
		}
		
		var maintab =jQuery('#tabs','#centerPane').tabs({
	        add: function(e, ui) {
	        	
	            // append close thingy
	            $(ui.tab).parents('li:first')
	                .append('<span class="ui-icon ui-icon-close" title="关闭页签"></span>')
	                .find('span.ui-icon-close')
	                .click(function() {   
	                	var selected = maintab.tabs('option','selected');                	
	                	maintab.tabs('remove',selected);
	                   // maintab.tabs('remove', $('li', maintab).index($(this).parents('li:first')[0]));
	                });
	            // select just added tab
	            maintab.tabs('select', '#' + ui.panel.id);           
	        },  
	        
	        show:function(e, ui){
	        	jQuery('#tabs','#centerPane').tabs('select', '#' + ui.panel.id);  
	        },
	        
	        remove :function(event, ui){        	
	        	//关闭tab页时，将inner layout从innerLayouts中删除
	        	if(innerLayouts && innerLayouts.length > 0){
		        	//根据tab页名称，将inner layout从innerLayouts中删除
		        	innerLayouts = $.grep(innerLayouts,function(val,key){
		        		return val.tabid != ui.panel.id;
		        	});	        	
	        	} 
	        	
	        	if(tabgrids && tabgrids.length > 0){
		        
	        		$.each($.grep(tabgrids,function(val,key){
		        		return val.tabid == ui.panel.id;
		        	}),function(i,item){
	        			$.each(item.grids,function(i,gridid){
	        				$('#alertmod_'+gridid).remove();
	        				$('.ui-jqdialog',$('body')).each(function(i){
	        					var alertmodid = this.id;        					
	        					var subgridid = gridid.substring(gridid.length - 32,gridid.length);        					
	        					if(alertmodid.indexOf('alertmod_'+gridid) != -1 
	        					||(alertmodid.indexOf('alertmod_') != -1 && alertmodid.indexOf(subgridid) != -1  )){
	        						$(this).remove();
	        					}
	        				});
	        			});
	        		});
		        	
		        	
		        	tabgrids = $.grep(tabgrids,function(val,key){
		        		return val.tabid != ui.panel.id;
		        	});	
		        	
	        	}
	        }
	    });
		// initialize paging
		function initPaging() {		
			maintab.tabs('paging', { cycle: true, follow: true, followOnActive: true, activeOnAdd: true });
		}
		initPaging();
		
	    jQuery("#west-grid").jqGrid({
	        url: ctx+"/static/tree.json",
	        datatype: "json",
	        height: "auto",
	        pager: false,
	        loadui: "disable",
	        colNames: ["id","url",""],
	        colModel: [
	            {name: "id",width:0,hidden:true, key:true},
	            {name: "url",width:0,hidden:true},
	            {name: "name", width:150, resizable: true, sortable:false}            
	        ],
	        treeGrid: true,
	        treeGridModel: "adjacency",
			caption: "导航菜单",
	        ExpandColumn: "name",
	        autowidth: true,      
	        rowNum: 200,
	        ExpandColClick: true,
	        treeIcons: {leaf:'ui-icon-document-b'},
	 	  
		    onSelectRow: function(rowid) {
		        var treedata = $("#west-grid").jqGrid('getRowData',rowid);
		        if(treedata.isLeaf=="true" && treedata.url) {
		        	
		            var st = "#t"+treedata.id;
					if($(st).html() != null ) {
						maintab.tabs('select',st);
					} else {					
						var url = ctx+'/'+treedata.url;
						if(url.indexOf("_target=iframe") == -1){						
							$.ajax({
								url: url,
								type: "GET",
								dataType: "html",
								//error:function(xhr, textStatus, errorThrown){
									//maintab.tabs('remove', $('li', maintab).index($(this).parents('li:first')[0]));
								//},
								complete : function (req, err) {
									if(parseInt(req.status,10) == 200){
										maintab.tabs('add',st, treedata.name);
										$(st,maintab).append(req.responseText);	
										
							            var grids= $.jgrid.getGridNames($('.ui-jqgrid',st,"#tabs"));					            
							            if(grids && grids.length > 0){
							            	tabgrids.push({"tabid":"t"+treedata.id,"grids":grids});
							            }								
									}
								}
							});
						}else{	
							var vw = $('#centerPane').width()-10;
							var vh = $('#centerPane').innerHeight()-$(".ui-tabs-nav",maintab).outerHeight();	        	
							maintab.tabs('addIframe',{id:treedata.id,name:treedata.name,url:url,width:vw,height:vh});					
						}
					}	        	
		        }
		    }	   
	    });     
	  
	   var vh_north =  $('.ui-state-default-north').height();
	   var vh_south =  $('.ui-state-default-south').height();
	   var vh_boths = (vh_north||0)+(vh_south||0);
	   
	    $("#west-grid").jqGrid('setGridHeight',$('#westPane').innerHeight() - $('#southPane').innerHeight()-vh_boths); 
		

	    /**
	     *	addThemeSwitcher
	     *
	     *	Remove the cookie set by the UI Themeswitcher to reset a page to default styles
	     *
	     *	Dependancies: /lib/js/themeswitchertool.js
	     */
	    function addThemeSwitcher ( container, position,settings ) {
	    	var pos = { top: '10px', right: '10px', zIndex: 10 };
	    	$('<div id="themeContainer" style="position: absolute; overflow-x: hidden;"></div>')
	    		.css( $.extend( pos, position ) )
	    		.appendTo( container || 'body')
	    		.themeswitcher(settings)
	    	;
	    };

	    /**
	     *	removeUITheme
	     *
	     *	Remove the cookie set by the UI Themeswitcher to reset a page to default styles
	     */
	    function removeUITheme ( cookieName, removeCookie ) {
	    	$('link.ui-theme').remove();
	    	$('.jquery-ui-themeswitcher-title').text( 'Switch Theme' );
	    	if (removeCookie !== false)
	    		$.cookie( cookieName || 'jquery-ui-theme', null );
	    };
	    addThemeSwitcher('#northPane',{ top: '50px', right: '10px' },
		{uipath:ctx+'/static/js/lib/jquery-ui-1.9.2.custom',
			uifilename:'jquery-ui-1.9.2.custom.min.css',
			onselect: function() {						
				setTimeout(resetPage, 500);					
			}    		
		});
	    
	    
	    
	});
	
});