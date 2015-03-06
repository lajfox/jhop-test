package com.techstar.modules.activiti.engine.impl.bpmn.diagram;

import java.awt.Color;
import java.io.InputStream;
import java.util.List;

import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.Artifact;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowElementsContainer;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.bpmn.model.Lane;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.Pool;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;

/**
 * <p>
 * Activiti 流程跟踪任务节点和线高亮显示,能标记流程所走过节点和线为红色,
 * </p>
 * <p>
 * 当前处理节点为绿色，未走过的节点和线不标记，支持驳回和子流程。
 * </p>
 * 
 * @author sundoctor
 * @see org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator
 * 
 */
public class CustomProcessDiagramGenerator extends ProcessDiagramGenerator {

	/**
	 * Generates a PNG diagram image of the given process definition, using the
	 * diagram interchange information of the process.
	 * 
	 * @param bpmnModel
	 *            流程定义模型
	 * @param highLightedHisActivities
	 *            流程所走过节点(不包括当前处理节点)
	 * @param highLightedCurActivities
	 *            流程当前处理节点
	 * @param highLightedFlows
	 *            流程所走过线
	 * @return 流程跟踪任务节点和线高亮显示流程图
	 * @see CustomProcessDiagramGenerator#generateDiagram(BpmnModel , String , List ,
	 *      List , List)
	 */
	public static InputStream generatePngDiagram(BpmnModel bpmnModel, List<String> highLightedHisActivities,
			List<String> highLightedCurActivities, List<String> highLightedFlows) {
		return generateDiagram(bpmnModel, "png", highLightedHisActivities, highLightedCurActivities, highLightedFlows);
	}

	/**
	 * Generates a JPG diagram image of the given process definition, using the
	 * diagram interchange information of the process.
	 * 
	 * @param bpmnModel
	 *            流程定义模型
	 * @param highLightedHisActivities
	 *            流程所走过节点(不包括当前处理节点)
	 * @param highLightedCurActivities
	 *            流程当前处理节点
	 * @param highLightedFlows
	 *            流程所走过线
	 * @return 流程跟踪任务节点和线高亮显示流程图
	 * @see CustomProcessDiagramGenerator#generateDiagram(BpmnModel , String , List ,
	 *      List , List)
	 */
	public static InputStream generateJpgDiagram(BpmnModel bpmnModel, List<String> highLightedHisActivities,
			List<String> highLightedCurActivities, List<String> highLightedFlows) {
		return generateDiagram(bpmnModel, "jpg", highLightedHisActivities, highLightedCurActivities, highLightedFlows);
	}

	/**
	 * Generates a JPG/PNG diagram image of the given process definition, using
	 * the diagram interchange information of the process.
	 * 
	 * @param bpmnModel
	 *            流程定义模型
	 * @param imageType
	 *            流程图类型jpg/png
	 * @param highLightedHisActivities
	 *            流程所走过节点(不包括当前处理节点)
	 * @param highLightedCurActivities
	 *            流程当前处理节点
	 * @param highLightedFlows
	 *            流程所走过线
	 * @return 流程跟踪任务节点和线高亮显示流程图
	 */
	public static InputStream generateDiagram(BpmnModel bpmnModel, String imageType,
			List<String> highLightedHisActivities, List<String> highLightedCurActivities, List<String> highLightedFlows) {
		return generateDiagram(bpmnModel, highLightedHisActivities, highLightedCurActivities, highLightedFlows)
				.generateImage(imageType);
	}

	protected static CustomProcessDiagramCanvas generateDiagram(BpmnModel bpmnModel, List<String> highLightedHisActivities,
			List<String> highLightedCurActivities, List<String> highLightedFlows) {
		CustomProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel);

		// // Draw pool shape, if process is participant in collaboration
		for (Pool pool : bpmnModel.getPools()) {
			GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
			processDiagramCanvas.drawPoolOrLane(pool.getName(), (int) graphicInfo.getX(), (int) graphicInfo.getY(),
					(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());
		}

		// Draw lanes
		for (Process process : bpmnModel.getProcesses()) {
			for (Lane lane : process.getLanes()) {
				GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(lane.getId());
				processDiagramCanvas.drawPoolOrLane(lane.getName(), (int) graphicInfo.getX(), (int) graphicInfo.getY(),
						(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());
			}
		}

		// Draw activities and their sequence-flows
		for (FlowNode flowNode : bpmnModel.getProcesses().get(0).findFlowElementsOfType(FlowNode.class)) {
			drawActivity(processDiagramCanvas, bpmnModel, flowNode, highLightedHisActivities, highLightedCurActivities,
					highLightedFlows);
		}

		return processDiagramCanvas;
	}

	/**
	 * <p>
	 * 参见org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator#
	 * drawActivity(ProcessDiagramCanvas,BpmnModel,FlowNode,List,List)
	 * </p>
	 * <p>
	 * 增加参数highLightedCurActivities当前处理节点集合，标注为绿色
	 * </p>
	 * 
	 * @param processDiagramCanvas
	 * @param bpmnModel
	 * @param flowNode
	 * @param highLightedHisActivities
	 * @param highLightedCurActivities
	 * @param highLightedFlows
	 * @see org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator#drawActivity(CustomProcessDiagramCanvas,BpmnModel,FlowNode,List,List)
	 */
	protected static void drawActivity(CustomProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel,
			FlowNode flowNode, List<String> highLightedHisActivities, List<String> highLightedCurActivities,
			List<String> highLightedFlows) {

		ActivityDrawInstruction drawInstruction = activityDrawInstructions.get(flowNode.getClass());
		if (drawInstruction != null) {

			drawInstruction.draw(processDiagramCanvas, bpmnModel, flowNode);

			// Gather info on the multi instance marker
			boolean multiInstanceSequential = false, multiInstanceParallel = false, collapsed = false;
			if (flowNode instanceof Activity) {
				Activity activity = (Activity) flowNode;
				MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
				if (multiInstanceLoopCharacteristics != null) {
					multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
					multiInstanceParallel = !multiInstanceSequential;
				}
			}

			// Gather info on the collapsed marker
			GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
			if (flowNode instanceof SubProcess) {
				collapsed = graphicInfo.getExpanded() != null && graphicInfo.getExpanded() == false;
			} else if (flowNode instanceof CallActivity) {
				collapsed = true;
			}

			// Actually draw the markers
			processDiagramCanvas.drawActivityMarkers((int) graphicInfo.getX(), (int) graphicInfo.getY(),
					(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(), multiInstanceSequential,
					multiInstanceParallel, collapsed);

			// Draw highlighted activities
			// 修改开始 曾文锋
			// if (highLightedActivities.contains(flowNode.getId())) {
			// drawHighLight(processDiagramCanvas,
			// bpmnModel.getGraphicInfo(flowNode.getId()));
			// }
			// 己走过的历史节点标红色
			if (highLightedHisActivities.contains(flowNode.getId())) {
				drawHighLight(flowNode, Color.RED, processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()));
			}

			// 当前处理节点标绿色色
			if (highLightedCurActivities.contains(flowNode.getId())) {
				drawHighLight(flowNode, Color.GREEN, processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()));
			}
			// 修改结束 曾文锋

		}

		/*
		 * // Original transitions drawing
		 * 
		 * // Outgoing transitions of activity for (SequenceFlow sequenceFlow :
		 * flowNode.getOutgoingFlows()) { List<GraphicInfo> graphicInfoList =
		 * bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId()); boolean
		 * drawedLabel = false; for (int i=1; i<graphicInfoList.size(); i++) {
		 * 
		 * GraphicInfo graphicInfo = graphicInfoList.get(i); GraphicInfo
		 * previousGraphicInfo = graphicInfoList.get(i-1);
		 * 
		 * boolean highLighted =
		 * (highLightedFlows.contains(sequenceFlow.getId())); boolean
		 * drawConditionalIndicator = (i == 1) &&
		 * sequenceFlow.getConditionExpression() != null && !(flowNode
		 * instanceof Gateway);
		 * 
		 * if (i < graphicInfoList.size() - 1) {
		 * processDiagramCanvas.drawSequenceflowWithoutArrow((int)
		 * previousGraphicInfo.getX(), (int) previousGraphicInfo.getY(), (int)
		 * graphicInfo.getX(), (int) graphicInfo.getY(),
		 * drawConditionalIndicator, highLighted); } else {
		 * processDiagramCanvas.drawSequenceflow((int)
		 * previousGraphicInfo.getX(), (int) previousGraphicInfo.getY(), (int)
		 * graphicInfo.getX(), (int) graphicInfo.getY(),
		 * drawConditionalIndicator, highLighted); if (!drawedLabel) {
		 * GraphicInfo labelGraphicInfo =
		 * bpmnModel.getLabelGraphicInfo(sequenceFlow.getId()); if
		 * (labelGraphicInfo != null) { int middleX = (int)
		 * (((previousGraphicInfo.getX() + labelGraphicInfo.getX()) +
		 * (graphicInfo.getX()+ labelGraphicInfo.getX())) / 2); int middleY =
		 * (int) (((previousGraphicInfo.getY() + labelGraphicInfo.getY()) +
		 * (graphicInfo.getY()+ labelGraphicInfo.getY())) / 2); middleX += 15;
		 * processDiagramCanvas.drawLabel(sequenceFlow.getName(), middleX,
		 * middleY, (int) labelGraphicInfo.getWidth(), (int)
		 * labelGraphicInfo.getHeight()); drawedLabel = true; } } } } }
		 */

		// TODO: use graphicInfo

		// Outgoing transitions of activity
		for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
			boolean highLighted = (highLightedFlows.contains(sequenceFlow.getId()));
			boolean isDefault = sequenceFlow.getConditionExpression() == null && (flowNode instanceof Gateway);
			boolean drawConditionalIndicator = sequenceFlow.getConditionExpression() != null
					&& !(flowNode instanceof Gateway);

			List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
			int xPoints[] = new int[graphicInfoList.size()];
			int yPoints[] = new int[graphicInfoList.size()];

			for (int i = 1; i < graphicInfoList.size(); i++) {
				GraphicInfo graphicInfo = graphicInfoList.get(i);
				GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

				if (i == 1) {
					xPoints[0] = (int) previousGraphicInfo.getX();
					yPoints[0] = (int) previousGraphicInfo.getY();
				}
				xPoints[i] = (int) graphicInfo.getX();
				yPoints[i] = (int) graphicInfo.getY();

			}

			processDiagramCanvas.drawSequenceflow(xPoints, yPoints, drawConditionalIndicator, isDefault, highLighted);

			// Draw sequenceflow label
			GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
			if (labelGraphicInfo != null) {
				GraphicInfo lineCenter = getLineCenter(graphicInfoList);
				processDiagramCanvas.drawLabel(sequenceFlow.getName(),
						(int) lineCenter.getX() + (int) labelGraphicInfo.getX(), (int) (lineCenter.getY()
								+ (int) labelGraphicInfo.getY() - labelGraphicInfo.getHeight()),
						(int) labelGraphicInfo.getWidth(), (int) labelGraphicInfo.getHeight(), false);
			}
		}

		// Nested elements
		if (flowNode instanceof FlowElementsContainer) {
			for (FlowElement nestedFlowElement : ((FlowElementsContainer) flowNode).getFlowElements()) {
				if (nestedFlowElement instanceof FlowNode) {
					// 修改开始 曾文锋
					// drawActivity(processDiagramCanvas, bpmnModel, (FlowNode)
					// nestedFlowElement, highLightedActivities,
					// highLightedFlows);
					drawActivity(processDiagramCanvas, bpmnModel, (FlowNode) nestedFlowElement,
							highLightedHisActivities, highLightedCurActivities, highLightedFlows);
					// 修改结束 曾文锋
				}
			}
		}
	}

	/**
	 * <p>
	 * 参见org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator#
	 * drawHighLight(ProcessDiagramCanvas , GraphicInfo )
	 * </p>
	 * <p>
	 * 增加参数FlowNode与Color
	 * </p>
	 * <p>
	 * 根据FlowNode确认节点类型标注颜色Color
	 * </p>
	 * 
	 * @param flowNode
	 * @param color
	 * @param processDiagramCanvas
	 * @param graphicInfo
	 * @see org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator#drawHighLight(CustomProcessDiagramCanvas
	 *      , GraphicInfo )
	 */
	private static void drawHighLight(FlowNode flowNode, Color color, CustomProcessDiagramCanvas processDiagramCanvas,
			GraphicInfo graphicInfo) {
		processDiagramCanvas.drawHighLight(flowNode, color, (int) graphicInfo.getX(), (int) graphicInfo.getY(),
				(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());

	}

	/**
	 * copy from org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator#
	 * initProcessDiagramCanvas(BpmnModel)
	 * 
	 * @param bpmnModel
	 * @see org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator#initProcessDiagramCanvas(BpmnModel)
	 * @return ProcessDiagramCanvas
	 */
	protected static CustomProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel) {

		// We need to calculate maximum values to know how big the image will be
		// in its entirety
		double minX = Double.MAX_VALUE;
		double maxX = 0;
		double minY = Double.MAX_VALUE;
		double maxY = 0;

		for (Pool pool : bpmnModel.getPools()) {
			GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
			minX = graphicInfo.getX();
			maxX = graphicInfo.getX() + graphicInfo.getWidth();
			minY = graphicInfo.getY();
			maxY = graphicInfo.getY() + graphicInfo.getHeight();
		}

		List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
		for (FlowNode flowNode : flowNodes) {

			GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());

			// width
			if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX) {
				maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
			}
			if (flowNodeGraphicInfo.getX() < minX) {
				minX = flowNodeGraphicInfo.getX();
			}
			// height
			if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY) {
				maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
			}
			if (flowNodeGraphicInfo.getY() < minY) {
				minY = flowNodeGraphicInfo.getY();
			}

			for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
				List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
				for (GraphicInfo graphicInfo : graphicInfoList) {
					// width
					if (graphicInfo.getX() > maxX) {
						maxX = graphicInfo.getX();
					}
					if (graphicInfo.getX() < minX) {
						minX = graphicInfo.getX();
					}
					// height
					if (graphicInfo.getY() > maxY) {
						maxY = graphicInfo.getY();
					}
					if (graphicInfo.getY() < minY) {
						minY = graphicInfo.getY();
					}
				}
			}
		}

		List<Artifact> artifacts = gatherAllArtifacts(bpmnModel);
		for (Artifact artifact : artifacts) {

			GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());

			if (artifactGraphicInfo != null) {
				// width
				if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX) {
					maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
				}
				if (artifactGraphicInfo.getX() < minX) {
					minX = artifactGraphicInfo.getX();
				}
				// height
				if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY) {
					maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
				}
				if (artifactGraphicInfo.getY() < minY) {
					minY = artifactGraphicInfo.getY();
				}
			}

			List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
			if (graphicInfoList != null) {
				for (GraphicInfo graphicInfo : graphicInfoList) {
					// width
					if (graphicInfo.getX() > maxX) {
						maxX = graphicInfo.getX();
					}
					if (graphicInfo.getX() < minX) {
						minX = graphicInfo.getX();
					}
					// height
					if (graphicInfo.getY() > maxY) {
						maxY = graphicInfo.getY();
					}
					if (graphicInfo.getY() < minY) {
						minY = graphicInfo.getY();
					}
				}
			}
		}

		int nrOfLanes = 0;
		for (Process process : bpmnModel.getProcesses()) {
			for (Lane l : process.getLanes()) {

				nrOfLanes++;

				GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(l.getId());
				// // width
				if (graphicInfo.getX() + graphicInfo.getWidth() > maxX) {
					maxX = graphicInfo.getX() + graphicInfo.getWidth();
				}
				if (graphicInfo.getX() < minX) {
					minX = graphicInfo.getX();
				}
				// height
				if (graphicInfo.getY() + graphicInfo.getHeight() > maxY) {
					maxY = graphicInfo.getY() + graphicInfo.getHeight();
				}
				if (graphicInfo.getY() < minY) {
					minY = graphicInfo.getY();
				}
			}
		}

		// Special case, see http://jira.codehaus.org/browse/ACT-1431
		if (flowNodes.size() == 0 && bpmnModel.getPools().size() == 0 && nrOfLanes == 0) {
			// Nothing to show
			minX = 0;
			minY = 0;
		}

		//修改开始 曾文锋 
		//return new ProcessDiagramCanvas((int) maxX + 10,(int) maxY + 10, (int) minX, (int) minY);
		return new CustomProcessDiagramCanvas((int) maxX + 10, (int) maxY + 10, (int) minX, (int) minY);
		//修改结束 曾文锋 
	}

}
