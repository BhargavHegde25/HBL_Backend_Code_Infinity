/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.utils;

/**
 * @author k.meiyazhagan
 */
public class PdfContentStyle {
    public boolean isHeading = false;
    String data;
    PdfGenerator.FontStyle fontStyle = PdfGenerator.FontStyle.REGULAR_DARK;

    public PdfContentStyle(String data) { // default style
        this.data = data;
    }

    public PdfContentStyle(String data, PdfGenerator.FontStyle fontStyle) {
        this.data = data;
        this.fontStyle = fontStyle;
        this.isHeading = fontStyle.equals(PdfGenerator.FontStyle.HEADING)
                || fontStyle.equals(PdfGenerator.FontStyle.SUBHEADING_BOLD_DARK);
    }
}
