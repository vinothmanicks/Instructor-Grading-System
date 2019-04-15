package com.cannapaceus.services;

import com.cannapaceus.grader.Student;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class TrialShit {

    public static void main(String[] args) throws IOException {

            String filename = "RandomSHit.pdf";
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            String text = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();

            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 730);

            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);

            int iCounter = 0;

            System.out.println(text.length());
            System.out.println(text.length()/64);

            for(int i = 0; i < 200; i++) {
                System.out.println("Line number: " + iCounter);
                if (text.length() > 64) {
                    int iSplitter = 0;
                    while (iSplitter < (text.length()/64)) {
                        contentStream.showText(text.substring(iSplitter*64, iSplitter*64 + 63));
                        contentStream.newLine();
                        ++iSplitter;
                        ++iCounter;

                        if (iCounter == 47) {
                            contentStream.endText();
                            contentStream.close();

                            PDPage tempPage = new PDPage();
                            document.addPage(tempPage);
                            contentStream = new PDPageContentStream(document, tempPage);

                            contentStream.beginText();
                            contentStream.setLeading(14.5f);
                            contentStream.newLineAtOffset(25, 730);
                            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

                            iCounter = 0;
                        }
                    }
                    contentStream.showText(text.substring(iSplitter*64));
                }
                else
                    contentStream.showText(text);

                ++iCounter;

                if(iCounter == 47){
                    contentStream.endText();
                    contentStream.close();

                    PDPage tempPage = new PDPage();
                    document.addPage(tempPage);
                    contentStream = new PDPageContentStream(document, tempPage);

                    contentStream.beginText();
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(25, 730);
                    contentStream.setFont(PDType1Font.TIMES_BOLD, 12);

                    iCounter = 0;
                }
                contentStream.newLine();
            }

            contentStream.endText();
            contentStream.close();

            document.save(filename);
            document.close();



    }
}
