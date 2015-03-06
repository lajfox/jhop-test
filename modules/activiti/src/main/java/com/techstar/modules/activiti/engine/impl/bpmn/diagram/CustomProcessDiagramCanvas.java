package com.techstar.modules.activiti.engine.impl.bpmn.diagram;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas;

/**
 * 扩展org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas
 * 
 * @author sundoctor
 * @see org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas
 * @see CustomProcessDiagramGenerator
 */
public class CustomProcessDiagramCanvas extends ProcessDiagramCanvas {

	public CustomProcessDiagramCanvas(int width, int height) {
		super(width, height);
	}

	public CustomProcessDiagramCanvas(int width, int height, int minX, int minY) {
		super(width, height, minX, minY);
	}

	/**
	 * 标记流程节点高亮
	 * 
	 * @param flowNode
	 *            流程节点
	 * @param color
	 *            高亮颜色
	 * @param x
	 *            X坐标点值
	 * @param y
	 *            Ｙ坐标点值
	 * @param width
	 *            　节点宽度
	 * @param height
	 *            　节点高度
	 */
	public void drawHighLight(FlowNode flowNode, Color color, int x, int y, int width, int height) {
		Paint originalPaint = g.getPaint();
		Stroke originalStroke = g.getStroke();

		g.setPaint(color);

		if (color == Color.GREEN || flowNode instanceof EndEvent) {// 当前处理节点或者结束节点
			g.setStroke(THICK_TASK_BORDER_STROKE);
		}

		if (flowNode instanceof StartEvent || flowNode instanceof EndEvent) {// 开始、结束节点
			if (flowNode instanceof EndEvent) {
				g.setPaint(Color.YELLOW);
			}
			Ellipse2D ellipse = new Ellipse2D.Double(x, y, width, height);
			g.draw(ellipse);
		} else if (flowNode instanceof Gateway) {// 网关节点
			drawGateway(x, y, width, height);
		} else {// 其它节点
			RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
			g.draw(rect);
		}

		g.setPaint(originalPaint);
		g.setStroke(originalStroke);
	}

}
