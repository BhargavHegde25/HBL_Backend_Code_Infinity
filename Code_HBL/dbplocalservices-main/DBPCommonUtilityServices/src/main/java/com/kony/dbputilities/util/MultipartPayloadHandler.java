package com.kony.dbputilities.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FilenameUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * Utility used to handle multipart form data http requests
 * 
 * @author Venkateswara Rao Alla
 *
 */

public class MultipartPayloadHandler {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static DiskFileItemFactory FILE_ITEM_FACTORY;

    private static String FILE_UPLOAD_DIR = System.getProperty("java.io.tmpdir");

    private MultipartPayloadHandler() {
    }

    /**
     * Parses request instance passed and returns list of {@link FormItem}. Returns null in case if the argument is null
     * or underlying request is not a multipart request
     * 
     * @param dataControllerRequest
     * @return
     * @throws FileSizeLimitExceededException
     *             if any of the file being uploaded is crossing more than the configured max size
     */
    @SuppressWarnings("deprecation")
    public static List<FormItem> handleMultipart(DataControllerRequest dataControllerRequest)
            throws FileSizeLimitExceededException, InvalidFileNameException {

        if (!isMultipartRequest(dataControllerRequest)) {
            return null;
        }

        HttpServletRequest request = (HttpServletRequest) dataControllerRequest.getOriginalRequest();

        if (FILE_ITEM_FACTORY == null) {
            createItemFactory(request);
        }

        return parseMultipartBody(request);
    }

    private static synchronized void createItemFactory(HttpServletRequest request) {
        if (FILE_ITEM_FACTORY == null) {
            DiskFileItemFactory factory = new DiskFileItemFactory();

            factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
            factory.setRepository(new File(FILE_UPLOAD_DIR));

            FileCleaningTracker fileCleaningTracker = FileCleanerCleanup
                    .getFileCleaningTracker(request.getServletContext());
            factory.setFileCleaningTracker(fileCleaningTracker);
            FILE_ITEM_FACTORY = factory;
        }
    }

    /**
     * Methods that parses the multipart body
     * 
     * @param request
     * @return
     * @throws FileSizeLimitExceededException
     *             if any of the file being uploaded is crossing more than the configured max size
     */
    private static List<FormItem> parseMultipartBody(HttpServletRequest request)
            throws FileSizeLimitExceededException, InvalidFileNameException {
        try {
            List<FormItem> formItems = new ArrayList<>();
            ServletFileUpload upload = new ServletFileUpload(FILE_ITEM_FACTORY);
            upload.setFileSizeMax(10485760);
            List<FileItem> fileItems = upload.parseRequest(request);

            String fileName = null;
            String fileBaseName = null;
            String fileExtension = null;
            String fileContentType = null;
            long fileSize;
            File file = null;
            for (FileItem item : fileItems) {
                if (item.isFormField()) { // if form parameter
                    formItems.add(new FormItem(false, item.getFieldName(),
                            item.getString(StandardCharsets.UTF_8.toString()), null, null, null, 0, null));
                } else { // if file
                    fileName = FilenameUtils.getName(item.getName());
                    // Verify file name for any upload attacks
                    if (!Pattern.matches("^[A-Za-z0-9\\s]*+(.csv|.txt|.doc|.docx|.pdf|.png|.jpeg|.jpg)$",
                            FilenameUtils.getName(item.getName()).toLowerCase())) {
                        alert.prepareError("Invalid file name or Unsupported file extension "
                                + FilenameUtils.getName(item.getName())).log();
                        throw new InvalidFileNameException(
                                new Throwable("Invalid file name or Unsupported file extension"),
                                FilenameUtils.getName(item.getName()));
                    }

                    fileBaseName = FilenameUtils.getBaseName(item.getName());
                    fileExtension = FilenameUtils.getExtension(item.getName());
                    fileContentType = item.getContentType();
                    fileSize = item.getSize();
                    file = new File(FILE_UPLOAD_DIR, fileBaseName + "_" + (new Date()).getTime() + "." + fileExtension);
                    item.write(file);
                    formItems.add(new FormItem(true, item.getFieldName(), null, fileName, fileExtension,
                            fileContentType, fileSize, file));
                }
            }
            return formItems;
        } catch (FileSizeLimitExceededException fslee) {
            alert.prepareError("Uploaded File size exceeds the configured limit", fslee).log();
            throw fslee;
        } catch (InvalidFileNameException ifne) {
            alert.prepareError("Invalid file name found in the request payload", ifne).log();
            throw ifne;
        } catch (Exception e) {
            alert.prepareError("Failed to parse multipart form data in request", e).log();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static boolean isMultipartRequest(DataControllerRequest dataControllerRequest) {
        if (dataControllerRequest != null && dataControllerRequest.getOriginalRequest() != null) {
            HttpServletRequest request = (HttpServletRequest) dataControllerRequest.getOriginalRequest();
            if (ServletFileUpload.isMultipartContent(request)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Instance of this class represents a form item i.e., either a form parameter or an uploaded file
     * 
     * @author Venkateswara Rao Alla
     *
     */
    public static class FormItem {

        private boolean isFile;
        private String paramName;
        private String paramValue;
        private String fileName;
        private String fileExtension;
        private String fileContentType;
        private long fileSize;
        private File file;

        private FormItem(boolean isFile, String paramName, String paramValue, String fileName, String fileExtension,
                String fileContentType, long fileSize, File file) {
            super();
            this.isFile = isFile;
            this.paramName = paramName;
            this.paramValue = paramValue;
            this.fileName = fileName;
            this.fileExtension = fileExtension;
            this.fileContentType = fileContentType;
            this.fileSize = fileSize;
            this.file = file;
        }

        public boolean isFile() {
            return isFile;
        }

        public String getParamName() {
            return paramName;
        }

        public String getParamValue() {
            return paramValue;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileExtension() {
            return fileExtension;
        }

        public String getFileContentType() {
            return fileContentType;
        }

        public long getFileSize() {
            return fileSize;
        }

        public File getFile() {
            return file;
        }

    }

}
