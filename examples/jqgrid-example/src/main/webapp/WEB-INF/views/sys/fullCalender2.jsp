<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8" />
	<title>${fns:getConfig("productName")}</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<%@ include file="/WEB-INF/common/assets.css.jsp"%>
	<link href="${ctx}/static/css/style.css" rel="stylesheet">
</head>

<body>
	<!-- BEGIN CONTAINER -->
	<div id="container" class="row-fluid">
		<div class="row-fluid">
			<div class="span8 responsive" data-tablet="span8 fix-margin" data-desktop="span8">
				<!-- BEGIN CALENDAR PORTLET-->
				<div class="widget">
					<div class="widget-title">
						<h4>
							<i class="icon-calendar"></i> Calendar
						</h4>
						<span class="tools"> <a href="javascript:;" class="icon-chevron-down"></a> <a href="javascript:;"
							class="icon-remove"></a>
						</span>
					</div>
					<div class="widget-body">
						<div id="calendar" class="has-toolbar"></div>
					</div>
				</div>
				<!-- END CALENDAR PORTLET-->
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/common/jquery.js.jsp"%>
	<%@ include file="/WEB-INF/common/assets.js.jsp"%>
	<script src="${ctx}/static/script/my.bootstrap.js"></script>
	<script>
			jQuery().ready(function (){
			    jQuery('.widget .tools .icon-remove').click(function () {
			        jQuery(this).parents(".widget").parent().remove();
			    });

			    jQuery('.widget .tools .icon-refresh').click(function () {
			        var el = jQuery(this).parents(".widget");
			        App.blockUI(el);
			        window.setTimeout(function () {
			            App.unblockUI(el);
			        }, 1000);
			    });

			    jQuery('.widget .tools .icon-chevron-down, .widget .tools .icon-chevron-up').click(function () {
			        var el = jQuery(this).parents(".widget").children(".widget-body");
			        if (jQuery(this).hasClass("icon-chevron-down")) {
			            jQuery(this).removeClass("icon-chevron-down").addClass("icon-chevron-up");
			            el.slideUp(200);
			        } else {
			            jQuery(this).removeClass("icon-chevron-up").addClass("icon-chevron-down");
			            el.slideDown(200);
			        }
			    });
			    
			    if (!jQuery().fullCalendar) {
			        return;
			    }

			    var date = new Date();
			    var d = date.getDate();
			    var m = date.getMonth();
			    var y = date.getFullYear();

			    var h = {};
			    if ($(window).width() <= 320) {
			        h = {
			            left: 'title, prev,next',
			            center: 'prevYear,nextYear',
			            right: 'today,month,agendaWeek,agendaDay'
			        };
			    } else {
			        h = {
			            left: '',
			            center: 'title',
			            right: 'prev,next,today,month,agendaWeek,agendaDay'
			        };
			    }
			        
				$('#calendar').html("");
				
				$('#calendar').fullCalendar({
					header: h,
			        editable: false,
			        titleFormat:{
					    month: 'yyyy MMMM',                             // September 2009
					    week: "yyyy MMM d{ '&#8212;'[yyyy] [ MMM] d}", // Sep 7 - 13 2009
					    day: 'yyyy, MMM d, dddd'                  // Tuesday, Sep 8, 2009
					},
					buttonText:{
						prev:     '上一个',
						next:     '下一个',
						prevYear: '去年',
						nextYear: '明年',
						today:    '今天',
						month:    '月',
						week:     '周',
						day:      '日'
					},
					monthNames:['一月','二月', '三月', '四月', '五月', '六月', '七月','八月', '九月', '十月', '十一月', '十二月'],
					monthNamesShort:['一月','二月', '三月', '四月', '五月', '六月', '七月','八月', '九月', '十月', '十一月', '十二月'],
				    dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
					dayNamesShort:['星期日', '星期一', '星期二', '星期三','星期四', '星期五', '星期六'],
//				 		        events: [{"id":null,"title":"当天任务1","allDay":false,"start":new Date(y, m, d - 3),"end":null,"url":null,"className":'label label-default',"editable":false,"source":null,"color":null,"backgroundColor":null,"borderColor":null,"textColor":null},{"id":null,"title":"当天任务2","allDay":false,"start":new Date(y, m, d),"end":null,"url":null,"className":'label label-success',"editable":false,"source":null,"color":null,"backgroundColor":"blue","borderColor":null,"textColor":null}],
					//下一页按钮的触发事件
					viewDisplay: function(view) {
//					       alert('The new title of the view is ' + view.title);
				    },
					//当点击某一天时触发此操作
					dayClick: function(date, allDay, jsEvent, view) {
				        if (allDay) {
				            alert('Clicked on the entire day: ' + date);
				        }else{
				            alert('Clicked on the slot: ' + date);
				        }
				        alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
				        alert('Current view: ' + view.name);
				        // change the day's background color just for fun
				        $(this).css('background-color', 'red');
				    },
				   //当点击某一个事件时触发此操作
				    eventClick:function( event, jsEvent, view ){
						//alert("eventClick="+event.url);
						if(event.url){
							window.open(event.url, 'Event-窗口', 'width='+$(window).width()+',height='+$(window).height());
						}
						return false;
					},
				 	//当鼠标悬停在一个事件上触发此操作
				    eventMouseover:function( event, jsEvent, view ) {
				    	//alert("eventMouseover="+event.title);
				    },
				    //当鼠标从一个事件上移开触发此操作
				    eventMouseout:function( event, jsEvent, view ) {
				    	//alert("eventMouseout="+event.title);
				    },
				    events:"${ctx}/public/searchEvents1"
				});
				
			});
	</script>
</body>
</html>