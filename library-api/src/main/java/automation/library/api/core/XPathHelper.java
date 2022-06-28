package automation.library.api.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;


/**
 * Deprecated, use @XmlHelper instead.
 */
public class XPathHelper
{

    private XPath xpath;
    private Document document;
    protected Logger log = LogManager.getLogger(this.getClass().getName());


    /**
     * Deprecated, use @XmlHelper instead.
     * @param xml input xml
     */
    public XPathHelper(String xml)
    {
        InputSource source = new InputSource(new StringReader(xml));

        XPathFactory xpf = XPathFactory.newInstance();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(source);
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            log.error("Unable to parse input String for XPath document", e);
        }

        xpath = xpf.newXPath();
    }

    /**
     * Deprecated, use @XmlHelper instead.
     * @param query query
     * @return value
     */
    public String query(String query)
    {
        String result = "";
        try
        {
            XPathExpression expr = xpath.compile(query);
            result = expr.evaluate(document);
        }
        catch (XPathExpressionException e)
        {
            log.error("Invalid XPath query", e);
        }
        return result;
    }
}