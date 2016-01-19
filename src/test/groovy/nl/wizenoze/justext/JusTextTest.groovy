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
     * http://www.aardgasindeklas.nl/allesoveraardgas/aardgasbijjouthuis/
     */
    def testAardgas() {
        String html = loadHtml("aardgas.html")

        when:
        List<Paragraph> paragraphs = jusText.extract(html, "nl")

        then:
        !paragraphs.isEmpty()
        paragraphs[0].text == "Aardgas bij jou thuis"
    }

    /*
     * http://www.cosmos4kids.com/files/galaxy_intro.html
     */
    def testCosmos() {
        String html = loadHtml("cosmos.html")

        when:
        List<Paragraph> paragraphs = jusText.extract(html, "en")

        then:
        paragraphs.size() == 5
        paragraphs[0].text == "We've already covered the universe. Let's focus on something smaller, a galaxy. Right " +
                "now, you're sitting on a planet that orbits a star in the Milky Way galaxy. As you know, there are " +
                "loads of galaxies in the universe. Each is different from the other, like fingerprints. We're going " +
                "to talk about a few of the common traits of galaxies and how you can look up in the sky and look " +
                "for other galaxies."
    }

    /*
     * http://www.kidsgeo.com/geography-for-kids/0168-grassland-biomes.php
     */
    def testGrassland() {
        String html = loadHtml("grassland.html")

        when:
        List<Paragraph> paragraphs = jusText.extract(html, "en")

        then:
        paragraphs.size() == 1
        paragraphs[0].text == "Grassland biomes exist throughout the Earth, and in many cases can be vast, expanding " +
                "across millions of square miles, or kilometers. These biomes are marked by sparse trees and " +
                "extensive grasses as well as a variety of small and large animals. Some of the largest land animals " +
                "on Earth live in grasslands, including American bison, elephants, giraffes, and so forth."
    }

}
