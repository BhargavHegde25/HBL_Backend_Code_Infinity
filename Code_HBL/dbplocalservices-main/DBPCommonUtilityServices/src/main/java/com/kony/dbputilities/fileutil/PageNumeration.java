package com.kony.dbputilities.fileutil;

import java.awt.Color;
import java.io.FileOutputStream;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

class PageNumeration extends PdfPageEventHelper {
/** The template with the total number of pages. */
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
PdfTemplate total;
public PdfPTable table;
public PdfGState gstate;
private Font normal, normalSmall,valueFont;
public BaseFont helv,baseFontB;
public Image headerImage;
private String downloadType;

public PageNumeration (String downloadType){
    try{
    	this.downloadType = downloadType;
          }
    catch (Exception e) {
        alert.prepareError(e.getMessage()).log();
    }
}


public void onOpenDocument(PdfWriter writer, Document document) {
	diagnostic.prepareDebug("open doc").log();
	
    total = writer.getDirectContent().createTemplate(30, 12);
    try {
        // initialization of the header table
      //3) Now we get a file writer instance from the class com.lowagie.text.pdf.PdfWriter
       
       
        
        //-----------------------------------common header 
      //  Image image = Image.getInstance(IOUtils.toByteArray(is));
		   
       
        table = new PdfPTable(2);
        Phrase p = new Phrase();
        Chunk ck = new Chunk("lowagie.com\n", new Font(Font.TIMES_ROMAN, 16, Font.BOLDITALIC, Color.blue));
        p.add(ck);
        ck = new Chunk("Ghent\nBelgium", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.darkGray));
        p.add(ck);
        table.getDefaultCell().setBackgroundColor(Color.WHITE);
        table.getDefaultCell().setBorderWidth(0);
        table.addCell(p);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
       // table.addCell(new Phrase(new Chunk(headerImage, 0, 0)));
        // initialization of the Graphic State
        gstate = new PdfGState();
        gstate.setFillOpacity(0.3f);
        gstate.setStrokeOpacity(0.3f);
        // initialization of the template
        total = writer.getDirectContent().createTemplate(100, 100);
        total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        // initialization of the font
        helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
        baseFontB = BaseFont.createFont("/fonts/SourceSansPro-Regular.ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
       // valueFont = new Font(baseFontB, 11,Font.NORMAL);
    }
    catch(Exception e) {
        throw new ExceptionConverter(e);
    }
}


public void onEndPage(PdfWriter writer, Document document) {
	diagnostic.prepareDebug("end page").log();
	PdfContentByte cb = writer.getDirectContent();
    table.setTotalWidth(document.right() - document.left());
    table.writeSelectedRows(0, -1, document.left(), document.getPageSize().getHeight() - 50, cb);
    // compose the footer
    
    String text = "Page " + writer.getPageNumber() + "/";
    float textSize = baseFontB.getWidthPoint(text, 11);
    float textBase;
    System.out.println("onEnd Page Page Number:"+writer.getPageNumber());
  /*  if (writer.getPageNumber() == 1) {
    	if("All".equalsIgnoreCase(this.downloadType)) {
    		textBase = document.top()-230;
    	
    	}else if("LoanSchedule".equalsIgnoreCase(this.downloadType)) {
    		textBase = document.top()-285;
    	}else if("Other".equalsIgnoreCase(this.downloadType)) {
    		textBase = document.top()-195;
    	}else if("Combined".equalsIgnoreCase(this.downloadType)) {
    		textBase = document.top()-260;
    	}
    	else {
    		textBase = document.top()-205;
    	}
    }
    else {
    	if("Combined".equalsIgnoreCase(this.downloadType)) {
    		textBase = document.top()-70;
    	}else {
    		textBase = document.top()-145;
    	}
    	
    }*/
    System.out.println("Document bottom:"+document.bottom());
    textBase = document.bottom()-10;
    	
    
    cb.beginText();
    cb.setFontAndSize(baseFontB, 11);
   
   
        cb.setTextMatrix(document.right()-45, textBase);
        cb.showText(text);
        cb.endText();
        cb.addTemplate(total, document.right()-45 + textSize, textBase);
    
   
    }
	


public void onCloseDocument(PdfWriter writer, Document document) {
   
	  System.out.println("onCloseDocument Page Page Number:"+writer.getPageNumber());
	 total.beginText();
	 total.setFontAndSize(baseFontB, 11);
	 total.setTextMatrix(0, 0);
	 total.showText(Integer.toString(writer.getPageNumber() - 1));
	 total.endText();
	 total.sanityCheck();
	}
	
}  
