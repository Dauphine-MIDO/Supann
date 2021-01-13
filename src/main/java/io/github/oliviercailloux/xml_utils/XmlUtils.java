package io.github.oliviercailloux.xml_utils;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtils {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtils.class);
	private static DocumentBuilder builder;

	public static String toXml(JAXBElement<?> element) {
		try {
			final JAXBContext jc = JAXBContext.newInstance(element.getValue().getClass());
			final Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			final StringWriter wr = new StringWriter();
			marshaller.marshal(element, wr);
			return wr.toString();
		} catch (JAXBException exc) {
			throw new IllegalStateException(exc);
		}
	}

	private static void prepareBuilder() {
		if (builder == null) {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public static Document asDocument(InputSource input) {
		prepareBuilder();

		final Document doc;
		try {
			doc = builder.parse(input);
		} catch (SAXException | IOException e) {
			throw new IllegalStateException(e);
		}

		final Element docE = doc.getDocumentElement();
		LOGGER.debug("Main tag name: {}.", docE.getTagName());

		return doc;
	}

}
