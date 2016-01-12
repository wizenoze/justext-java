package nl.wizenoze.justext.util

import nl.wizenoze.justext.util.HtmlUtil
import spock.lang.Specification

import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

import static org.w3c.dom.Node.TEXT_NODE

/**
 * Created by lcsontos on 1/7/16.
 */
class HtmlUtilTest extends Specification {

    def testCleanDom() {
        def dirtyHtml = "<html><!-- comment --><body><h1>Header</h1><!-- comment --> text<p>footer</body></html>"
        def xpath = XPathFactory.newInstance().newXPath()
        when:
        def cleanedDom = HtmlUtil.cleanDom(dirtyHtml);
        def nodes = xpath.evaluate("/html/body/h1/text()", cleanedDom.documentElement, XPathConstants.NODESET)
        then:
        nodes.length == 1
        nodes.item(0).nodeType == TEXT_NODE
        nodes.item(0).textContent == "Header"
    }

    def testRemoveComments() {
        def dirtyHtml = "<html><!-- comment --><body><h1>Header</h1><!-- comment --> text<p>footer</body></html>"
        when:
        def cleanedHtml = HtmlUtil.cleanHtml(dirtyHtml)
        then:
        cleanedHtml.equals("<html><body><h1>Header</h1>text<p>footer</p></body></html>")
    }

    def testRemoveHeadTag() {
        def dirtyHtml = """
            <html><head><title>Title</title></head><body>
            <h1>Header</h1>
            <p><span>text</span></p>
            <p>footer <em>like</em> a boss</p>
            </body></html>
        """
        when:
        def cleanedHtml = HtmlUtil.cleanHtml(dirtyHtml)
        println(cleanedHtml)
        then:
        cleanedHtml.equals(
                "<html><body><h1>Header</h1><p><span>text</span></p><p>footer <em>like</em> a boss</p></body></html>")
    }

    def testSimpleXhtmlWithDeclaration() {
        def dirtyHtml = """
            <?xml version="1.0" encoding="windows-1250"?>
            <!DOCTYPE html>
            <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="sk" lang="sk">
            <head>
            <title>Hello World</title>
            <meta http-equiv="imagetoolbar" content="no" />
            <meta http-equiv="Content-Type" content="text/html; charset=windows-1250" />
            </head>
            <body id="index">
            </body>
            </html>
        """
        when:
        def cleanedHtml = HtmlUtil.cleanHtml(dirtyHtml)
        println(cleanedHtml)
        then:
        cleanedHtml.equals(
                '<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="sk" lang="sk"><body id="index"></body></html>')
    }

}
