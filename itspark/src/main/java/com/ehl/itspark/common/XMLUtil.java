package com.ehl.itspark.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;


public class XMLUtil {
    /** 
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。 
     * @param strxml 
     * @return 
     * @throws JDOMException 
     * @throws IOException 
     */  
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {  
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");  
  
        if(null == strxml || "".equals(strxml)) {  
            return null;  
        }            
        Map m = new HashMap();         
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));  
        SAXBuilder builder = new SAXBuilder();  
        Document doc = builder.build(in);  
        Element root = doc.getRootElement();  
        List list = root.getChildren();  
        Iterator it = list.iterator();  
        while(it.hasNext()) {  
            Element e = (Element) it.next();  
            String k = e.getName();  
            String v = "";  
            List children = e.getChildren();  
            if(children.isEmpty()) {  
                v = e.getTextNormalize();  
            } else {  
                v = XMLUtil.getChildrenText(children);  
            }       
            m.put(k, v);  
        }      
        //关闭流  
        in.close();            
        return m;  
    }  
      
    /** 
     * 获取子结点的xml 
     * @param children 
     * @return String 
     */  
    public static String getChildrenText(List children) {  
        StringBuffer sb = new StringBuffer();  
        if(!children.isEmpty()) {  
            Iterator it = children.iterator();  
            while(it.hasNext()) {  
                Element e = (Element) it.next();  
                String name = e.getName();  
                String value = e.getTextNormalize();  
                List list = e.getChildren();  
                sb.append("<" + name + ">");  
                if(!list.isEmpty()) {  
                    sb.append(XMLUtil.getChildrenText(list));  
                }  
                sb.append(value);  
                sb.append("</" + name + ">");  
            }  
        }          
        return sb.toString();  
    }  
    
    /**
	 * 将xml string 转化为map
	 * 
	 * @param xmlDoc
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> xmlToMap(String xmlDoc) throws JDOMException, IOException {
		// 创建一个新的字符串
		StringReader read = new StringReader(xmlDoc);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();

		Map<String, Object> xmlMap = new HashMap<String, Object>();

		Document doc = sb.build(source); // 通过输入源构造一个Document
		Element root = doc.getRootElement(); // 取的根元素

		List<Element> cNodes = root.getChildren(); // 得到根元素所有子元素的集合(根元素的子节点，不包括孙子节点)
		Element et = null;
		for (int i = 0; i < cNodes.size(); i++) {
			et = cNodes.get(i); // 循环依次得到子元素
			xmlMap.put(et.getName(), et.getText());
		}
		return xmlMap;
	}
}
