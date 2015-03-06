(function($) {   
    $.extend($.ui.tabs.prototype, {
    	
    	/**    	
    	 * @param p
    	 */
    	addIframe :function(p){
    		p = $.extend({
    			frameborder:0,
    			scrolling:'no',
    			height:'100%',
    			width:'100%'
    		},p||{});
    		
    		var self = this;
    		var st = "#t"+p.id;
			if($(st).html() != null ) {
				self.select(st);
			} else {
				
				$.each(p.url.split('?')[1].split("&"),function(i,item){
					if(item){
						if(item.indexOf("_frameborder=") != -1){
							var items = item.split("=");
							p = $.extend(p,{frameborder:items[1]});
						}
						if(item.indexOf("_scrolling=") != -1){
							var items = item.split("=");
							p = $.extend(p,{scrolling:items[1]});
						}
						if(item.indexOf("_height=") != -1){
							var items = item.split("=");
							p = $.extend(p,{height:items[1]});
						}
						if(item.indexOf("_width=") != -1){
							var items = item.split("=");
							p = $.extend(p,{width:items[1]});
						}						
					}
				});					
				
				$.ajax({
					url: ctx+'/iframe?_ajax=true&id=f'+p.id+'&url='+p.url+'&frameborder='+p.frameborder+'&scrolling='+p.scrolling+'&width='+p.width+'&height='+p.height,
					type: "GET",
					dataType: "html",					
					complete : function (req, err) {
						if(parseInt(req.status,10) == 200){
							self.add(st, p.name);				
							$(st).append(req.responseText);	
							
							$('#f'+p.id).load(function(){
								var _iframe = this;					
								var uitheme = $.cookie('jquery-ui-theme');
								if(uitheme){
									 $('span','#themeGalleryUL',window.parent.document).each(function(i){
										if($(this).text() == uitheme){
											var locStr = $(this).parent().attr('href');
											$(_iframe).contents().find("#jquery-ui-css").attr('href',locStr);
										}
									 });
								}
							});								
						}
					}
				});				
				

			}
    	},
        next: function() {
            var self = this, o = this.options;
            var i = o.selected;
            var n = i + 1;
            while ((n < self.anchors.length) &&
            (self.lis.eq(n).hasClass('ui-state-disabled') ||
            (self.lis.eq(n).css("display") == "none"))) {
                n++;
            }
            if (n < self.anchors.length) {
                if (self._trigger('beforegotonext',
                    null,
                    self._ui(self.anchors[i],
                    self.panels[i])) !== false)
                    self.select(n);
            }
        },
        prev: function() {
            var self = this, o = this.options;
            var p = o.selected - 1;
            while ((p >= 0) &&
            (self.lis.eq(p).hasClass('ui-state-disabled') ||
            (self.lis.eq(p).css("display") == "none"))) {
                p--;
            }
            if (p >= 0)
                self.select(p);
        }
    });
})(jQuery);