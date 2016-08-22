package nl.wizenoze.justext

import nl.wizenoze.justext.paragraph.Paragraph
import spock.lang.Specification

/**
 * Created by lcsontos on 1/17/16.
 */
class JusTextTest extends Specification {

    final JusText jusTextDefault = new JusText();
    final JusText jusTextWithImages = new JusText(new ClassifierProperties.Builder().setNoImages(false).build());

    def loadHtml(String name) {
        return getClass().getResource("/html/${name}").text;
    }

    /*
     * http://www.aardgasindeklas.nl/allesoveraardgas/aardgasbijjouthuis/
     */
    def testAardgas() {
        String html = loadHtml("aardgas.html")

        when:
        List<Paragraph> paragraphs = jusTextWithImages.extract(html, "nl")
        println(paragraphs)
        then:
        !paragraphs.isEmpty()
        paragraphs[0].text == "Aardgas bij jou thuis"
        paragraphs[1].text == "Koken"
        paragraphs[1].isImage()
    }

    /*
     * http://www.bbc.co.uk/education/guides/zt2hpv4/revision/1
     */
    def testBitesize() {
        String html = loadHtml("bbc_bitesize.html")

        when:
        List<Paragraph> paragraphs = jusTextDefault.extract(html, "en")
        println(paragraphs.collect({ "\"${it.text}\"\n" }))

        then:
        !paragraphs.isEmpty()
    }

    /*
     * http://www.cosmos4kids.com/files/galaxy_intro.html
     */
    def testCosmos() {
        String html = loadHtml("cosmos.html")

        when:
        List<Paragraph> paragraphs = jusTextDefault.extract(html, "en")

        then:
        paragraphs.size() == 7
        paragraphs[0].text == "Looking At Galaxies"
        paragraphs[1].text == "We've already covered the universe. Let's focus on something smaller, a galaxy. Right " +
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
        List<Paragraph> paragraphs = jusTextWithImages.extract(html, "en")

        then:
        paragraphs.size() == 2
        paragraphs[0].text == "Grasslands"
        paragraphs[0].url == "/images/grassland-biome.jpg"
        paragraphs[0].isImage()
        paragraphs[1].text == "Grassland biomes exist throughout the Earth, and in many cases can be vast, expanding " +
                "across millions of square miles, or kilometers. These biomes are marked by sparse trees and " +
                "extensive grasses as well as a variety of small and large animals. Some of the largest land animals " +
                "on Earth live in grasslands, including American bison, elephants, giraffes, and so forth."
    }

    /*
     * http://www.kidsgeo.com/geography-for-kids/0168-grassland-biomes.php
     */
    def testBBCNatureReptile() {
        String html = loadHtml("BBCNatureReptile.html")

        when:
        List<Paragraph> paragraphs = jusTextWithImages.extract(html, "en")

        then:
        paragraphs.size() == 5
        paragraphs[0].text == "Nature"
        paragraphs[1].text == "Wildlife"
        paragraphs[2].text == "Lava lizard basking on the head of a marine iguana"
        paragraphs[2].url == "http://ichef.bbci.co.uk/naturelibrary/images/ic/credit/640x395/r/re/reptile/reptile_1.jpg"
        paragraphs[2].isImage()
        paragraphs[3].text == "Reptiles"
        paragraphs[4].text == "The reptiles are a class of vertebrates. Charateristically they are cold-blooded, have dry scaly or horny skin and a four-chambered heart. Most reptiles lay eggs with leathery shells, but a few types bear live young. Because they are cold-blooded, reptiles are more common in the tropics than in temperate regions and are not found in polar areas."
    }

    /*
     * http://www.askaboutireland.ie/learning-zone/primary-students/5th-+-6th-class/history/history-the-full-story/ireland-the-early-20th-ce/leaders-of-the-1916-risin/patrick-pearse/
     */
    def testPatrickPearse() {
        String html = loadHtml("Patrick_Pearse.html")

        when:
        List<Paragraph> paragraphs = jusTextWithImages.extract(html, "en")

        then:
        !paragraphs.isEmpty()
        paragraphs[21].isImage()
        paragraphs[21].url == "/_internal/cimg!0/rftwihwcfe4nagiq4fjdln5hyn0v2y6"
        paragraphs[22].isImage()
        paragraphs[22].url == "/_internal/cimg!0/3rgm1dvgctkxewdg6qh2f6rzt8sn3jc"
    }

    /*
     * https://www.theguardian.com/us-news/2016/jul/27/bill-clinton-democration-convention-speech-hillary-clinton
     */
    def testBillClinton() {
        String html = loadHtml("Bill_Clinton.html")

        when:
        List<Paragraph> paragraphs = jusTextWithImages.extract(html, "en")

        then:
        !paragraphs.isEmpty()
        paragraphs[0].text.startsWith("Bill Clinton makes")
    }

    def testTheIceBucketChallenge() {
        String html = loadHtml("The_ice_bucket_challenge.html")

        when:
        List<Paragraph> paragraphs = jusTextWithImages.extract(html, "en")
        paragraphs.each {
            println(it.text)
        }
        then:
        !paragraphs.isEmpty()
        paragraphs[0].text.startsWith("Remember the ALS ice bucket challenge?")
    }

}
