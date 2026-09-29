package com.kony.dbputilities.fileutil;

public class FileGeneratorFactory {
    private FileGeneratorFactory() {
    }

    public static FileGenerator getFileGenerator(String fileType) {
        FileGenerator generator;

        switch (fileType) {
            case "csv":
                generator = new CSVGenerator();
                break;
            case "xlsx":
            case "xls":
                generator = new ExcelGenerator();
                break;
            case "qbo":
                generator = new QBOGenerator();
                break;
            case "qfx":
                generator = new QFXGenerator();
                break;
            case "pdf":
                generator = new PDFGenerator();
                break;
            default:
                generator = null;
        }
        return generator;
    }
}
