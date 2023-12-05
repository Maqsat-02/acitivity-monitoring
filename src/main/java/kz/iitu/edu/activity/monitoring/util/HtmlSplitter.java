package kz.iitu.edu.activity.monitoring.util;

import kz.iitu.edu.activity.monitoring.entity.TextItem;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class HtmlSplitter {
    public List<TextItem> getTextItems(String html) {
        Document doc = Jsoup.parse(html);
        Element contentElement = doc.getElementsByClass("document").get(0);
        return contentElement.children().stream()
                .flatMap(element -> {
                    if (element.tagName().equals("ul") || element.tagName().equals("ol")) {
                        List<TextItem> liTextItems = element.children().stream().map(this::elementToTextItem).toList();
                        if (liTextItems.isEmpty()) return Stream.empty();
                        TextItem firstLiTextItem = liTextItems.get(0);
                        TextItem lastLiTextItem = liTextItems.get(liTextItems.size() - 1);
                        firstLiTextItem.setPrefixTags(getOpeningTag(element) + firstLiTextItem.getPrefixTags());
                        lastLiTextItem.setPostfixTags(lastLiTextItem.getPostfixTags() + getClosingTag(element));
                        return liTextItems.stream();
                    }
                    return Stream.of(elementToTextItem(element));
                })
                .flatMap(textItem -> {
                    List<String> sentences = splitTextIntoSentences(textItem.getText());
                    if (sentences.isEmpty() || sentences.size() == 1) {
                        return Stream.of(textItem);
                    }

                    return IntStream.range(0, sentences.size())
                            .mapToObj(i -> {
                                String sentence = sentences.get(i);
                                return TextItem.builder()
                                        .prefixTags(i == 0 ? textItem.getPrefixTags() : "") // keep prefixTags for first sentence
                                        .text(i == 0 ? sentence : " " + sentence) // keep spaces between sentences
                                        .postfixTags(i == sentences.size() - 1 ? textItem.getPostfixTags() : "") // keep postfixTags for last sentence
                                        .build();
                            });
                })
                .toList();
    }

    private String getOpeningTag(Element element) {
        String tagName = element.tagName();
        String attributes = element.attributes().toString();
        return "<" + tagName + (attributes.isEmpty() ? "" : attributes) + ">";
    }

    private String getClosingTag(Element element) {
        String tagName = element.tagName();
        return "</" + tagName + ">";
    }

    private TextItem elementToTextItem(Element element) {
        // Remove link styles for simplicity
        removeClassIfPresent(element, "Hyperlink");

        String tagsPrefix = getOpeningTag(element);
        String tagsPostfix = getClosingTag(element);

        // Docx4J specifies font styles in span child element.
        // For simplicity take the first span, even though there can be multiple.
        Elements childSpans = element.getElementsByTag("span");
        if (!childSpans.isEmpty()) {
            Element firstSpan = childSpans.get(0);
            removeClassIfPresent(firstSpan, "Hyperlink");

            tagsPrefix = tagsPrefix + getOpeningTag(firstSpan);
            tagsPostfix = getClosingTag(firstSpan) + tagsPostfix;
        }

        return TextItem.builder()
                .prefixTags(tagsPrefix)
                .text(element.text())
                .postfixTags(tagsPostfix)
                .build();
    }

    private void removeClassIfPresent(Element element, String className) {
        if (element.hasClass(className)) {
            element.removeClass(className);
        }
    }

    private List<String> splitTextIntoSentences(String text) {
        SentenceModel sentenceDetectorModel;

        try {
            sentenceDetectorModel = new SentenceModel(
                    Objects.requireNonNull(
                            getClass().getClassLoader().getResourceAsStream("en-sent.bin")
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // E.g.: "3. Birds sang melodies that seemed to echo from another realm."
        // Sentence detector would wrongly split it into "3." and "Birds sang melodies that seemed to echo from another realm."
        // Handle this
        if (text.matches("^\\d\\..+\\.$")) {
            return List.of(text);
        }

        SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceDetectorModel);

        String[] sentences = sentenceDetector.sentDetect(text);
        return Arrays.asList(sentences);
    }
}
