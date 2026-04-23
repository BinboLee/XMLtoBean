package com.XMLtoBean;

import com.XMLtoBean.bean.Body0001;
import com.XMLtoBean.bean.Body0002;
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

        // 模拟外部传入的完整XML报文，CSP使用会从缓冲区读取
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><head><trancode>0002</trancode></head><body><Msg>我是消息内容</Msg><Acct>1025618745</Acct><Name>测试人员</Name></body></root>";

        // 1. 第一步：解析根报文，获取报文头中的交易码
        RootBean rootBean = JAXBUtil.toBean(xml, RootBean.class);//使用JAXBUtil的toBean方法，将报文本身和报文root层结构送进去进行匹配，rootbean可以看作是<root></root>包裹的这一部分数据
        String trancode = rootBean.getHead().getTrancode();//使用getHead方法，把<root></root>中的<head>部分取出来，然后再使用getTrancode去取<head>中的Trancode
        System.out.println("解析到交易码：" + trancode);

        // 2. 第二步：根据交易码，匹配对应的Body实体类
        Class<?> bodyClass = TranCodeEnum.getByCode(trancode).getBodyClass();//用上面提取到的trancode去TranCodeEnum匹配，获取到对应的bean类型，用泛型 bodyClass把实体类存进去

        // 3. 第三步：从完整XML中提取<body>节点单独字符串
        String bodyXml = extractBodyNode(xml);//之所以head可以用上面的方式取，body不能，是因为，head格式是固定的，所以可以写bean，但body由于接口不同内容不同不能写公共的bean，因此针对body只能用DOM去取，所以如果能直接获取到body的xml部分就可以不用写这个方法直接往下走

        // 4. 第四步：JAXB解析body为对应Java对象
        Object bodyObj = JAXBUtil.toBean(bodyXml, bodyClass);//同上rootBean的取法，只是对象类型不能具体，所以写Object

        // 5. 业务取值使用
        if (bodyObj instanceof Body0001) {
            Body0001 body = (Body0001) bodyObj;
            System.out.println("报文体Msg内容：" + body.getMsg());
            System.out.println("报文体Acct内容：" + body.getAcct());
        }

        if (bodyObj instanceof Body0002) {
            Body0002 body = (Body0002) bodyObj;
            System.out.println("报文体Msg内容：" + body.getMsg());
            System.out.println("报文体Name内容：" + body.getName());
        }
    }

    /**
     * 从完整XML中提取<body>节点内容（兼容JDK8，修复parse报错）
     */
    private static String extractBodyNode(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));//这3行是为了把XML写成DOM树放进内存

            //DOM   把XML这种有标准规范的字符串切开存进内存，内存里面有元素节点 Element和文本节点 Text，根据元素节点来获取文本

            NodeList nodeList = document.getElementsByTagName("body");//这里就是找到所有的body元素节点
            Node bodyNode = nodeList.item(0);//取出第一个<body>标签包含的所有内容

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");//这两行是构造XML转换器，并且不要XML标准头
            StringWriter stringWriter = new StringWriter();//在内存中准备一个用来写入string的空间
            transformer.transform(new DOMSource(bodyNode), new StreamResult(stringWriter));//转换器把DOM里的第一个body这个node作为源数据，输入给StreamResult，然后StreamResult再把内容写到stringWriter

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException("提取body节点失败", e);
        }
    }
}