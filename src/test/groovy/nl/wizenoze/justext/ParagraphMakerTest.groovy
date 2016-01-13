package nl.wizenoze.justext

import nl.wizenoze.justext.html.HtmlBeautifier

import spock.lang.Specification

/**
 * Created by lcsontos on 1/11/16.
 */
class ParagraphMakerTest extends Specification {

    void assertParagraphEqual(Paragraph paragraph, String text, int wordsCount, int tagsCount) {
        assert paragraph.text == text
        assert paragraph.wordsCount == wordsCount
        assert paragraph.tagsCount == tagsCount
    }

    def createParagraphs(String html) {
        html = HtmlBeautifier.cleanHtml(html)
        def reader = new StringReader(html)
        def paragraphMaker = new ParagraphMaker(reader)

        paragraphMaker.traverse()
    }

    def testBasic() {
        def html = """
            <html><body>
            <h1>Header</h1>
            <p>text and some <em>other</em> words <span class="class">that I</span> have in my head now</p>
            <p>footer</p>
            </body></html>
        """

        when:
        def paragraphs = createParagraphs(html)

        then:
        paragraphs.size() == 3
        assertParagraphEqual(paragraphs[0], "Header", 1, 0)
        assertParagraphEqual(paragraphs[1], "text and some other words that I have in my head now", 12, 2)
        assertParagraphEqual(paragraphs[2], "footer", 1, 0)
    }

    def testNoParagraphs() {
        def html = '<html><body></body></html>'

        when:
        def paragraphs = createParagraphs(html)

        then:
        paragraphs.size() == 0
    }

    def testWhitespaceHandling() {
        def html = """
            <html><body>
            <p>pre<em>in</em>post \t pre  <span class="class"> in </span>  post</p>
            <div>pre<em> in </em>post</div>
            <pre>pre<em>in </em>post</pre>
            <blockquote>pre<em> in</em>post</blockquote>
            </body></html>
        """

        when:
        def paragraphs = createParagraphs(html)

        then:
        paragraphs.size() == 4
        assertParagraphEqual(paragraphs[0], "preinpost pre in post", 4, 2)
        assertParagraphEqual(paragraphs[1], "pre in post", 3, 1)
        assertParagraphEqual(paragraphs[2], "prein post", 2, 1)
        assertParagraphEqual(paragraphs[3], "pre inpost", 2, 1)
    }

    def testMultipleLineBreak() {
        def html = """
            <html><body>
              normal text   <br><br> another   text
            </body></html>
        """

        when:
        def paragraphs = createParagraphs(html)

        then:
        paragraphs.size() == 2
        assertParagraphEqual(paragraphs[0], "normal text", 2, 0)
        assertParagraphEqual(paragraphs[1], "another text", 2, 0)

    }

    def testInlineTextInBody() {
        def html = """
            <html><body>
            <sup>I am <strong>top</strong>-inline\n\n\n\n and I am happy \n</sup>
            <p>normal text</p>
            <code>\nvar i = -INFINITY;\n</code>
            <div>after text with variable <var>N</var> </div>
               I am inline\n\n\n\n and I am happy \n
            </body></html>
        """

        when:
        def paragraphs = createParagraphs(html)

        then:
        paragraphs.size() == 5
        assertParagraphEqual(paragraphs[0], "I am top-inline\nand I am happy", 7, 2)
        assertParagraphEqual(paragraphs[1], "normal text", 2, 0)
        assertParagraphEqual(paragraphs[2], "var i = -INFINITY;", 4, 1)
        assertParagraphEqual(paragraphs[3], "after text with variable N", 5, 1)
        assertParagraphEqual(paragraphs[4], "I am inline\nand I am happy", 7, 0)
    }

    def testLinks() {
        def html = """
                <html><body>
                <a>I am <strong>top</strong>-inline\n\n\n\n and I am happy \n</a>
                <p>normal text</p>
                <code>\nvar i = -INFINITY;\n</code>
                <div>after <a>text</a> with variable <var>N</var> </div>
                   I am inline\n\n\n\n and I am happy \n
                </body></html>
        """

        when:
        def paragraphs = createParagraphs(html)

        then:
        paragraphs.size() == 5
        assertParagraphEqual(paragraphs[0], "I am top-inline\nand I am happy", 7, 2)
        assertParagraphEqual(paragraphs[1], "normal text", 2, 0)
        assertParagraphEqual(paragraphs[2], "var i = -INFINITY;", 4, 1)
        assertParagraphEqual(paragraphs[3], "after text with variable N", 5, 1)
        assertParagraphEqual(paragraphs[4], "I am inline\nand I am happy", 7, 0)
    }

}
