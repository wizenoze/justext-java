package nl.wizenoze.justext

import nl.wizenoze.justext.paragraph.Paragraph
import spock.lang.Specification

/**
 * Created by lcsontos on 1/17/16.
 */
class JusTextTest extends Specification {

    final JusText jusText = new JusText();

    def loadHtml(String name) {
        return getClass().getResource("/html/${name}").text;
    }

    /*
     * http://www.cosmos4kids.com/files/galaxy_intro.html
     */
    def testCosmos() {
        String html = loadHtml("cosmos.html")

        when:
        List<Paragraph> paragraphs = jusText.extract(html)

        then:
        println(paragraphs.collect { it.text })
    }

    /*
     * http://www.kidsgeo.com/geography-for-kids/0168-grassland-biomes.php
     */
    def testGrassland() {
        String html = loadHtml("grassland.html")

        when:
        List<Paragraph> paragraphs = jusText.extract(html)

        then:
        println(paragraphs.collect { it.text })
    }

}
