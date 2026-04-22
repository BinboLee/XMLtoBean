package com.XMLtoBean;

import com.XMLtoBean.bean.Body0001;
import com.XMLtoBean.bean.RootBean;
import com.XMLtoBean.enums.TranCodeEnum;
import com.XMLtoBean.utils.JAXBUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;

public class App {

    public static void main(String[] args) {

        // 模拟外部传入的完整XML报文
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><head><trancode>0001</trancode></head><body><Msg>我是消息内容</Msg></body></root>";

        // 1. 第一步：解析根报文，获取报文头中的交易码
        RootBean rootBean = JAXBUtil.toBean(xml, RootBean.class);
        String trancode = rootBean.getHead().getTrancode();
        System.out.println("解析到交易码：" + trancode);

        // 2. 第二步：根据交易码，匹配对应的Body实体类
        Class<?> bodyClass = TranCodeEnum.getByCode(trancode).getBodyClass();

        // 3. 第三步：从完整XML中提取<body>节点单独字符串
        String bodyXml = extractBodyNode(xml);

        // 4. 第四步：JAXB解析body为对应Java对象
        Object bodyObj = JAXBUtil.toBean(bodyXml, bodyClass);

        // 5. 业务取值使用
        if (bodyObj instanceof Body0001) {
            Body0001 body = (Body0001) bodyObj;
            System.out.println("报文体Msg内容：" + body.getMsg());
        }
    }

    /**
     * 从完整XML中提取<body>节点内容（兼容JDK8，修复parse报错）
     */
    private static String extractBodyNode(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            NodeList nodeList = document.getElementsByTagName("body");
            Node bodyNode = nodeList.item(0);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(bodyNode), new StreamResult(stringWriter));

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException("提取body节点失败", e);
        }
    }
}