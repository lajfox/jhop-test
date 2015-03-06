ORYX.I18N.PropertyWindow.dateFormat = "d/m/y";

ORYX.I18N.View.East = "属性";
ORYX.I18N.View.West = "模型元素";

ORYX.I18N.Oryx.title	= "Signavio";
ORYX.I18N.Oryx.pleaseWait = "请稍候,正在加载Signavio流程编辑器...";
ORYX.I18N.Edit.cutDesc = "剪切到剪贴板";
ORYX.I18N.Edit.copyDesc = "拷贝到剪贴板";
ORYX.I18N.Edit.pasteDesc = "粘贴剪贴板到画布上";
ORYX.I18N.ERDFSupport.noCanvas = "xml文档没有包括画布节点!";
ORYX.I18N.ERDFSupport.noSS = "流程编辑器画布的节点没有包含模板集定义!";
ORYX.I18N.ERDFSupport.deprText = "不推荐导出为eRDF,因为在未来版本的Signavio流程编辑器中将停止支持导出为eRDF。如果可以的话,请导出为JSON格式模型。你确定导出?";
ORYX.I18N.Save.pleaseWait = "请等待<br/>正在保存中...";

ORYX.I18N.Save.saveAs = "另保存为...";
ORYX.I18N.Save.saveAsDesc = "另保存为...";
ORYX.I18N.Save.saveAsTitle = "另保存为...";
ORYX.I18N.Save.savedAs = "复制保存";
ORYX.I18N.Save.savedDescription = "流程图存储在";
ORYX.I18N.Save.notAuthorized = "你目前没有登录. 请 <a href='/p/login' target='_blank'>登录</a> 后，你才可以保存当前流程图."
ORYX.I18N.Save.transAborted = "保存请求时间太长。你可以使用更快的网络连接。如果你使用无线局域网,请检查您的无线局域网连接的信号强度.";
ORYX.I18N.Save.noRights = "你没有权限保存当前模型. 请在 <a href='/p/explorer' target='_blank'>Signavio Explorer</a>检查, 如果你具有读写目标目录的权利.";
ORYX.I18N.Save.comFailed = "与服务器通信失败. 请检查你的网络连接. 如果仍然存在问题, 请联系 Signavio Support via.";
ORYX.I18N.Save.failed = "保存流程图发生错误.请重试. 如果仍然存在问题, 请联系 Signavio Support via .";
ORYX.I18N.Save.exception = "保存流程图发生异常.请重试. 如果仍然存在问题, 请联系 Signavio Support via .";
ORYX.I18N.Save.retrieveData = "请等待,数据检索中.";

/** New Language Properties: 10.6.09*/
if(!ORYX.I18N.ShapeMenuPlugin) ORYX.I18N.ShapeMenuPlugin = {};
ORYX.I18N.ShapeMenuPlugin.morphMsg = "形状变换";
ORYX.I18N.ShapeMenuPlugin.morphWarningTitleMsg = "形状变换";
ORYX.I18N.ShapeMenuPlugin.morphWarningMsg = "子形状将不能包含在转换后的元素。< br / >你无论如何都想转换吗?";

if (!Signavio) { var Signavio = {}; }
if (!Signavio.I18N) { Signavio.I18N = {} }
if (!Signavio.I18N.Editor) { Signavio.I18N.Editor = {} }

if (!Signavio.I18N.Editor.Linking) { Signavio.I18N.Editor.Linking = {} }
Signavio.I18N.Editor.Linking.CreateDiagram = "创建一个新的图";
Signavio.I18N.Editor.Linking.UseDiagram = "使用己存在的图";
Signavio.I18N.Editor.Linking.UseLink = "使用web链接";
Signavio.I18N.Editor.Linking.Close = "关闭";
Signavio.I18N.Editor.Linking.Cancel = "取消";
Signavio.I18N.Editor.Linking.UseName = "采用图的名字";
Signavio.I18N.Editor.Linking.UseNameHint = "替换当前的建模元素的名称({类型})的图链接.";
Signavio.I18N.Editor.Linking.CreateTitle = "建立链接";
Signavio.I18N.Editor.Linking.AlertSelectModel = "你必须选择一个模型.";
Signavio.I18N.Editor.Linking.ButtonLink = "图链接";
Signavio.I18N.Editor.Linking.LinkNoAccess = "你没有权限访问这个图.";
Signavio.I18N.Editor.Linking.LinkUnavailable = "这个图是不可用.";
Signavio.I18N.Editor.Linking.RemoveLink = "删除链接";
Signavio.I18N.Editor.Linking.EditLink = "编辑链接";
Signavio.I18N.Editor.Linking.OpenLink = "打开";
Signavio.I18N.Editor.Linking.BrokenLink = "这个链接己不存在!";
Signavio.I18N.Editor.Linking.PreviewTitle = "预览";

if(!Signavio.I18N.Glossary_Support) { Signavio.I18N.Glossary_Support = {}; }
Signavio.I18N.Glossary_Support.renameEmpty = "没有字典记录";
Signavio.I18N.Glossary_Support.renameLoading = "搜索中...";

/** New Language Properties: 08.09.2009*/
if(!ORYX.I18N.PropertyWindow) ORYX.I18N.PropertyWindow = {};
ORYX.I18N.PropertyWindow.oftenUsed = "主要属性";
ORYX.I18N.PropertyWindow.moreProps = "更多属性";

ORYX.I18N.PropertyWindow.btnOpen = "打开";
ORYX.I18N.PropertyWindow.btnRemove = "删除";
ORYX.I18N.PropertyWindow.btnEdit = "编辑";
ORYX.I18N.PropertyWindow.btnUp = "上移";
ORYX.I18N.PropertyWindow.btnDown = "下移";
ORYX.I18N.PropertyWindow.createNew = "新建";

if(!ORYX.I18N.PropertyWindow) ORYX.I18N.PropertyWindow = {};
ORYX.I18N.PropertyWindow.oftenUsed = "主要属性";
ORYX.I18N.PropertyWindow.moreProps = "更多属性";
ORYX.I18N.PropertyWindow.characteristicNr = "成本及资源分析";
ORYX.I18N.PropertyWindow.meta = "自定义属性";

if(!ORYX.I18N.PropertyWindow.Category){ORYX.I18N.PropertyWindow.Category = {}}
ORYX.I18N.PropertyWindow.Category.popular = "主要属性";
ORYX.I18N.PropertyWindow.Category.characteristicnr = "成本及资源分析";
ORYX.I18N.PropertyWindow.Category.others = "更多属性";
ORYX.I18N.PropertyWindow.Category.meta = "自定义属性";

if(!ORYX.I18N.PropertyWindow.ListView) ORYX.I18N.PropertyWindow.ListView = {};
ORYX.I18N.PropertyWindow.ListView.title = "编辑: ";
ORYX.I18N.PropertyWindow.ListView.dataViewLabel = "记录已经存在.";
ORYX.I18N.PropertyWindow.ListView.dataViewEmptyText = "没有记录.";
ORYX.I18N.PropertyWindow.ListView.addEntryLabel = "新增记录";
ORYX.I18N.PropertyWindow.ListView.buttonAdd = "增加";
ORYX.I18N.PropertyWindow.ListView.save = "保存";
ORYX.I18N.PropertyWindow.ListView.cancel = "取消";

if(!Signavio.I18N.Buttons) Signavio.I18N.Buttons = {};
Signavio.I18N.Buttons.save		= "保存";
Signavio.I18N.Buttons.cancel 	= "取消";
Signavio.I18N.Buttons.remove	= "删除";

if(!Signavio.I18N.btn) {Signavio.I18N.btn = {};}
Signavio.I18N.btn.btnEdit = "编辑";
Signavio.I18N.btn.btnRemove = "删除";
Signavio.I18N.btn.moveUp = "上移";
Signavio.I18N.btn.moveDown = "下移";

if(!Signavio.I18N.field) {Signavio.I18N.field = {};}
Signavio.I18N.field.Url = "连接";
Signavio.I18N.field.UrlLabel = "标签";
