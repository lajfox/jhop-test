jQuery().ready(function() {			
	
	$('#verifyCodeimg').click(function(){				
		$(this).attr('src',ctx+'/static/captcha.jpg?_time='+new Date().getTime());
	});	
	
	function ajaxlogin(){
		var rememberMe = false;		
		if($('#rememberMe').attr("checked")){
			rememberMe = true;
		}		
		$.ajax({
			url: ctx+'/login',
			type: "post",
			data:{username:$('#username').val(),password:$('#password').val(),rememberMe:rememberMe,verifyCode:$('#verifyCode').val()},
			dataType: "json",
			beforeSend:function(req){
				var username=$.trim($('#username').val());							
				if(!username){
					$('#ajaxlogin_error').html('请输入用户名！');
					$('#username').focus();
					return false;
				}
				var password=$.trim($('#password').val());
				if(!password){
					$('#ajaxlogin_error').html('请输入密码！');
					$('#password').focus();
					return false;
				}
				var verifyCode=$.trim($('#verifyCode').val());
				if(!verifyCode){
					$('#ajaxlogin_error').html('请输入验证码！');
					$('#verifyCode').focus();
					return false;
				}							
			},
			error:function(xhr, textStatus, errorThrown){
				$('#ajaxlogin_error').html('登录失败');
				$('#verifyCodeimg').attr('src',ctx+'/static/captcha.jpg?_time='+new Date().getTime());
			},
			success : function (data, textStatus) {	
				if(data.success){
					$('#editcntajaxlogin_dialog').dialog("close");
				}else{
					$('#verifyCodeimg').attr('src',ctx+'/static/captcha.jpg?_time='+new Date().getTime());
					$('#ajaxlogin_error').html(data.message);
				}
			}
		});			
	}
	
	//回车登录
	$('input','#ajaxlogin_form').keydown( function(e) {
		if(e.keyCode != 13){
			return;
		}					
		ajaxlogin();   		
	}); 				
	
	$('#ajaxlogin_sData').click(function (){
		ajaxlogin();
	});
	
	$('#ajaxlogin_cData').click(function (){
		$('#editcntajaxlogin_dialog').dialog("close");
	});
	

});