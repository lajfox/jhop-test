package com.techstar.modules.activiti.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Mybatis mappings文件合并工具类
 * 
 * @author sundoctor
 * 
 */
public final class MappingsUtils {

	private static final Logger logger = LoggerFactory.getLogger(MappingsUtils.class);
	private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private static final TransformerFactory tf = TransformerFactory.newInstance();

	/**
	 * 合并文档，将targetFile合并到sourceFile
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @return
	 */
	public static InputStream merge(File sourceFile, File targetFile) {
		try {
			return merge(new FileInputStream(sourceFile), new FileInputStream(targetFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 合并文档，将targetInputStream合并到sourceInputStream
	 * 
	 * @param sourceInputStream
	 * @param targetInputStream
	 * @return 合并后的文档
	 */
	public static InputStream merge(InputStream sourceInputStream, InputStream targetInputStream) {

		DocumentBuilder db = null;
		Document sourceDoc = null, targetDoc = null;
		try {
			db = dbf.newDocumentBuilder();
			db.setEntityResolver(NoVerifyEntityResolver.getInstance());

			sourceDoc = db.parse(sourceInputStream);
			targetDoc = db.parse(targetInputStream);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(sourceInputStream);
			IOUtils.closeQuietly(targetInputStream);
		}

		// 获取两个文件的根元素。
		Element sourceRoot = sourceDoc.getDocumentElement();
		Element targetRoot = targetDoc.getDocumentElement();

		// 下面添加被合并文件根节点下的每个元素
		NodeList targetItems = targetRoot.getChildNodes();
		int item_number = targetItems.getLength();
		// 如果去掉根节点下的第一个元素，比如<所属管理系统> ，那么i从3开始。否则i从1开始。
		for (int i = 1; i < item_number; i = i + 2) {
			// 调用dupliate()，依次复制被合并XML文档中根节点下的元素。
			dupliate(sourceDoc, sourceRoot, targetItems.item(i));
		}

		return transformer(sourceDoc);
	}

	/**
	 * 查找父节点
	 * 
	 * @param sourceDoc
	 *            文档
	 * @param son
	 *            　子节点
	 * @return
	 */
	private static Node findFatherNode(final Document sourceDoc, final Node son) {
		NodeList nodeList = sourceDoc.getElementsByTagName(son.getNodeName());
		return (nodeList == null || nodeList.getLength() <= 0) ? null : nodeList.item(0);
	}

	/**
	 * 复制节点
	 * 
	 * @param sourceDoc
	 * @param sourceRoot
	 * @param son
	 */
	private static void dupliate(Document sourceDoc, Node sourceRoot, Node son) {

		Node oldnode, newnode, importNode = null;
		Node father = findFatherNode(sourceDoc, son);
		if (father == null) {
			// 增加节点
			newnode = son.cloneNode(true);
			importNode = sourceDoc.importNode(newnode, true);
			sourceRoot.appendChild(importNode);
		} else {
			// 复制子结点
			NodeList nodeList = son.getChildNodes();
			int sub_item_number = nodeList.getLength();
			if (sub_item_number >= 2) {
				for (int j = 1; j < sub_item_number; j = j + 2) {
					oldnode = nodeList.item(j);
					newnode = oldnode.cloneNode(true);
					importNode = sourceDoc.importNode(newnode, true);
					father.appendChild(importNode);
				}

			}
		}
	}

	/**
	 * 将文档转换为输入流
	 * 
	 * @param doc
	 *            xml文档
	 * @return
	 */
	private static InputStream transformer(Document doc) {

		InputStream bis = null;
		DOMSource doms = new DOMSource(doc);
		// File f = new File(fileName);
		// StreamResult sr = new StreamResult(f);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StreamResult sr = new StreamResult(bos);

		try {
			Transformer t = tf.newTransformer();
			Properties properties = t.getOutputProperties();
			properties.setProperty(OutputKeys.ENCODING, "UTF-8");
			properties.setProperty(OutputKeys.STANDALONE, "yes");
			properties.setProperty(OutputKeys.DOCTYPE_PUBLIC, "-//mybatis.org//DTD Config 3.0//EN");
			properties.setProperty(OutputKeys.DOCTYPE_SYSTEM, "http://mybatis.org/dtd/mybatis-3-config.dtd");
			t.setOutputProperties(properties);
			t.transform(doms, sr);

			byte[] data = bos.toByteArray();
			if (ArrayUtils.isNotEmpty(data)) {
				bis = new ByteArrayInputStream(data);
			}

		} catch (TransformerException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(bos);
		}

		return bis;
	}

	/**
	 * 不校验xml文档里的DTD
	 * 
	 * @author sundoctor
	 * 
	 */
	private static class NoVerifyEntityResolver implements EntityResolver {

		private static final EntityResolver entityResolver = new NoVerifyEntityResolver();

		private NoVerifyEntityResolver() {
		}

		public static EntityResolver getInstance() {
			return entityResolver;
		}

		/**
		 * 返回一个没有内容的文件，等于不做校验
		 */
		@Override
		public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId) throws SAXException,
				IOException {
			return new InputSource(new StringReader("<?xml version='1.0' encoding='UTF-8'?>"));
		}
	}
}
