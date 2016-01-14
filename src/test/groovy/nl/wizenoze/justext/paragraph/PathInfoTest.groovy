package nl.wizenoze.justext.paragraph

import nl.wizenoze.justext.util.StringPool

import spock.lang.Specification

/**
 * Created by lcsontos on 1/11/16.
 */
class PathInfoTest extends Specification {

    def testEmptyPath() {
        when:
        PathInfo path = new PathInfo()

        then:
        path.dom() == StringPool.EMPTY
        path.xpath() == StringPool.SLASH
    }

    def testPathWithRootOnly() {
        when:
        PathInfo path = new PathInfo().append("html")

        then:
        path.dom() == "html"
        path.xpath() == "/html[1]"
    }

    def testPathWithMoreElements() {
        when:
        PathInfo path = new PathInfo().append("html").append("body").append("header").append("h1")

        then:
        path.dom() == "html.body.header.h1"
        path.xpath() == "/html[1]/body[1]/header[1]/h1[1]"
    }

    def testContainsMultipleTagsWithTheSameName() {
        when:
        PathInfo path = new PathInfo().append("html").append("body").append("div")

        then:
        path.dom() == "html.body.div"
        path.xpath() == "/html[1]/body[1]/div[1]"

        path.pop().append("div")

        path.dom() == "html.body.div"
        path.xpath() == "/html[1]/body[1]/div[2]"
    }

    def testMoreElementsInTag() {
        when:
        PathInfo path = new PathInfo().append("html").append("body").append("div")

        then:
        path.dom() == "html.body.div"
        path.xpath() == "/html[1]/body[1]/div[1]"

        path.pop()

        path.dom() == "html.body"
        path.xpath() == "/html[1]/body[1]"

        path.append("span")

        path.dom() == "html.body.span"
        path.xpath() == "/html[1]/body[1]/span[1]"

        path.pop()

        path.dom() == "html.body"
        path.xpath() == "/html[1]/body[1]"

        path.append("pre")

        path.dom() == "html.body.pre"
        path.xpath() == "/html[1]/body[1]/pre[1]"
    }

    def testRemovingElement() {
        when:
        PathInfo path = new PathInfo().append("html").append("body")

        then:
        path.append("div").append("a").pop()

        path.dom() == "html.body.div"
        path.xpath() == "/html[1]/body[1]/div[1]"

        path.pop()

        path.dom() == "html.body"
        path.xpath() == "/html[1]/body[1]"
    }

    def testPopOnEmptyPathRaisesException() {
        PathInfo path = new PathInfo()

        when:
        path.pop()

        then:
        thrown NoSuchElementException
    }

}
