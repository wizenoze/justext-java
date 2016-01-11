package nl.wizenoze.justext

import spock.lang.Specification

/**
 * Created by lcsontos on 1/11/16.
 */
class ParagraphMakerTest extends Specification {

    def testNoParagraphs() {
        def html = '<html><body></body></html>'
        def reader = new StringReader(html)
        def paragraphMaker = new ParagraphMaker(reader)

        when:
        def paragraphs = paragraphMaker.traverse()

        then:
        paragraphs.size() == 0
    }

}
