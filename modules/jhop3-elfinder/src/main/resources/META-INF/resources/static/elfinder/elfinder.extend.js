
if (elFinder && elFinder.prototype && typeof(elFinder.prototype.kinds) == 'object') {	
	$.extend(elFinder.prototype.kinds,
		{
		'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet':'MsExcel',
		'application/x-compressed'               	: 'ZIP',
		'application/x-zip-compressed'           	: 'ZIP',
		'multipart/x-zip'               			: 'ZIP',
		'application/vnd.ms-project'               	: 'MsProject',
		'application/vnd.ms-vision'               	: 'MsVision',
		'application/x-visio'               	    : 'MsVision'
		}
	);	
}

if (elFinder && elFinder.prototype && typeof(elFinder.prototype.i18.zh_CN.messages) == 'object') {	
	$.extend(elFinder.prototype.i18.zh_CN.messages,
		{			
			'kindMsProject'     : 'Microsoft Project 文档',
			'kindMsVision'      : 'Microsoft Vision 文档',
			'dropFilesBrowser'  : '从浏览器中拖拽或粘贴文件到此'
		}
	);	
}


/*
 * File: /home/osc/elFinder-build/elFinder/js/commands/search.js
 */

/**
 * @class  elFinder command "search"
 * Find files
 *
 * @author Dmitry (dio) Levashov
 **/
elFinder.prototype.commands.search = function() {
	this.title          = 'Find files';
	this.options        = {ui : 'searchbutton'}
	this.alwaysEnabled  = true;
	this.updateOnSelect = false;
	
	/**
	 * Return command status.
	 * Search does not support old api.
	 *
	 * @return Number
	 **/
	this.getstate = function() {
		return 0;
	}
	
	/**
	 * Send search request to backend.
	 *
	 * @param  String  search string
	 * @return $.Deferred
	 **/
	this.exec = function(q) {
		var fm = this.fm;
		
		if (typeof(q) == 'string' && q) {
			fm.trigger('searchstart', {query : q});			
			return fm.request({
				//modify by ZengWenfeng
				//修改搜索只查询当前目录，增加target参数为当前目录：fm.cwd().hash
				//data   : {cmd : 'search', q : q},
				data   : {cmd : 'search', q : q,target:fm.cwd().hash},
				notify : {type : 'search', cnt : 1, hideCnt : true}
			});
		}
		fm.getUI('toolbar').find('.'+fm.res('class', 'searchbtn')+' :text').focus();
		return $.Deferred().reject();
	}

}

/*
 * File: /home/osc/elFinder-build/elFinder/js/commands/copy.js
 */
/**
 * @class elFinder command "copy".
 * Put files in filemanager clipboard.
 *
 * @type  elFinder.command
 * @author  Dmitry (dio) Levashov
 */
elFinder.prototype.commands.copy = function() {
	
	this.shortcuts = [{
		pattern     : 'ctrl+c ctrl+insert'
	}];
	
	this.getstate = function(sel) {
		var sel = this.files(sel),
			cnt = sel.length;
		//modify by ZengWenfeng
		//return cnt && $.map(sel, function(f) { return f.phash && f.read ? f : null  }).length == cnt ? 0 : -1;
		return !this._disabled && cnt && $.map(sel, function(f) { return f.phash && f.read ? f : null  }).length == cnt ? 0 : -1;
	}
	
	this.exec = function(hashes) {
		var fm   = this.fm,
			dfrd = $.Deferred()
				.fail(function(error) {
					fm.error(error);
				});

		$.each(this.files(hashes), function(i, file) {
			if (!(file.read && file.phash)) {
				return !dfrd.reject(['errCopy', file.name, 'errPerm']);
			}
		});
		
		return dfrd.state() == 'rejected' ? dfrd : dfrd.resolve(fm.clipboard(this.hashes(hashes)));
	}

}


/*
 * File: /home/osc/elFinder-build/elFinder/js/commands/upload.js
 */

/**
 * @class elFinder command "upload"
 * Upload files using iframe or XMLHttpRequest & FormData.
 * Dialog allow to send files using drag and drop
 *
 * @type  elFinder.command
 * @author  Dmitry (dio) Levashov
 */
elFinder.prototype.commands.upload = function() {
	var hover = this.fm.res('class', 'hover');
	
	this.disableOnSearch = true;
	this.updateOnSelect  = false;
	
	// Shortcut opens dialog
	this.shortcuts = [{
		pattern     : 'ctrl+u'
	}];
	
	/**
	 * Return command state
	 *
	 * @return Number
	 **/
	this.getstate = function() {
		return !this._disabled && this.fm.cwd().write ? 0 : -1;
	};
	
	
	this.exec = function(data) {
		var fm = this.fm,
			upload = function(data) {
				dialog.elfinderdialog('close');
				fm.upload(data)
					.fail(function(error) {
						dfrd.reject(error);
					})
					.done(function(data) {
						dfrd.resolve(data);
					});
			},
			dfrd, dialog, input, button, dropbox, pastebox;
		
		if (this.disabled()) {
			return $.Deferred().reject();
		}
		
		if (data && (data.input || data.files)) {
			return fm.upload(data);
		}
		
		dfrd = $.Deferred();
		
		
		input = $('<input type="file" multiple="true"/>')
			.change(function() {
				upload({input : input[0]});
			});

		button = $('<div class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"><span class="ui-button-text">'+fm.i18n('selectForUpload')+'</span></div>')
			.append($('<form/>').append(input))
			.hover(function() {
				button.toggleClass(hover)
			});
			
		dialog = $('<div class="elfinder-upload-dialog-wrapper"/>')
			.append(button);
		
		pastebox = $('<div class="ui-corner-all elfinder-upload-dropbox" contenteditable=true></div>')
			.focus(function() {
				if (this.innerHTML) {
					var src = this.innerHTML.replace(/<br[^>]*>/gi, ' ');
					var type = src.match(/<[^>]+>/)? 'html' : 'text';
					this.innerHTML = '';
					upload({files : [ src ], type : type});
				}
			})
			.bind('dragenter mouseover', function(){
				this.focus();
				$(pastebox).addClass(hover);
			})
			.bind('dragleave mouseout', function(){
				this.blur();
				$(pastebox).removeClass(hover);
			})
			.bind('mouseup keyup', function() {
				setTimeout(function(){
					$(pastebox).focus();
				}, 100);
			});
		
		if (fm.dragUpload) {
			dropbox = $('<div class="ui-corner-all elfinder-upload-dropbox">'+fm.i18n('dropFiles')+'</div>')
				.prependTo(dialog)
				.after('<div class="elfinder-upload-dialog-or">'+fm.i18n('or')+'</div>')
				.after(pastebox)
				.after('<div>'+fm.i18n('dropFilesBrowser')+'</div>')
				.after('<div class="elfinder-upload-dialog-or">'+fm.i18n('or')+'</div>')[0];
			
			dropbox.addEventListener('dragenter', function(e) {
				e.stopPropagation();
			  	e.preventDefault();
				$(dropbox).addClass(hover);
			}, false);

			dropbox.addEventListener('dragleave', function(e) {
				e.stopPropagation();
			  	e.preventDefault();
				$(dropbox).removeClass(hover);
			}, false);

			dropbox.addEventListener('dragover', function(e) {
				e.stopPropagation();
			  	e.preventDefault();
			  	$(dropbox).addClass(hover);
			}, false);

			dropbox.addEventListener('drop', function(e) {
				e.stopPropagation();
			  	e.preventDefault();
				var file = false;
				var type = '';
				if (e.dataTransfer && e.dataTransfer.items &&  e.dataTransfer.items.length) {
					file = e.dataTransfer.items;
					type = 'data';
				} else if (e.dataTransfer && e.dataTransfer.files &&  e.dataTransfer.files.length) {
					file = e.dataTransfer.files;
					type = 'files';
				} else if (e.dataTransfer.getData('text/html')) {
					file = [ e.dataTransfer.getData('text/html') ];
					type = 'html';
				} else if (e.dataTransfer.getData('text')) {
					file = [ e.dataTransfer.getData('text') ];
					type = 'text';
				}
				if (file) {
					upload({files : file, type : type});
				}
			}, false);
			
		} else {
			//删除dropFilesBrowser 2014-09-23
			/*$('<div>'+fm.i18n('dropFilesBrowser')+'</div>')
				.append(pastebox)
				.prependTo(dialog)
				.after('<div class="elfinder-upload-dialog-or">'+fm.i18n('or')+'</div>')[0];*/
			
		}
		
		fm.dialog(dialog, {
			title          : this.title,
			modal          : true,
			resizable      : false,
			destroyOnClose : true
		});
		
		return dfrd;
	};

};
