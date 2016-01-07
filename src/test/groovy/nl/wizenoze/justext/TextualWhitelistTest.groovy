package nl.wizenoze.justext

import org.jsoup.safety.Whitelist

import spock.lang.Specification

/**
 * Created by lcsontos on 1/7/16.
 */
class TextualWhitelistTest extends Specification {

    final Whitelist whitelist = TextualWhitelist.instance

    void testAddAttributes() {
        when:
        whitelist.addAttributes(null)
        then:
        thrown(UnsupportedOperationException)
    }

    void testAddEnforcedAttribute() {
        when:
        whitelist.addEnforcedAttribute(null, null, null)
        then:
        thrown(UnsupportedOperationException)
    }

    void testAddProtocols() {
        when:
        whitelist.addProtocols(null, null)
        then:
        thrown(UnsupportedOperationException)
    }

    void testAddTags() {
        when:
        whitelist.addTags()
        then:
        thrown(UnsupportedOperationException)
    }

    void testGetInstance() {
        when:
        Whitelist whitelist1 = TextualWhitelist.instance
        Whitelist whitelist2 = TextualWhitelist.instance
        then:
        whitelist1 != null
        whitelist2 != null
        whitelist1 == whitelist2
    }

    void testPreserveRelativeLinks() {
        when:
        whitelist.preserveRelativeLinks(false)
        then:
        thrown(UnsupportedOperationException)
    }

    void testRemoveAttributes() {
        when:
        whitelist.removeAttributes(null)
        then:
        thrown(UnsupportedOperationException)
    }

    void testRemoveEnforcedAttribute() {
        when:
        whitelist.removeEnforcedAttribute(null, null)
        then:
        thrown(UnsupportedOperationException)
    }

    void testRemoveProtocols() {
        when:
        whitelist.removeProtocols(null, null)
        then:
        thrown(UnsupportedOperationException)
    }

    void testRemoveTags() {
        when:
        whitelist.removeTags()
        then:
        thrown(UnsupportedOperationException)
    }

}
