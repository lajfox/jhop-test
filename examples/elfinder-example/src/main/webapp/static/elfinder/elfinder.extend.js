
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
			'kindMsVision'      : 'Microsoft Vision 文档'
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
