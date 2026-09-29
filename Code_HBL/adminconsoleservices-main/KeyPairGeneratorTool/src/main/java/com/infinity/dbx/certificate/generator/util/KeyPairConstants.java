/**
 * 
 */
package com.infinity.dbx.certificate.generator.util;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public interface KeyPairConstants {
	String PARAM_CONFIG_PROPERTY_FILE_PATH = "ConfigPropertyFilePath";
	String PARAM_OUTPUT_FOLDER_PATH = "db.OutputFolderPath";
	String TOOL_USAGE = "usage :\n --ConfigPropertyFilePath - configuration properties file location \n --OutputFolderPath - output folder to generate public key / log files (optional) ";
	String NO_ARGS_PROVIDED = "Mandatory arguments missing.\n"+TOOL_USAGE;
	String MISSING_CONFIG_PROPERTY_FILE_PATH = "the config file path is missing.";
	String INVALID_ARGS = "invalid arguments provided \n"+TOOL_USAGE;
	String INVALID_FILE_EXTENSION = "invalid file/the provided file format in not valid. please provide the file with .properties extension";
	String READ_ACCESS_DENIED = "the provided file does not have the read access.";
	String DB_USERNAME = "dbusername";
	String DB_PASSWORD = "dbpassword";
	String DB_URL = "dburl";
	String DB_DRIVER_CLASSNAME = "driverClassname";
	String ENCRYPTION_KEY = "db.encryptionKey";
	String BLANK_DB_USERNAME = "DB username is not provided/empty";
	String BLANK_DB_PASSWORD = "DB password is not provided/empty";
	String BLANK_DB_URL = "DB URL is not provided/empty";
	String BLANK_ENCRYPTION_KEY = "encryption key is not provided/empty";
	
	String KEY_ALGO = "RSA";
	int KEY_INITIALIZATION_SIZE = 2048;
	String PUBLIC_KEY_FILE_NAME = "publicKey.file";
	String PRIVATE_KEY_FILE_NAME = "privateKey.file";
	String ENCRYPTED_PRIVATE_KEY_FILE_NAME = "encryptedPrivateKey.file";
	
	String BACKEND_NAME = "T24";
	String CERT_NAME = "AUTH";
	
	String FINAL_MSG = "tool run success. please check the KeyPair folder for key pair & success log file ";
	
	String CERT_GEN_TOOL = "KeyPair-GEN-TOOL";
	String FINAL_ERR_MSG = "tool run failed. please check KeyPair folder log file for detailed error";
	
}
