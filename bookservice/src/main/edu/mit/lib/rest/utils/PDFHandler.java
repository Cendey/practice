package edu.mit.lib.rest.utils;


import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.utils.PDFHandler</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/7/2016
 */
public class PDFHandler {

    /**
     * This will remove links in the document.
     *
     * @param document The PDF to remove the link(s) in.
     * @param source   The content of the link to remove.
     * @throws IOException If there is an error remove the link(s).
     */
    private void removeLinks(PDDocument document, String source) throws IOException {
        replaceLinks(document, source, null);

    }

    /**
     * This will remove/replace matched RUI in the document.
     *
     * @param page         Each page in the document
     * @param specifiedURI The matched URI in the document
     * @param newURI       The replaced URI specified by argument
     * @param pageNum      Current page number
     */
    private void replaceURI(PDPage page, String specifiedURI, String newURI, int pageNum) {
        try {
            List<PDAnnotation> annotations = page.getAnnotations();
            Iterator<PDAnnotation> iterator = annotations.iterator();
            while (iterator.hasNext()) {
                PDAnnotation annotation = iterator.next();
                if (annotation instanceof PDAnnotationLink) {
                    PDAnnotationLink link = (PDAnnotationLink) annotation;
                    PDAction action = link.getAction();
                    if (action instanceof PDActionURI) {
                        PDActionURI uri = (PDActionURI) action;
                        String oldURI = uri.getURI();
                        if (oldURI.matches(specifiedURI)) {
                            if (newURI != null && newURI.trim().length() > 0) {
                                System.out.println(
                                    String.format("Page [%d] : Replacing URI [%s] with [%s]", pageNum, oldURI, newURI));
                                uri.setURI(newURI);
                            } else {
                                System.out.println(String.format("Remove URI [%s] at Page [%d]", oldURI, pageNum));
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This will remove/replace matched string in the document.
     *
     * @param document     The document in which the matched string  will to be removed/replaced.
     * @param page         Each page in the document
     * @param specifiedURI The matched string in the document
     * @param newRUI       The replaced string specified in argument
     * @param pageNum      Current page number
     */
    private void replaceText(PDDocument document, PDPage page, String specifiedURI, String newRUI, int pageNum) {
        try {
            PDFStreamParser parser = new PDFStreamParser(page);
            parser.parse();
            // Get all tokens from the page
            List<Object> tokens = parser.getTokens();
            // Create a temporary List
            List<Object> newTokens = new ArrayList<>();

            for (Object token : tokens) {
                if (token instanceof Operator) {
                    Operator operator = Operator.class.cast(token);
                    COSDictionary dictionary = operator.getImageParameters();
                    if (dictionary != null) {
                        System.out.println(dictionary.toString());
                    }

                    if (operator.getName().equals("Tj")) {
                        // Tj contains 1 COSString
                        COSString previous = (COSString) newTokens.get(newTokens.size() - 1);
                        String oldContent = previous.getString();
                        // here can be any filters for checking a necessary string
                        if (oldContent.matches(specifiedURI)) {
                            if (newRUI != null) {
                                COSArray newLink = new COSArray();
                                newLink.add(new COSString(newRUI));
                                newTokens.set(newTokens.size() - 1, newLink);
                                System.out.println(String
                                    .format("Page [%d] : Replacing content [%s] with [%s]", pageNum, oldContent, newRUI)
                                );
                            } else {
                                newTokens.remove(newTokens.size() - 1);
                                System.out
                                    .println(String.format("Remove content [%s] at Page [%d]", oldContent, pageNum));
                            }
                        }
                    } else if (operator.getName().equals("TJ")) {
                        // TJ contains a COSArray with COSStrings and COSFloat (padding)
                        COSArray previous = (COSArray) newTokens.get(newTokens.size() - 1);

                        Iterator<COSBase> iterator1 = previous.iterator();
                        while (iterator1.hasNext()) {
                            COSBase element = iterator1.next();
                            if (element instanceof COSString) {
                                COSString cosString = (COSString) element;
                                String oldContent = cosString.getString();

                                // here can be any filters for checking a necessary string
                                if (oldContent.matches(specifiedURI)) {
                                    if (newRUI != null) {
                                        COSArray newLink = new COSArray();
                                        newLink.add(new COSString(newRUI));
                                        newTokens.set(newTokens.size() - 1, newLink);
                                        System.out.println(String
                                            .format("Page [%d] : Replacing content [%s] with [%s]", pageNum, oldContent,
                                                newRUI));
                                    } else {
                                        iterator1.remove();
                                        System.out.println(
                                            String.format("Remove content [%s] at Page [%d]", oldContent, pageNum));
                                    }
                                } else if (String.valueOf(oldContent).startsWith(specifiedURI)) {
                                    if (newRUI != null) {
                                        // this code should be changed. It can have some indenting problems that depend on COSFloat values
                                        COSArray newLink = (COSArray) newTokens.get(newTokens.size() - 1);
                                        int size = newLink.size();
                                        float f = ((COSFloat) newLink.get(size - 4)).floatValue();
                                        for (int i = 0; i < size - 4; i++) {
                                            newLink.remove(0);
                                        }
                                        newLink.set(0, new COSString(newRUI));
                                        // number for padding from right place. Should be checked.
                                        newLink.set(1, new COSFloat(f - 8000));
                                        newTokens.set(newTokens.size() - 1, newLink);
                                        System.out.println(String
                                            .format("Page [%d] : Replacing content [%s] with [%s]", pageNum, oldContent,
                                                newRUI));
                                    }
                                }
                            }
                        }
                    }
                }
                // save tokens to a temporary List
                newTokens.add(token);
            }

            // save the replaced data back to the document's srteam
            PDStream newContents = new PDStream(document);
            OutputStream out = newContents.createOutputStream(COSName.FLATE_DECODE);
            ContentStreamWriter writer = new ContentStreamWriter(out);
            writer.writeTokens(newTokens);
            out.close();

            // save content
            page.setContents(newContents);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This will remove/replace matched RUL link in the document.
     *
     * @param document     The PDF to remove the link(s) in.
     * @param specifiedURI The content of the link to remove.
     * @param newURI       The new content of the link to replace.
     * @throws IOException If there is an error setting the field.
     */
    private void replaceLinks(PDDocument document, String specifiedURI, String newURI) throws IOException {
        PDDocumentCatalog docCatalog = document.getDocumentCatalog();
        PDPageTree pageTree = docCatalog.getPages();
        int pageNum = 0;
        for (PDPage page : pageTree) {
            ++pageNum;
            replaceText(document, page, specifiedURI, newURI, pageNum);
            replaceURI(page, specifiedURI, newURI, pageNum);
        }
    }

    public static void replaceLink(PDDocument document, String specifiedURI, String newURI) {

        try {
            // replace all links in a footer and a header in XObjects with /ProcSet [/PDF /Text]
            // Note: these forms (and pattern objects too!) can have resources,
            // i.e. have Form XObjects or patterns again.
            // If so you need to use a recursion
            for (PDPage page : document.getPages()) {
                List<Object> newPdxTokens = new ArrayList<>();
                // Get all XObjects from the page
                for (COSName cosName : page.getResources().getXObjectNames()) {
                    boolean isHasTextStream = false;
                    PDXObject pdxObject = page.getResources().getXObject(cosName);
                    // If a stream has not '/ProcSet [/PDF /Text]' line inside it has to be skipped
                    // isXobjectHasTextFieldInPdf has a recursion
                    if (COSDictionary.class.isAssignableFrom(pdxObject.getCOSObject().getClass())) {
                        isHasTextStream = isXobjectHasTextFieldInPdf(pdxObject.getCOSObject());
                    }

                    if (pdxObject instanceof PDFormXObject && isHasTextStream) {
                        // Set stream from pdxObject
                        PDStream stream = pdxObject.getStream();
                        PDFStreamParser streamParser = new PDFStreamParser(stream.toByteArray());
                        streamParser.parse();
                        Iterator<Object> tokens = streamParser.getTokens().iterator();
                        while (tokens.hasNext()) {
                            Object token = tokens.next();
                            if (token instanceof Operator) {
                                Operator op = (Operator) token;
                                if (op.getName().equals("Tj")) {
                                    // Tj contains 1 COSString
                                    COSString previous = (COSString) newPdxTokens.get(newPdxTokens.size() - 1);
                                    String oldURI = previous.getString();
                                    // here can be any filters for checking a necessary string
                                    if (oldURI.matches(specifiedURI)) {
                                        if (newURI != null && newURI.trim().length() > 0) {
                                            COSArray newLink = new COSArray();
                                            newLink.add(new COSString(newURI));
                                            newPdxTokens.set(newPdxTokens.size() - 1, newLink);
                                        } else {
                                            tokens.remove();
                                            page.getResources().getCOSObject().removeItem(cosName);
                                        }
                                    }
                                } else if (op.getName().equals("TJ")) {
                                    processCOSArray(newPdxTokens);
                                }
                            }
                            // save tokens to a temporary List
                            if (token != null) {
                                newPdxTokens.add(token);
                            }
                        }
                        // save the replaced data back to the stream
                        OutputStream out = stream.createOutputStream();
                        ContentStreamWriter writer = new ContentStreamWriter(out);
                        writer.writeTokens(newPdxTokens);
                        out.close();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void replaceText(PDDocument document, String specifiedURI, String newURI) throws IOException {
        // replace data from any text stream from pdf. XObjects not included.
        for (PDPage page : document.getPages()) {
            PDFStreamParser parser = new PDFStreamParser(page);
            parser.parse();
            // Get all tokens from the page
            List<Object> tokens = parser.getTokens();
            // Create a temporary List
            List<Object> newTokens = new ArrayList<>();

            Iterator<Object> iterator = tokens.iterator();
            while (iterator.hasNext()) {
                Object token = iterator.next();
                if (token instanceof Operator) {
                    COSDictionary dictionary = ((Operator) token).getImageParameters();
                    if (dictionary != null) {
                        System.out.println(dictionary.toString());
                    }
                }
                if (token instanceof Operator) {
                    Operator op = (Operator) token;
                    if (op.getName().equals("Tj")) {
                        // Tj contains 1 COSString
                        COSString previous = (COSString) newTokens.get(newTokens.size() - 1);
                        String oldURI = previous.getString();
                        // here can be any filters for checking a necessary string
                        if (oldURI.matches(specifiedURI)) {
                            if (newURI != null && newURI.trim().length() > 0) {
                                COSArray newLink = new COSArray();
                                newLink.add(new COSString(newURI));
                                newTokens.set(newTokens.size() - 1, newLink);
                            } else {
                                iterator.remove();
                            }
                        }
                    } else if (op.getName().equals("TJ")) {
                        // TJ contains a COSArray with COSStrings and COSFloat (padding)
                        processCOSArray(newTokens);
                    }
                }
                // save tokens to a temporary List
                if (token != null) {
                    newTokens.add(token);
                }
            }
            // save the replaced data back to the document's srteam
            PDStream newContents = new PDStream(document);
            OutputStream out = newContents.createOutputStream(COSName.FLATE_DECODE);
            ContentStreamWriter writer = new ContentStreamWriter(out);
            writer.writeTokens(newTokens);
            out.close();

            // save content
            page.setContents(newContents);

            // replace all links that have a pop-up line (It does not affect the visible text)
            List<PDAnnotation> annotations = page.getAnnotations();
            annotations.stream().filter(annotation -> annotation instanceof PDAnnotationLink)
                .forEach(annotation -> {
                    PDAnnotationLink link = (PDAnnotationLink) annotation;
                    PDAction action = link.getAction();
                    if (action instanceof PDActionURI) {
                        PDActionURI uri = (PDActionURI) action;
                        uri.setURI(newURI);
                    }
                });
        }
    }

    private static void processCOSArray(List<Object> newPdxTokens) {
        // TJ contains a COSArray with COSStrings and COSFloat (padding)
        COSArray previous = (COSArray) newPdxTokens.get(newPdxTokens.size() - 1);
        String string = "";
        for (int k = 0; k < previous.size(); k++) {
            Object arrElement = previous.getObject(k);
            if (arrElement instanceof COSString) {
                COSString cosString = (COSString) arrElement;
                String content = cosString.getString();
                string += content;
            }
        }
        // here can be any filters for checking a necessary string
        // check if string contains a necessary link
        if (string.equals("www.testlink.com")) {
            COSArray newLink = new COSArray();
            newLink.add(new COSString("test.test.com"));
            newPdxTokens.set(newPdxTokens.size() - 1, newLink);
        } else if (string.startsWith("www.testlink.com")) {
            // this code should be changed. It can have some indenting problems that depend on COSFloat values
            COSArray newLink = (COSArray) newPdxTokens.get(newPdxTokens.size() - 1);
            int size = newLink.size();
            float f = ((COSFloat) newLink.get(size - 4)).floatValue();
            for (int i = 0; i < size - 4; i++) {
                newLink.remove(0);
            }
            newLink.set(0, new COSString("test.test.com"));
            // number for indenting from right place. Should be checked.
            newLink.set(1, new COSFloat(f - 8000));
            newPdxTokens.set(newPdxTokens.size() - 1, newLink);
        }
    }

    // Check if COSDictionary has '/ProcSet [/PDF /Text]' string in the stream
    private static boolean isXobjectHasTextFieldInPdf(COSDictionary dictionary) {
        boolean isHasTextField = false;
        for (COSBase cosBase : dictionary.getValues()) {
            // go to a recursion because COSDictionary can have COSDictionaries inside
            if (cosBase instanceof COSDictionary) {
                COSDictionary cosDictionaryNew = (COSDictionary) cosBase;
                // check if '/ProcSet' has '/Text' param
                if (cosDictionaryNew.containsKey(COSName.PROC_SET)) {
                    COSBase procSet = cosDictionaryNew.getDictionaryObject(COSName.PROC_SET);
                    if (procSet instanceof COSArray) {
                        for (COSBase procSetIterator : ((COSArray) procSet)) {
                            if (procSetIterator instanceof COSName
                                && ((COSName) procSetIterator).getName().equals("Text")) {
                                return true;
                            }
                        }
                    } else if (procSet instanceof COSString && ((COSString) procSet).getString().equals("Text")) {
                        return true;
                    }
                }
                // go to the COSDictionary children
                isHasTextField = isXobjectHasTextFieldInPdf(cosDictionaryNew);
            }
        }
        return isHasTextField;
    }

    private void handleDocument(String[] args) throws IOException {
        PDDocument pdf = null;
        try {
            if (args.length > 3) {
                usage();
            } else {
                PDFHandler example = new PDFHandler();
                pdf = PDDocument.load(new File(args[0]));
                if (pdf.isEncrypted()) {
                    System.err.println("Can't support encrypted pdf file!");
                } else {
                    example.removeLinks(pdf, args[1]);
                    pdf.save(args[0]);
                }
            }
        } finally {
            if (pdf != null) {
                pdf.close();
            }
        }
    }

    /**
     * This will print out a message telling how to use this example.
     */
    private static void usage() {
        System.err.println(
            "usage: org.apache.pdfbox.examples.interactive.form.PDFHandler <pdf-file> <field-name> <field-value>");
    }

    /**
     * This will read a PDF file and set a field and then write it the pdf out
     * again. <br>
     * see usage() for commandline
     *
     * @param args command line arguments
     * @throws IOException If there is an error importing the FDF document.
     */
    public static void main(String[] args) throws IOException {
        PDFHandler setter = new PDFHandler();
        setter.handleDocument(args);
    }
}
