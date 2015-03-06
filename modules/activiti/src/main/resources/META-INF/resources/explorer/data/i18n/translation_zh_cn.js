/**
 * @author willi.tscheschner
 * 
 * contains all strings for default language (en_us)
 * 
 */



// namespace
if(window.Signavio == undefined) Signavio = {};
if(window.Signavio.I18N == undefined) Signavio.I18N = {};

Signavio.I18N.Language = "zh_cn"; //Pattern <ISO language code>_<ISO country code> in lower case!

Signavio.I18N.askForRefresh = "The content of the current folder might have changed.<br/>Do you want to refresh the view?";

// DEFINE THE DEFAULT VALUES FOR THE EXT MSG-BOX
Ext.MessageBox.buttonText.yes = "是";
Ext.MessageBox.buttonText.no = "否";
Ext.MessageBox.buttonText.cancel = "取消";
Ext.MessageBox.buttonText.ok = "确定";


// REPOSITORY
if(!Signavio.I18N.Repository) Signavio.I18N.Repository = {};
Signavio.I18N.Repository.leftPanelTitle = "";
Signavio.I18N.Repository.rightPanelTitle = "";


// DATE
if(!Signavio.I18N.Repository.Date) Signavio.I18N.Repository.Date = {};
Signavio.I18N.Repository.Date.ago = "ago";
// SINGULAR
Signavio.I18N.Repository.Date.year = "年";
Signavio.I18N.Repository.Date.month = "月";
Signavio.I18N.Repository.Date.day = "日";
Signavio.I18N.Repository.Date.hour = "小时";
Signavio.I18N.Repository.Date.minute = "分钟";
Signavio.I18N.Repository.Date.second = "秒";
// PLURAL
Signavio.I18N.Repository.Date.years = "years";
Signavio.I18N.Repository.Date.months = "months";
Signavio.I18N.Repository.Date.days = "days";
Signavio.I18N.Repository.Date.hours = "hours";
Signavio.I18N.Repository.Date.minutes = "minutes";
Signavio.I18N.Repository.Date.seconds = "seconds";


// LOGGING TOOL
if(!Signavio.I18N.Repository.Log) Signavio.I18N.Repository.Log = {};
Signavio.I18N.Repository.Log.ERROR = "ERROR";
Signavio.I18N.Repository.Log.CRITICAL = "CRITICAL";
Signavio.I18N.Repository.Log.FATAL = "FATAL";
Signavio.I18N.Repository.Log.LOG = "Log";

Signavio.I18N.Repository.loadingFailed = "The application could not be loaded. Please double check your internet connection and reload the page.";

Signavio.I18N.Repository.noFeaturesTitle = "No functions";
Signavio.I18N.Repository.noFeaturesMsg = "There are no functions available at the moment.";


// HEADER TEMPLATE
if(!Signavio.I18N.Repository.Header) Signavio.I18N.Repository.Header = {};
Signavio.I18N.Repository.Header.openIdSample = "your.openid.net";
Signavio.I18N.Repository.Header.sayHello = "你好";
Signavio.I18N.Repository.Header.login = "登录";
Signavio.I18N.Repository.Header.logout = "登出";
Signavio.I18N.Repository.Header.loginTitle = "登录";
Signavio.I18N.Repository.Header.loginError401 = "用户名或密码错误!";
Signavio.I18N.Repository.Header.loginError404 = "用户己失效.";
Signavio.I18N.Repository.Header.loginError500 = "服务器内部错误.";

Signavio.I18N.Repository.Header.windowBtnLogin = "登录";
Signavio.I18N.Repository.Header.windowBtnCancel = "取消";
Signavio.I18N.Repository.Header.windowBtnResetPassword = "重置密码";
Signavio.I18N.Repository.Header.windowFieldName = "电邮";
Signavio.I18N.Repository.Header.windowFieldUserName = "用户名";
Signavio.I18N.Repository.Header.windowFieldPassword = "密码";
Signavio.I18N.Repository.Header.windowFieldRemember = "记住我";
Signavio.I18N.Repository.Header.windowResetPasswordDesc = "请输入邮箱地址接收密码重置方法.";
Signavio.I18N.Repository.Header.windowBtnSend = "发送";
Signavio.I18N.Repository.Header.windowResetPasswordHintOk = "重置密码邮件己发送到你的邮箱中，请查收.";
Signavio.I18N.Repository.Header.windowResetPasswordHintFail = "重置密码邮件发送失败，请重试或联系技术支持.";

// CONTEXT PLUGIN
if(!Signavio.I18N.Repository.ContextPlugin) Signavio.I18N.Repository.ContextPlugin = {};
Signavio.I18N.Repository.ContextPlugin.selectedElements = "Selected elements";


// FOLDER PLUGIN
if(!Signavio.I18N.Repository.Folder) Signavio.I18N.Repository.Folder = {};
Signavio.I18N.Repository.Folder.folder = "&raquo; 文件件";
Signavio.I18N.Repository.Folder.favorits = "&raquo; 收藏";
Signavio.I18N.Repository.Folder.savedSearch = "&raquo; 保存查询";

Signavio.I18N.Repository.Folder["public"] = "工作空间";

// INFO PLUGIN
if(!Signavio.I18N.Repository.Info) Signavio.I18N.Repository.Info = {};
Signavio.I18N.Repository.Info.noTitle = "No title";
Signavio.I18N.Repository.Info.noDescription = "No summary";
Signavio.I18N.Repository.Info.noSelection = "No elements are selected";
Signavio.I18N.Repository.Info.willBeNotified = "In case of changes, you will get an email notification.";
Signavio.I18N.Repository.Info.wontBeNotified = "";
Signavio.I18N.Repository.Info.notifyMe = "Notify me via Email, if the diagram changes.";
Signavio.I18N.Repository.Info.dontNotifyMe = "Don't notify me.";
Signavio.I18N.Repository.Info.elementSelected = "elements selected";
// DEFINE ATTRIBUTES
Signavio.I18N.Repository.Info.Attributes = {};
Signavio.I18N.Repository.Info.Attributes.name = "名称";
Signavio.I18N.Repository.Info.Attributes.description = "描述";
Signavio.I18N.Repository.Info.Attributes.noname = "No name";
Signavio.I18N.Repository.Info.Attributes.nodescription = "No description";
Signavio.I18N.Repository.Info.Attributes.author = "作者";
Signavio.I18N.Repository.Info.Attributes.created = "创建";
Signavio.I18N.Repository.Info.Attributes.updated = "更新";
Signavio.I18N.Repository.Info.Attributes.info = "edited <b>#{time}</b> ago #{delimiter}by <b>#{user}</b>";
Signavio.I18N.Repository.Info.Attributes.infoMulipleOne = "last edited <b>#{time}</b> ago";
Signavio.I18N.Repository.Info.Attributes.infoMulipleTwo= "last edited between <b>#{time}</b> and <b>#{time2}</b>";

// BREADCRUMP PLUGIN
if(!Signavio.I18N.Repository.BreadCrumb) Signavio.I18N.Repository.BreadCrumb = {};
Signavio.I18N.Repository.BreadCrumb.delimiter = "&raquo; ";
Signavio.I18N.Repository.BreadCrumb.search = "查询: ";
Signavio.I18N.Repository.BreadCrumb.nrOfResults = "found in {nr} objects"; 
Signavio.I18N.Repository.BreadCrumb.none = "没有选择目录";
Signavio.I18N.Repository.BreadCrumb.goBack = "返回";

// VIEW TEMPLATE
if(!Signavio.I18N.Repository.View) Signavio.I18N.Repository.View = {};
Signavio.I18N.Repository.View.foundInNames = "Found in titles";
Signavio.I18N.Repository.View.foundInDescriptions = "Found in descriptions";
Signavio.I18N.Repository.View.foundInLabels = "Found in diagram elements";
Signavio.I18N.Repository.View.foundInComments = "Found in comments";
Signavio.I18N.Repository.View.foundInRevComments = "Found in revision comments";
Signavio.I18N.Repository.View.foundInMetaData = "Found in custom attributes";

// ICONVIEW PLUGIN
if(!Signavio.I18N.Repository.IconView) Signavio.I18N.Repository.IconView = {};
Signavio.I18N.Repository.IconView.none = "No elements";
Signavio.I18N.Repository.IconView.description = "Icon view";

if(!Signavio.I18N.Repository.TableView) Signavio.I18N.Repository.TableView = {};
Signavio.I18N.Repository.TableView.foundIn = "Found in";
Signavio.I18N.Repository.TableView.name = "名称";
Signavio.I18N.Repository.TableView.description = "描述";
Signavio.I18N.Repository.TableView.revision = "修正版";
Signavio.I18N.Repository.TableView.lastChanges = "最后更新";
Signavio.I18N.Repository.TableView.lastAuthor = "最后修改人";
Signavio.I18N.Repository.TableView.toolTip = "List view";

// ALL OFFERS
if(!Signavio.I18N.Repository.Offer) Signavio.I18N.Repository.Offer = {};

// DELETE
Signavio.I18N.Repository.Offer.deleteTitle = "删除";
Signavio.I18N.Repository.Offer.deleteDescription = "Move the selected elements to the trash";
Signavio.I18N.Repository.Offer.removeTitle = "删除";
Signavio.I18N.Repository.Offer.removeDescription = "Remove the selected elements. This action is irreversible.";
Signavio.I18N.Repository.Offer.restoreTitle = "恢复";
Signavio.I18N.Repository.Offer.restoreDescription = "Restore the elements from the trash";
Signavio.I18N.Repository.Offer.deleteQuestion = "Are you sure you want to move the diagram(s) to the trash?";
Signavio.I18N.Repository.Offer.removeQuestion = "Do you really want to permanently delete the selecte diagram(s) from the trash? This action is irreversible.";
// MOVE
Signavio.I18N.Repository.Offer.copyPrefix = " (复制)";
Signavio.I18N.Repository.Offer.moveTitle = "移动";
Signavio.I18N.Repository.Offer.moveDescription = "移动己选择的元素到指定文件夹";
Signavio.I18N.Repository.Offer.moveCreateCopy = "Create a copy";
Signavio.I18N.Repository.Offer.moveCreateCopyNot = "Only available if all elements are diagrams.";
Signavio.I18N.Repository.Offer.moveWindowHeader = "移动 '#{title}' 到:";
Signavio.I18N.Repository.Offer.moveWindowHeaderMultiple = "复制所有选择的#{count}个元素到:";
Signavio.I18N.Repository.Offer.moveBtnOk = "移动";
Signavio.I18N.Repository.Offer.moveBtnCancel = "取消";
Signavio.I18N.Repository.Offer.moveAlertTitle = "移动";
Signavio.I18N.Repository.Offer.moveAlertDesc = "你对选定的目标文件夹没有可写权限.";


Signavio.I18N.Repository.Offer.copyTitle = "复制";
Signavio.I18N.Repository.Offer.copyDescription = "复制己选择的元素到指定文件夹";
Signavio.I18N.Repository.Offer.copyWindowHeader = "复制 '#{title}' 到:";
Signavio.I18N.Repository.Offer.copyWindowHeaderMultiple = "复制所有选择的#{count}个图形到:";
Signavio.I18N.Repository.Offer.copyBtnOk = "复制";
Signavio.I18N.Repository.Offer.copyAlertTitle = "复制";
Signavio.I18N.Repository.Offer.copyAlertDesc = "你对选定的目标文件夹没有可写权限.";


// NEW
Signavio.I18N.Repository.Offer.newTitle = "新建";
Signavio.I18N.Repository.Offer.newFolderTitle = "文件夹";
Signavio.I18N.Repository.Offer.newFolderDescription = "新建文件夹";
Signavio.I18N.Repository.Offer.newFolderDefaultTitle = "新建文件夹";
Signavio.I18N.Repository.Offer.newFFOnly = "流程编辑器不支持你使用web浏览器. 更多关于浏览器兼容性信息，请点击 <a target='_blank' href='http://www.signavio.com/en/browser-compatibility.html' >这里</a>."
Signavio.I18N.Repository.Offer.newWindowFolderTitle = "新建文件夹";
Signavio.I18N.Repository.Offer.newWindowFolderDesc = "请给新建文件夹输入一个名称:";

// Edit
Signavio.I18N.Repository.Offer.editGroupTitle = "编辑";
Signavio.I18N.Repository.Offer.edit = "编辑流程图";
Signavio.I18N.Repository.Offer.editDescription = "编辑当前流程图";

// SEARCH
Signavio.I18N.Repository.Offer.search = "查询";
// UPDATE
Signavio.I18N.Repository.Offer.updateTitle = "刷新";
Signavio.I18N.Repository.Offer.updateDescription = "更新整个仓库";

// HOVERBUTTON
if(!Signavio.I18N.Repository.HoverButton) Signavio.I18N.Repository.HoverButton = {};
Signavio.I18N.Repository.HoverButton.deleteString = "删除";

if(!Signavio.I18N.Repository.FormStorePanel) Signavio.I18N.Repository.FormStorePanel = {};
Signavio.I18N.Repository.FormStorePanel.commit = "保存";
Signavio.I18N.Repository.FormStorePanel.reject = "放弃";
Signavio.I18N.Repository.FormStorePanel.language = "Requires a <a href='/p/explorer'>refresh</a> of the page.";

if(!Signavio.I18N.Repository.Hint) Signavio.I18N.Repository.Hint = {};
Signavio.I18N.Repository.Hint.supportedBrowserEditor = "目前, 模型还不支持你的浏览器 (请查看 <a href='http://www.signavio.com/en/browser-compatibility.html' target='_blank'>浏览器兼容性</a>).<br/>请使用 Firefox 3.5+."

if(!Signavio.I18N.Repository.SearchFields) Signavio.I18N.Repository.SearchFields = {};
Signavio.I18N.Repository.SearchFields.labels = "属性";
Signavio.I18N.Repository.SearchFields.text1 = "修改意见";
Signavio.I18N.Repository.SearchFields.text2 = "属性";
Signavio.I18N.Repository.SearchFields.comments = "注解";
Signavio.I18N.Repository.SearchFields.description = "描述";
Signavio.I18N.Repository.SearchFields.name = "名称";
