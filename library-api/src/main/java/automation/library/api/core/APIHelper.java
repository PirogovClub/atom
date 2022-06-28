package automation.library.api.core;


//import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.StringWriter;


public class APIHelper {
	
	public APIHelper(){}

    public static Document setDoc(String path) throws Exception{
	    DocumentBuilderFactory dbf =  DocumentBuilderFactory.newInstance();
	    Document doc = dbf.newDocumentBuilder().parse(new File(path));
	    return doc;
    }

    public String getElement(Document doc, String path) throws Exception{
	    String val=null;
	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    XPathExpression expr = xpath.compile(path);
	    Node el = (Node) expr.evaluate(doc, XPathConstants.NODE);
	
	    if (el !=null){
	                    val = el.getTextContent();
	    }
	
	    return val;
    }

    public static Document setElement(Document doc, String path, String val) throws Exception{
    	
    	XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = xpath.compile(path);
        Node el = (Node) expr.evaluate(doc, XPathConstants.NODE);


        if (el !=null){
	        if (val.equalsIgnoreCase("<rem>")){
	                        el.getParentNode().removeChild(el);
	        }else{
	                        el.setTextContent(val);
	        }
        }else {
            // int numnodes = getOccurences(path, "/");
            String parent = getParent(path);
            String child = getChild(path);
                            
            xpath = factory.newXPath();
            expr = xpath.compile(parent);
            el = (Node) expr.evaluate(doc, XPathConstants.NODE);
            
            Element el1 = doc.createElement(child);
            el.appendChild(el1);
            el1.setTextContent(val);

        }
        return doc;
        
    }

    public static String createRequestXML(Document doc) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc),  new StreamResult(writer));
        return writer.getBuffer().toString();
    }

   /* public static JSONObject jsonMerge(JSONObject source, JSONObject target) throws JSONException {
        String[] keys = source.names();
            if (keys !=null){
                for (String key: keys) {
                	
                    Object value = source.get(key);
                    
                    if (!target.has(key)) {
                        target.put(key, value);
                    } else {
                    	if (value instanceof JSONObject) {
                    		JSONObject valueJson = (JSONObject)value;
                            jsonMerge(valueJson, target.getJSONObject(key));
                        } else {
                            target.put(key, value);
                        }
                    }
                }
            }
            return target;
    }

    public static JSONObject jsonToXML(Document doc, JSONObject source, String xpath) throws Exception {
        String[] keys = JSONObject.getNames(source);
        for (String key: keys) {   
            Object value = source.get(key);
            if (value instanceof JSONObject) {
                JSONObject valueJson = (JSONObject)value;
                jsonToXML(doc, valueJson, xpath+"/"+key);
            }else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i=0;i<array.length();i++){
	                Object arrayvalue = array.get(i);
	                if (arrayvalue instanceof JSONObject) {
	                                JSONObject valueJson = (JSONObject)arrayvalue;
	                                jsonToXML(doc, valueJson, xpath+"/"+key+"["+(i+1)+"]");
	                }else{
	                setElement(doc, xpath+"/"+key+"["+(i+1)+"]",array.get(i).toString());
	                }
                }
            }else{    
                String val = value.toString();
                setElement(doc, xpath+"/"+key,val);
            }
        }
        return source;
    }

    public static JSONObject checkXML(XPathHelper xph, JSONObject source, String xpath) throws Exception {
        String[] keys = JSONObject.getNames(source);
        for (String key: keys) {   
            Object value = source.get(key);
            if (value instanceof JSONObject) {
                JSONObject valueJson = (JSONObject)value;
                checkXML(xph, valueJson, xpath+"/"+key);
            }else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i=0;i<array.length();i++){
                    Object arrayvalue = array.get(i);
                    if (arrayvalue instanceof JSONObject) {
                        JSONObject valueJson = (JSONObject)arrayvalue;
                        checkXML(xph, valueJson, xpath+"/"+key+"["+(i+1)+"]");
                    }
                }
            }else{
                String val = value.toString();
                try {
                    Assert.assertEquals(xph.query(xpath+"/"+key),val);
                }catch (AssertionError e) {
                    //ExtentReport.logFail("expected: "+key+"="+val+";"+"actual: "+key+"="+xph.query(xpath+"/"+key));
                    throw e;
                }
            }
        }
        return source;
    }*/
                
                
    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }
                
    public static int getOccurences(String str, String substr){
	    int occurrences = 0;
	    for(char c : str.toCharArray()){
	       if(c == '/'){
	          occurrences++;
	       }
	    }
	    
	    return occurrences;
    }
    
    public static String getParent(String path){
        int occurences = getOccurences(path, "/");
        
        String parent = path.substring(0,ordinalIndexOf(path, "/", occurences));
        
        return parent;
    }
    
    public static String getChild(String path){
	    int occurences = getOccurences(path, "/");
	    
	    String child = path.substring(ordinalIndexOf(path, "/", occurences)+1,path.length());
	    
	    return child;
    }
                
                
}
