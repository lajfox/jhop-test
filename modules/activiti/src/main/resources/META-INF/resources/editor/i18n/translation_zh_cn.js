/**
 * @author nicolas.peters
 * 
 * Contains all strings for the default language (en-us).
 * Version 1 - 08/29/08
 */
if(!ORYX) var ORYX = {};

if(!ORYX.I18N) ORYX.I18N = {};

ORYX.I18N.Language = "zh_cn"; //Pattern <ISO language code>_<ISO country code> in lower case!

if(!ORYX.I18N.Oryx) ORYX.I18N.Oryx = {};

ORYX.I18N.Oryx.title		= "Oryx";
ORYX.I18N.Oryx.noBackendDefined	= "警告! \n没有后端定义.\n 请求的模型不能被加载。请试加载保存插件配置.";
ORYX.I18N.Oryx.pleaseWait 	= "请稍等，数据加载中...";
ORYX.I18N.Oryx.notLoggedOn = "没有登录";
ORYX.I18N.Oryx.editorOpenTimeout = "编辑器没有启动，请检查，你是否启用弹出式窗口拦截器和或允许这个站点弹出窗口. 我们在这个网站永远不会显示任何广告.";

if(!ORYX.I18N.AddDocker) ORYX.I18N.AddDocker = {};

ORYX.I18N.AddDocker.group = "Docker";
ORYX.I18N.AddDocker.add = "增加 Docker";
ORYX.I18N.AddDocker.addDesc = "Add a Docker to an edge, by clicking on it";
ORYX.I18N.AddDocker.del = "删除 Docker";
ORYX.I18N.AddDocker.delDesc = "删除一个 Docker";

if(!ORYX.I18N.Arrangement) ORYX.I18N.Arrangement = {};

ORYX.I18N.Arrangement.groupZ = "Z轴次序";
ORYX.I18N.Arrangement.btf = "置前";
ORYX.I18N.Arrangement.btfDesc = "置前";
ORYX.I18N.Arrangement.btb = "置后";
ORYX.I18N.Arrangement.btbDesc = "置后";
ORYX.I18N.Arrangement.bf = "上移一层";
ORYX.I18N.Arrangement.bfDesc = "将当前层向上移一层";
ORYX.I18N.Arrangement.bb = "下移一层";
ORYX.I18N.Arrangement.bbDesc = "将当前层向下移一层";
ORYX.I18N.Arrangement.groupA = "对齐";
ORYX.I18N.Arrangement.ab = "底部对齐";
ORYX.I18N.Arrangement.abDesc = "底部";
ORYX.I18N.Arrangement.am = "垂直对齐";
ORYX.I18N.Arrangement.amDesc = "垂直";
ORYX.I18N.Arrangement.at = "顶部对齐";
ORYX.I18N.Arrangement.atDesc = "顶部";
ORYX.I18N.Arrangement.al = "左对齐";
ORYX.I18N.Arrangement.alDesc = "左";
ORYX.I18N.Arrangement.ac = "居中";
ORYX.I18N.Arrangement.acDesc = "居中";
ORYX.I18N.Arrangement.ar = "右对齐";
ORYX.I18N.Arrangement.arDesc = "右";
ORYX.I18N.Arrangement.as = "同样大小对齐";
ORYX.I18N.Arrangement.asDesc = "同样大小";

if(!ORYX.I18N.Edit) ORYX.I18N.Edit = {};

ORYX.I18N.Edit.group = "编辑";
ORYX.I18N.Edit.cut = "剪切";
ORYX.I18N.Edit.cutDesc = "将选择的剪切到剪切板";
ORYX.I18N.Edit.copy = "拷贝";
ORYX.I18N.Edit.copyDesc = "从剪切板中拷贝";
ORYX.I18N.Edit.paste = "粘贴";
ORYX.I18N.Edit.pasteDesc = "将剪切板粘贴到画板";
ORYX.I18N.Edit.del = "删除";
ORYX.I18N.Edit.delDesc = "删除所有选定的图形";

if(!ORYX.I18N.EPCSupport) ORYX.I18N.EPCSupport = {};

ORYX.I18N.EPCSupport.group = "EPC";
ORYX.I18N.EPCSupport.exp = "导出 EPC";
ORYX.I18N.EPCSupport.expDesc = "导出 EPML";
ORYX.I18N.EPCSupport.imp = "导入 EPC";
ORYX.I18N.EPCSupport.impDesc = "导入 EPML 文件";
ORYX.I18N.EPCSupport.progressExp = "导出模型中";
ORYX.I18N.EPCSupport.selectFile = "选择一个 EPML (.empl) 文件导入.";
ORYX.I18N.EPCSupport.file = "文件";
ORYX.I18N.EPCSupport.impPanel = "导入 EPML 文件";
ORYX.I18N.EPCSupport.impBtn = "导入";
ORYX.I18N.EPCSupport.close = "关闭";
ORYX.I18N.EPCSupport.error = "错误";
ORYX.I18N.EPCSupport.progressImp = "导入中...";

if(!ORYX.I18N.ERDFSupport) ORYX.I18N.ERDFSupport = {};

ORYX.I18N.ERDFSupport.exp = "导出 ERDF";
ORYX.I18N.ERDFSupport.expDesc = "导出 ERDF";
ORYX.I18N.ERDFSupport.imp = "导入 ERDF";
ORYX.I18N.ERDFSupport.impDesc = "导入 ERDF 文件";
ORYX.I18N.ERDFSupport.impFailed = "导入 ERDF 文件失败.";
ORYX.I18N.ERDFSupport.impFailed2 = "导入错误! <br/>请以下提示信息: <br/><br/>";
ORYX.I18N.ERDFSupport.error = "错误";
ORYX.I18N.ERDFSupport.noCanvas = "xml 文档没有包含 Oryx 画板节点!";
ORYX.I18N.ERDFSupport.noSS = "Oryx 画板没有包含节点没有模板集定义!";
ORYX.I18N.ERDFSupport.wrongSS = "给定的模板集不适合当前的编辑器!";
ORYX.I18N.ERDFSupport.selectFile = "请选择一个 ERDF (.xml) 文件导入!";
ORYX.I18N.ERDFSupport.file = "文件";
ORYX.I18N.ERDFSupport.impERDF = "导入 ERDF";
ORYX.I18N.ERDFSupport.impBtn = "导入";
ORYX.I18N.ERDFSupport.impProgress = "导入中...";
ORYX.I18N.ERDFSupport.close = "关闭";
ORYX.I18N.ERDFSupport.deprTitle = "确认导出 eRDF文件?";
ORYX.I18N.ERDFSupport.deprText = "不推荐导出为eRDF,因为在未来版本的Signavio流程编辑器中将停止支持导出为eRDF。如果可以的话,请导出为JSON格式模型。你确定导出?";

if(!ORYX.I18N.jPDLSupport) ORYX.I18N.jPDLSupport = {};

ORYX.I18N.jPDLSupport.group = "ExecBPMN";
ORYX.I18N.jPDLSupport.exp = "导出 jPDL";
ORYX.I18N.jPDLSupport.expDesc = "导出 jPDL";
ORYX.I18N.jPDLSupport.imp = " 导入 jPDL";
ORYX.I18N.jPDLSupport.impDesc = "导入 jPDL 文件";
ORYX.I18N.jPDLSupport.impFailedReq = "导入 jPDL 文件失败.";
ORYX.I18N.jPDLSupport.impFailedJson = "转换 jPDL 文件失败.";
ORYX.I18N.jPDLSupport.impFailedJsonAbort = "终上导入.";
ORYX.I18N.jPDLSupport.loadSseQuestionTitle = "jBPM模板设置扩展需要加载"; 
ORYX.I18N.jPDLSupport.loadSseQuestionBody = "为了导入 jPDL, 模板设置扩展已经被装载. 是否要继续?";
ORYX.I18N.jPDLSupport.expFailedReq = "导出模型请求失败.";
ORYX.I18N.jPDLSupport.expFailedXml = "导出 jPDL 失败. 导出日志: ";
ORYX.I18N.jPDLSupport.error = "错误";
ORYX.I18N.jPDLSupport.selectFile = "请选择一个 jPDL (.xml) 文件导入!";
ORYX.I18N.jPDLSupport.file = "文件";
ORYX.I18N.jPDLSupport.impJPDL = "导入 jPDL";
ORYX.I18N.jPDLSupport.impBtn = "导入";
ORYX.I18N.jPDLSupport.impProgress = "导入中...";
ORYX.I18N.jPDLSupport.close = "关闭";

if(!ORYX.I18N.Save) ORYX.I18N.Save = {};

ORYX.I18N.Save.group = "文件";
ORYX.I18N.Save.save = "保存";
ORYX.I18N.Save.saveDesc = "保存";
ORYX.I18N.Save.saveAs = "另保存为...";
ORYX.I18N.Save.saveAsDesc = "另保存为...";
ORYX.I18N.Save.unsavedData = "数据没有保存，请你离开前保存，否则你所做的修改将丢失。!";
ORYX.I18N.Save.newProcess = "新建流程";
ORYX.I18N.Save.saveAsTitle = "另保存为...";
ORYX.I18N.Save.saveBtn = "保存";
ORYX.I18N.Save.close = "关闭";
ORYX.I18N.Save.savedAs = "保存为";
ORYX.I18N.Save.saved = "保存成功!";
ORYX.I18N.Save.failed = "保存失败.";
ORYX.I18N.Save.noRights = "你没有权限保存.";
ORYX.I18N.Save.saving = "保存中";
ORYX.I18N.Save.saveAsHint = "流程图存储在:";

if(!ORYX.I18N.File) ORYX.I18N.File = {};

ORYX.I18N.File.group = "文件";
ORYX.I18N.File.print = "打印";
ORYX.I18N.File.printDesc = "打印当前模型";
ORYX.I18N.File.pdf = "导出为 PDF";
ORYX.I18N.File.pdfDesc = "导出为 PDF";
ORYX.I18N.File.info = "提示";
ORYX.I18N.File.infoDesc = "提示";
ORYX.I18N.File.genPDF = "生成 PDF";
ORYX.I18N.File.genPDFFailed = "生成 PDF 失败.";
ORYX.I18N.File.printTitle = "打印";
ORYX.I18N.File.printMsg = "打印功能目前还存在一些问题。我们建议使用PDF导出打印图。你是否继续印刷?";

if(!ORYX.I18N.Grouping) ORYX.I18N.Grouping = {};

ORYX.I18N.Grouping.grouping = "组合";
ORYX.I18N.Grouping.group = "组合";
ORYX.I18N.Grouping.groupDesc = "组合所有选定形状";
ORYX.I18N.Grouping.ungroup = "取消组全";
ORYX.I18N.Grouping.ungroupDesc = "删除所有选定的组的形状";

if(!ORYX.I18N.Loading) ORYX.I18N.Loading = {};

ORYX.I18N.Loading.waiting ="请稍等...";

if(!ORYX.I18N.PropertyWindow) ORYX.I18N.PropertyWindow = {};

ORYX.I18N.PropertyWindow.name = "名称";
ORYX.I18N.PropertyWindow.value = "值";
ORYX.I18N.PropertyWindow.selected = "选择";
ORYX.I18N.PropertyWindow.clickIcon = "点击图标";
ORYX.I18N.PropertyWindow.add = "新增";
ORYX.I18N.PropertyWindow.rem = "删除";
ORYX.I18N.PropertyWindow.complex = "编辑一个复杂类型";
ORYX.I18N.PropertyWindow.text = "编辑一个文本类型";
ORYX.I18N.PropertyWindow.ok = "确定";
ORYX.I18N.PropertyWindow.cancel = "取消";
ORYX.I18N.PropertyWindow.dateFormat = "m/d/y";

if(!ORYX.I18N.ShapeMenuPlugin) ORYX.I18N.ShapeMenuPlugin = {};

ORYX.I18N.ShapeMenuPlugin.drag = "拖曳";
ORYX.I18N.ShapeMenuPlugin.clickDrag = "点击或拖曳";
ORYX.I18N.ShapeMenuPlugin.morphMsg = "形状变形";

if(!ORYX.I18N.SyntaxChecker) ORYX.I18N.SyntaxChecker = {};

ORYX.I18N.SyntaxChecker.group = "验证";
ORYX.I18N.SyntaxChecker.name = "语法检查";
ORYX.I18N.SyntaxChecker.desc = "语法检查";
ORYX.I18N.SyntaxChecker.noErrors = "没有语法错误.";
ORYX.I18N.SyntaxChecker.invalid = "服务器内部错误.";
ORYX.I18N.SyntaxChecker.checkingMessage = "检查中 ...";

if(!ORYX.I18N.Deployer) ORYX.I18N.Deployer = {};

ORYX.I18N.Deployer.group = "发布";
ORYX.I18N.Deployer.name = "发布";
ORYX.I18N.Deployer.desc = "发布到流程引擎";

if(!ORYX.I18N.Undo) ORYX.I18N.Undo = {};

ORYX.I18N.Undo.group = "撤消";
ORYX.I18N.Undo.undo = "撤消";
ORYX.I18N.Undo.undoDesc = "撤消最后操作";
ORYX.I18N.Undo.redo = "重做";
ORYX.I18N.Undo.redoDesc = "重做最后操作";

if(!ORYX.I18N.View) ORYX.I18N.View = {};

ORYX.I18N.View.group = "缩放";
ORYX.I18N.View.zoomIn = "放大";
ORYX.I18N.View.zoomInDesc = "放大模型";
ORYX.I18N.View.zoomOut = "缩小";
ORYX.I18N.View.zoomOutDesc = "缩小模型";
ORYX.I18N.View.zoomStandard = "缩放到标准大小";
ORYX.I18N.View.zoomStandardDesc = "缩放到标准大小水平";
ORYX.I18N.View.zoomFitToModel = "模型最佳大小";
ORYX.I18N.View.zoomFitToModelDesc = "缩放至模型最佳大小";

/** New Language Properties: 08.12.2008 */

ORYX.I18N.PropertyWindow.title = "属性面板";

if(!ORYX.I18N.ShapeRepository) ORYX.I18N.ShapeRepository = {};
ORYX.I18N.ShapeRepository.title = "图形仓库面板";

ORYX.I18N.Save.dialogDesciption = "请输入名称、描述、注解.";
ORYX.I18N.Save.dialogLabelTitle = "标题";
ORYX.I18N.Save.dialogLabelDesc = "描述";
ORYX.I18N.Save.dialogLabelType = "类型";
ORYX.I18N.Save.dialogLabelComment = "注解";

Ext.MessageBox.buttonText.yes = "是";
Ext.MessageBox.buttonText.no = "否";
Ext.MessageBox.buttonText.cancel = "取消";
Ext.MessageBox.buttonText.ok = "确定";

if(!ORYX.I18N.Perspective) ORYX.I18N.Perspective = {};
ORYX.I18N.Perspective.no = "没有视角"
ORYX.I18N.Perspective.noTip = "卸载当前透视图"

/** New Language Properties: 21.04.2009 */
ORYX.I18N.JSONSupport = {
    imp: {
        name: "从 JSON 导入",
        desc: "从 JSON 导入模型",
        group: "导出",
        selectFile: "请选择一个 JSON (.json) 文件导入!",
        file: "文件",
        btnImp: "导入",
        btnClose: "关闭",
        progress: "导入中 ...",
        syntaxError: "语法错误"
    },
    exp: {
        name: "导出为 JSON",
        desc: "导出当前模型为 JSON",
        group: "导出"
    }
};

/** New Language Properties: 09.05.2009 */
if(!ORYX.I18N.JSONImport) ORYX.I18N.JSONImport = {};

ORYX.I18N.JSONImport.title = "JSON 导入";
ORYX.I18N.JSONImport.wrongSS = "模板组导入的文件({ 0 })不匹配己加载模板集({ 1 })."

/** New Language Properties: 14.05.2009 */
if(!ORYX.I18N.RDFExport) ORYX.I18N.RDFExport = {};
ORYX.I18N.RDFExport.group = "导出";
ORYX.I18N.RDFExport.rdfExport = "导出 RDF";
ORYX.I18N.RDFExport.rdfExportDescription = "导出当前模型为RDF";

/** New Language Properties: 15.05.2009*/
if(!ORYX.I18N.SyntaxChecker.BPMN) ORYX.I18N.SyntaxChecker.BPMN={};
ORYX.I18N.SyntaxChecker.BPMN_NO_SOURCE = "图形边框必须有一个源.";
ORYX.I18N.SyntaxChecker.BPMN_NO_TARGET = "图形边框必须有一个目标.";
ORYX.I18N.SyntaxChecker.BPMN_DIFFERENT_PROCESS = "源和目标节点必须在相同的流程中.";
ORYX.I18N.SyntaxChecker.BPMN_SAME_PROCESS = "源和目标节点必须包含在不同的泳道中.";
ORYX.I18N.SyntaxChecker.BPMN_FLOWOBJECT_NOT_CONTAINED_IN_PROCESS = "一个顺序流对象必须包含在一个流程中.";
ORYX.I18N.SyntaxChecker.BPMN_ENDEVENT_WITHOUT_INCOMING_CONTROL_FLOW = "结束事件必须有一个传入的顺序流.";
ORYX.I18N.SyntaxChecker.BPMN_STARTEVENT_WITHOUT_OUTGOING_CONTROL_FLOW = "一个开始的事件必须有一个流出顺序流.";
ORYX.I18N.SyntaxChecker.BPMN_STARTEVENT_WITH_INCOMING_CONTROL_FLOW = "启动事件必须没有输入顺序流.";
ORYX.I18N.SyntaxChecker.BPMN_ATTACHEDINTERMEDIATEEVENT_WITH_INCOMING_CONTROL_FLOW = "附加中间事件必须没有输入顺序流.";
ORYX.I18N.SyntaxChecker.BPMN_ATTACHEDINTERMEDIATEEVENT_WITHOUT_OUTGOING_CONTROL_FLOW = "附加中间事件必须只有一个流出顺序流.";
ORYX.I18N.SyntaxChecker.BPMN_ENDEVENT_WITH_OUTGOING_CONTROL_FLOW = "结束事件必须没有流出顺序流.";
ORYX.I18N.SyntaxChecker.BPMN_EVENTBASEDGATEWAY_BADCONTINUATION = "基于事件的网关其后不能是网关或子流程.";
ORYX.I18N.SyntaxChecker.BPMN_NODE_NOT_ALLOWED = "不允许的节点类型.";

if(!ORYX.I18N.SyntaxChecker.IBPMN) ORYX.I18N.SyntaxChecker.IBPMN={};
ORYX.I18N.SyntaxChecker.IBPMN_NO_ROLE_SET = "交互必须有一个发送者与接收者角色组";
ORYX.I18N.SyntaxChecker.IBPMN_NO_INCOMING_SEQFLOW = "该节点必须有输入顺序流.";
ORYX.I18N.SyntaxChecker.IBPMN_NO_OUTGOING_SEQFLOW = "该节点必须有输出顺序流.";

if(!ORYX.I18N.SyntaxChecker.InteractionNet) ORYX.I18N.SyntaxChecker.InteractionNet={};
ORYX.I18N.SyntaxChecker.InteractionNet_SENDER_NOT_SET = "没有设置发送方";
ORYX.I18N.SyntaxChecker.InteractionNet_RECEIVER_NOT_SET = "没有设置接收方";
ORYX.I18N.SyntaxChecker.InteractionNet_MESSAGETYPE_NOT_SET = "没有设置消息类型";
ORYX.I18N.SyntaxChecker.InteractionNet_ROLE_NOT_SET = "没有设置角色";

if(!ORYX.I18N.SyntaxChecker.EPC) ORYX.I18N.SyntaxChecker.EPC={};
ORYX.I18N.SyntaxChecker.EPC_NO_SOURCE = "Each edge must have a source.";
ORYX.I18N.SyntaxChecker.EPC_NO_TARGET = "Each edge must have a target.";
ORYX.I18N.SyntaxChecker.EPC_NOT_CONNECTED = "Node must be connected with edges.";
ORYX.I18N.SyntaxChecker.EPC_NOT_CONNECTED_2 = "Node must be connected with more edges.";
ORYX.I18N.SyntaxChecker.EPC_TOO_MANY_EDGES = "Node has too many connected edges.";
ORYX.I18N.SyntaxChecker.EPC_NO_CORRECT_CONNECTOR = "Node is no correct connector.";
ORYX.I18N.SyntaxChecker.EPC_MANY_STARTS = "There must be only one start event.";
ORYX.I18N.SyntaxChecker.EPC_FUNCTION_AFTER_OR = "There must be no functions after a splitting OR/XOR.";
ORYX.I18N.SyntaxChecker.EPC_PI_AFTER_OR = "There must be no process interface after a splitting OR/XOR.";
ORYX.I18N.SyntaxChecker.EPC_FUNCTION_AFTER_FUNCTION =  "There must be no function after a function.";
ORYX.I18N.SyntaxChecker.EPC_EVENT_AFTER_EVENT =  "There must be no event after an event.";
ORYX.I18N.SyntaxChecker.EPC_PI_AFTER_FUNCTION =  "There must be no process interface after a function.";
ORYX.I18N.SyntaxChecker.EPC_FUNCTION_AFTER_PI =  "There must be no function after a process interface.";
ORYX.I18N.SyntaxChecker.EPC_SOURCE_EQUALS_TARGET = "Edge must connect two distinct nodes."

if(!ORYX.I18N.SyntaxChecker.PetriNet) ORYX.I18N.SyntaxChecker.PetriNet={};
ORYX.I18N.SyntaxChecker.PetriNet_NOT_BIPARTITE = "这个图不是由两部分构成的";
ORYX.I18N.SyntaxChecker.PetriNet_NO_LABEL = "标签没有设置一个标记的过渡";
ORYX.I18N.SyntaxChecker.PetriNet_NO_ID = "有一个节点没有id";
ORYX.I18N.SyntaxChecker.PetriNet_SAME_SOURCE_AND_TARGET = "两个顺序流关系有相同的源和目标";
ORYX.I18N.SyntaxChecker.PetriNet_NODE_NOT_SET = "一个节点没有设置一个顺序流关系";

/** New Language Properties: 02.06.2009*/
ORYX.I18N.Edge = "Edge";
ORYX.I18N.Node = "Node";

/** New Language Properties: 03.06.2009*/
ORYX.I18N.SyntaxChecker.notice = "将鼠标移向一个红十字会的图标查看错误消息.";

/** New Language Properties: 05.06.2009*/
if(!ORYX.I18N.RESIZE) ORYX.I18N.RESIZE = {};
ORYX.I18N.RESIZE.tipGrow = "增加画布大小:";
ORYX.I18N.RESIZE.tipShrink = "减小画布大小:";
ORYX.I18N.RESIZE.N = "上";
ORYX.I18N.RESIZE.W = "左";
ORYX.I18N.RESIZE.S ="右";
ORYX.I18N.RESIZE.E ="下";

/** New Language Properties: 15.07.2009*/
if(!ORYX.I18N.Layouting) ORYX.I18N.Layouting ={};
ORYX.I18N.Layouting.doing = "Layouting...";

/** New Language Properties: 18.08.2009*/
ORYX.I18N.SyntaxChecker.MULT_ERRORS = "更多错误";

/** New Language Properties: 08.09.2009*/
if(!ORYX.I18N.PropertyWindow) ORYX.I18N.PropertyWindow = {};
ORYX.I18N.PropertyWindow.oftenUsed = "常用";
ORYX.I18N.PropertyWindow.moreProps = "更多属性";

/** New Language Properties 01.10.2009 */
if(!ORYX.I18N.SyntaxChecker.BPMN2) ORYX.I18N.SyntaxChecker.BPMN2 = {};

ORYX.I18N.SyntaxChecker.BPMN2_DATA_INPUT_WITH_INCOMING_DATA_ASSOCIATION = "A Data Input must not have any incoming Data Associations.";
ORYX.I18N.SyntaxChecker.BPMN2_DATA_OUTPUT_WITH_OUTGOING_DATA_ASSOCIATION = "A Data Output must not have any outgoing Data Associations.";
ORYX.I18N.SyntaxChecker.BPMN2_EVENT_BASED_TARGET_WITH_TOO_MANY_INCOMING_SEQUENCE_FLOWS = "Targets of Event-based Gateways may only have one incoming Sequence Flow.";

/** New Language Properties 02.10.2009 */
ORYX.I18N.SyntaxChecker.BPMN2_EVENT_BASED_WITH_TOO_LESS_OUTGOING_SEQUENCE_FLOWS = "An Event-based Gateway must have two or more outgoing Sequence Flows.";
ORYX.I18N.SyntaxChecker.BPMN2_EVENT_BASED_EVENT_TARGET_CONTRADICTION = "If Message Intermediate Events are used in the configuration, then Receive Tasks must not be used and vice versa.";
ORYX.I18N.SyntaxChecker.BPMN2_EVENT_BASED_WRONG_TRIGGER = "Only the following Intermediate Event triggers are valid: Message, Signal, Timer, Conditional and Multiple.";
ORYX.I18N.SyntaxChecker.BPMN2_EVENT_BASED_WRONG_CONDITION_EXPRESSION = "The outgoing Sequence Flows of the Event Gateway must not have a condition expression.";
ORYX.I18N.SyntaxChecker.BPMN2_EVENT_BASED_NOT_INSTANTIATING = "The Gateway does not meet the conditions to instantiate the process. Please use a start event or an instantiating attribute for the gateway.";

/** New Language Properties 05.10.2009 */
ORYX.I18N.SyntaxChecker.BPMN2_GATEWAYDIRECTION_MIXED_FAILURE = "The Gateway must have both multiple incoming and outgoing Sequence Flows.";
ORYX.I18N.SyntaxChecker.BPMN2_GATEWAYDIRECTION_CONVERGING_FAILURE = "The Gateway must have multiple incoming but most NOT have multiple outgoing Sequence Flows.";
ORYX.I18N.SyntaxChecker.BPMN2_GATEWAYDIRECTION_DIVERGING_FAILURE = "The Gateway must NOT have multiple incoming but must have multiple outgoing Sequence Flows.";
ORYX.I18N.SyntaxChecker.BPMN2_GATEWAY_WITH_NO_OUTGOING_SEQUENCE_FLOW = "A Gateway must have a minimum of one outgoing Sequence Flow.";
ORYX.I18N.SyntaxChecker.BPMN2_RECEIVE_TASK_WITH_ATTACHED_EVENT = "Receive Tasks used in Event Gateway configurations must not have any attached Intermediate Events.";
ORYX.I18N.SyntaxChecker.BPMN2_EVENT_SUBPROCESS_BAD_CONNECTION = "An Event Subprocess must not have any incoming or outgoing Sequence Flow.";

/** New Language Properties 13.10.2009 */
ORYX.I18N.SyntaxChecker.BPMN_MESSAGE_FLOW_NOT_CONNECTED = "At least one side of the Message Flow has to be connected.";

/** New Language Properties 24.11.2009 */
ORYX.I18N.SyntaxChecker.BPMN2_TOO_MANY_INITIATING_MESSAGES = "A Choreography Activity may only have one initiating message.";
ORYX.I18N.SyntaxChecker.BPMN_MESSAGE_FLOW_NOT_ALLOWED = "A Message Flow is not allowed here.";

/** New Language Properties 27.11.2009 */
ORYX.I18N.SyntaxChecker.BPMN2_EVENT_BASED_WITH_TOO_LESS_INCOMING_SEQUENCE_FLOWS = "An Event-based Gateway that is not instantiating must have a minimum of one incoming Sequence Flow.";
ORYX.I18N.SyntaxChecker.BPMN2_TOO_FEW_INITIATING_PARTICIPANTS = "A Choreography Activity must have one initiating Participant (white).";
ORYX.I18N.SyntaxChecker.BPMN2_TOO_MANY_INITIATING_PARTICIPANTS = "A Choreography Acitivity must not have more than one initiating Participant (white)."

ORYX.I18N.SyntaxChecker.COMMUNICATION_AT_LEAST_TWO_PARTICIPANTS = "The communication must be connected to at least two participants.";
ORYX.I18N.SyntaxChecker.MESSAGEFLOW_START_MUST_BE_PARTICIPANT = "The message flow's source must be a participant.";
ORYX.I18N.SyntaxChecker.MESSAGEFLOW_END_MUST_BE_PARTICIPANT = "The message flow's target must be a participant.";
ORYX.I18N.SyntaxChecker.CONV_LINK_CANNOT_CONNECT_CONV_NODES = "The conversation link must connect a communication or sub conversation node with a participant.";
