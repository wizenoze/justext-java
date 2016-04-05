# justext-java

justext-java is a library for removing boilerplate content, such as navigation links, headers, and footers from HTML pages. It is designed to preserve mainly text containing full sentences and it is therefore well suited for creating linguistic resources such as Web corpora. This implementation is the Java port of https://github.com/miso-belica/jusText.

# How it works

See what is kept and what is discarded from a [typical web page](http://corpus.tools/raw-attachment/wiki/Justext/nlp_jusText_fi.jpg).
Read a description of the [jusText algorithm](http://corpus.tools/wiki/Justext/Algorithm).

# Releases

TBD

# Building from source

```
% mvn clean install
```

# Code Sample (Groovy)

```
import nl.wizenoze.justext.JusText
import nl.wizenoze.justext.paragraph.Paragraph
import nl.wizenoze.justext.util.StopWordsUtil

JusText jusText = new JusText()
String rawHtml = "http://www.devx.com/wireless/remote-work-and-the-social-forces-and-technologies-that-enable-it.html".toURL().getText()
Set<String> stopWords = StopWordsUtil.getStopWords("en")
List<Paragraph> paragraphs = jusText.extract(rawHtml, stopWords)

paragraphs.each { println(it.getText()) }
```

# License

justext-java is available under the GNU Lesser General Public License v3.0.

# Copyright

Copyright (c) 2016-present WizeNoze B.V. All rights reserved.
