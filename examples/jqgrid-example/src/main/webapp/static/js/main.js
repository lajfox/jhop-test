requirejs.config({
    "baseUrl": "static/js/lib",
    "paths": {
      "jquery":"./jquery/jquery-1.8.3.min",	
      "jquery-ui":"./jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min",
      "jquery-ui-zh-CN":"./jquery-ui-1.9.2.custom/development-bundle/ui/i18n/jquery.ui.datepicker-zh-CN",
      "jquery-layout":"./jquery-layout-1.3/jquery.layout-latest.min",
      "jqgrid-cn":"./jqgrid/js/i18n/grid.locale-cn",
      "jqgrid-ui.multiselect":'./jqgrid/plugins/ui.multiselect',
      "jqqrid":'./jqgrid/js/jquery.jqGrid.min',
      "jqqrid-extend":'./jqgrid/jqGrid.extend',
      "jquery-ui-tabs-paging":"./jquery-ui-tabs-paging/ui.tabs.paging",
      "themeswitchertool":'./jquery-layout-1.3/themeswitchertool',
      "jquery-ui-tabs-extend":"./jquery-ui-1.9.2.custom/jquery.ui.tabs.extend",
      
      "jquery-ui-timepicker":"./timepicker/jquery-ui-timepicker-addon",
      "multiselect2side":"./jquery.multiselect2side/js/jquery.multiselect2side",
      "jquery-zTree":"./jquery.zTree/js/jquery.ztree.all-3.5.min",
      "jquery-form":"./jquery-form/jquery.form",
      "jquery-resize":"./jquery-resize/jquery.ba-resize.min",
      
      "blocku":"./blockui/jquery.blockUI",
      "jquery.validate":"./jquery-validation/1.9.0/jquery.validate.min",
      "jquery.validate.cn":"./jquery-validation/1.9.0/messages_bs_cn",
      "uniform":"./uniform/jquery.uniform.min",     
      
      "app": "../app"
    },
    "shim": {
        "jquery-ui": ["jquery"],
        "jquery-ui-zh-CN": ["jquery-ui"],
        "jquery-layout":["jquery"],
        "jqgrid-cn":["jquery"],
        "jqgrid-ui.multiselect":["jquery-ui"],
        "jqqrid":["jquery-ui"],
        "jqqrid-extend":["jqqrid"],
        "jquery-ui-tabs-paging":["jquery-ui"],
        "themeswitchertool":["jquery"],
        "jquery-ui-tabs-extend":["jquery-ui"],
        
        "jquery-ui-timepicker":["jquery-ui"],
        "multiselect2side":["jquery"],
        "jquery-zTree":["jquery"],
        "jquery-form":["jquery"],
        "jquery-resize":["jquery"],
        "blocku":["jquery"],
        "jquery.validate":["jquery"],
        "jquery.validate.cn":["jquery.validate"],
        "uniform":["jquery"]
    }
});

// Load the main app module to start the app
requirejs(["app/index"]);