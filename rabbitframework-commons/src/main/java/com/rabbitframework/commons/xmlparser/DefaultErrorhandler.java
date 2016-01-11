package com.rabbitframework.commons.xmlparser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DefaultErrorhandler implements ErrorHandler {
	private final static Logger log = LogManager.getLogger(DefaultErrorhandler.class);

    public void warning(SAXParseException exception) throws SAXException {
        log.warn(exception.getMessage(), exception);
    }

    public void error(SAXParseException exception) throws SAXException {
        log.error(exception.getMessage(), exception);
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

}
