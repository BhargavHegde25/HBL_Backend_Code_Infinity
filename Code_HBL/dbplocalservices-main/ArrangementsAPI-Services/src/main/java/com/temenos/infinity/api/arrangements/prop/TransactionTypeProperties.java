package com.temenos.infinity.api.arrangements.prop;

import java.io.InputStream;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
/**
 * <p>
 * Class to load and access values of transactiontype.properties file
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class TransactionTypeProperties {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private static final Properties PROPS = loadProps();

    private TransactionTypeProperties() {
        // Private Constructor
    }

    private static Properties loadProps() {
        Properties properties = new Properties();
        try (InputStream inputStream = TransactionTypeProperties.class.getClassLoader()
                .getResourceAsStream("transactiontype.properties")) {
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            alert.prepareError("Error while loading transactiontype.properties", e).log();
        }
        return properties;
    }

    /**
     * Returns the transaction type associated with this key
     * 
     * @param key
     * @return
     */
    public static String getValue(String transactionTypeCode) {
        return PROPS.getProperty(transactionTypeCode);
    }

}
