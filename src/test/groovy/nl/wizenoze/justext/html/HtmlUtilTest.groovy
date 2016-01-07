package nl.wizenoze.justext.html

import spock.lang.Specification

/**
 * Created by lcsontos on 1/7/16.
 */
class HtmlUtilTest extends Specification {

    def testRemoveComments() {
        def dirtyHtml = "<html><!-- comment --><body><h1>Header</h1><!-- comment --> text<p>footer</body></html>"
        when:
        def cleanedHtml = HtmlUtil.clean(dirtyHtml)
        then:
        cleanedHtml.equals("<html><body><h1>Header</h1> text<p>footer</p></body></html>")
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
        def cleanedHtml = HtmlUtil.clean(dirtyHtml)
        println(cleanedHtml)
        then:
        cleanedHtml.equals(
                "<html><body><h1>Header</h1><p><span>text</span></p><p>footer <em>like</em> a boss</p></body></html>")
    }

}
