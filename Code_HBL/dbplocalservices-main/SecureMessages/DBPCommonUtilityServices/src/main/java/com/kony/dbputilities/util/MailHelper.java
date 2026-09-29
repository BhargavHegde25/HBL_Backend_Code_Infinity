package com.kony.dbputilities.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.sun.mail.util.MailSSLSocketFactory;

public class MailHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static Properties props;
	private static Session session;
	private static String email = new String();
	private static String password = new String();
	private static String smtpHost = new String();
	private static String smtpPort = new String();
	
	public MailHelper(DataControllerRequest dcRequest) {

		email = URLFinder.getPathUrl(URLConstants.DMS_EMAIL, dcRequest);
		password = URLFinder.getPathUrl(URLConstants.DMS_PASSWORD, dcRequest);
		smtpHost = URLFinder.getPathUrl(URLConstants.DMS_SMTP_HOST, dcRequest);
		smtpPort = URLFinder.getPathUrl(URLConstants.DMS_SMTP_PORT, dcRequest);
		
		props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");

		try {
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.smtp.host", smtpHost);

			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.trust", "*");
			props.put("mail.smtp.ssl.socketFactory", sf);
		} catch (GeneralSecurityException e) {
			alert.prepareError(e).log();
		}
		session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				try {
					return new PasswordAuthentication(email, password);
				} finally {

				}
			}
		});
	}

	public static void sendMail(String toId, String fromId, String sub, String msg) throws Exception {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromId));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toId));
		message.setSubject(sub);
		message.setText(msg);

		Transport.send(message);

	}

	public static void sendMail(String fromId, String sub, String toId, String username, String password,
			String userFirstname, String country, DataControllerRequest dcRequest) throws IOException {
		System.out.println("@@@ Inside sendMail");
		ClassLoader loader = MailHelper.class.getClassLoader();
		File image = new File(loader.getResource("RetailBankingBanner.png").getPath());

		prepareMailContentandSend(email, toId, sub, image, null, username, password, userFirstname, country,
				dcRequest);
	}

	private static void prepareMailContentandSend(String fromId, String toId, String sub, File konyLogo,
			File pdfInstructions, String username, String password, String userFirstname, String country,
			DataControllerRequest dcRequest) {
		System.out.println("@@@ Inside prepareMailContentandSend");
		MimeMessage message;
		MimeMultipart multipart;
		DataSource lg;
		ClassLoader loader = MailHelper.class.getClassLoader();
		InputStream inputStream = loader.getResourceAsStream("RetailBankingBanner.png");
		try {

			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromId));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toId));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress("neha.arora@kony.com"));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress("shujath.mohammed@kony.com"));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress("DBXDemos@Kony.com"));

			message.setSubject(sub);
			multipart = new MimeMultipart("related");

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(getEmailContent(username, password, country, dcRequest), "text/html");
			multipart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();
			lg = new ByteArrayDataSource(inputStream, "image/png");
			messageBodyPart.setDataHandler(new DataHandler(lg));
			messageBodyPart.setHeader("Content-ID", "logo-img");
			messageBodyPart.setDisposition("inline; filename=image.jpg");
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			Transport.send(message);

		} catch (MessagingException me) {
			alert.prepareError(me).log();
		} catch (Exception e) {
			alert.prepareError(e).log();
		} finally {
			message = null;
			multipart = null;
			lg = null;
			if (inputStream != null) {
        		try {
        			inputStream.close();			
        		}
        		catch(Exception e) {
        			alert.prepareError(e).log();
        		}
        	}
		}

	}

	public static String getEmailContentFromFS() {
		try (InputStream inputStream = MailHelper.class.getClassLoader()
				.getResourceAsStream("DemoUserEmailContent.html");
				ByteArrayOutputStream result = new ByteArrayOutputStream();) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			result.toString("UTF-8");
		} catch (IOException e) {
			alert.prepareError(e).log();
		}
		return null;
	}

	public static String getEmailContent(String username, String password, String country,
			DataControllerRequest dcRequest) {
		StringBuilder sb = new StringBuilder();
		Calendar now = Calendar.getInstance();
		String rbLink = URLFinder.getPathUrl(URLConstants.REDIRECTLINK, dcRequest);
		String bbLink = URLFinder.getPathUrl(URLConstants.BUSINESSBANKLINK, dcRequest);
		String loansLink = URLFinder.getPathUrl(URLConstants.LOANSLINK, dcRequest);
		String rbTestDriveLink = URLFinder.getPathUrl(URLConstants.RB_TEST_DRIVE_LINK, dcRequest);
		if (StringUtils.isBlank(rbLink)) {
			rbLink = "https://konyapps.konycloud.com/apps/KonyOLB";
		}
		if (StringUtils.isBlank(bbLink)) {
			bbLink = "https://konyapps.konycloud.com/apps/BusinessBanking";
		}
		if (StringUtils.isBlank(loansLink)) {
			loansLink = "https://konyapps.konycloud.com/apps/ConsumerLending";
		}
		if (StringUtils.isBlank(rbTestDriveLink)) {
			rbTestDriveLink = "https://konydocs.atlassian.net/wiki/spaces/ARBD/pages/989079425/Test+Drive+Retail+Banking";
		}
		int year = now.get(Calendar.YEAR);
		sb.append("<!DOCTYPE HTML>");
		sb.append("<html>");
		sb.append("<body>");
		sb.append("<table style='border:1px;margin-top:15px'>");
		sb.append(
				"<tr><td style='background-color:#284e77;color:white;margin:0px;width:100%;valign:middle;'><img src='cid:logo-img'/></td></tr>");
		sb.append(
				"<tr><td style='font-size:15px'>Congratulations! Your Infinity trial accounts have been set up and are ready for use. Use these accounts to experience the online banking through Infinity.<br></br></td></tr>");

		sb.append("<tr><td style='font-size:15px'>Explore Retail Banking on <a href='"+ rbLink + "'>browser</a> or through the native phone and tablet applications from Google Play - <a href='https://play.google.com/store/apps/details?id=com.kony.RetailBanking'>Android Phone</a> &nbsp; or, &nbsp;App Store - <a href='https://itunes.apple.com/us/app/kony-dbx-retail-banking/id1172171955?ls=1&mt=8'>iPhone</a>&nbsp;.</td></tr>");
		sb.append(
				"<tr><td style='font-size:15px'>To experience Retail Banking, use the credentials:<br></br></td></tr>");
		sb.append(
				"<tr><td style='font-size:15px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Username:</b>&nbsp;&nbsp;"
						+ username + "</td></tr>");
		sb.append(
				"<tr><td style='font-size:15px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Password:</b>&nbsp;&nbsp;"
						+ password + "</br><br></td></tr>");

//		sb.append(
//				"<tr><td style='font-size:15px'>You can also explore the Micro Business gig economy business owners can manage their micro business with basic business banking functionality through the <a href='"
//						+ rbLink + "'>browser</a>.</td></tr>");
//		sb.append(
//				"<tr><td style='font-size:15px'>To experience Micro Business Banking, use the credentials:<br></br></td></tr>");
//		sb.append(
//				"<tr><td style='font-size:15px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Username:</b>&nbsp;&nbsp;"
//						+ username + "MB" + "</td></tr>");
//		sb.append(
//				"<tr><td style='font-size:15px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Password:</b>&nbsp;&nbsp;"
//						+ password + "</br><br></td></tr>");

		if (!rbTestDriveLink.equals("NO")) {
			sb.append(
					"<tr><td style='font-size:15px;'>For more details regarding the access to advanced features such as Account Aggregation and/or Spotlight, refer to <a href='"
							+ rbTestDriveLink + "'>Retail Banking Test Drive</a>.<br></br></td></tr>");
		}
		
		if (country.equals("United States")) {
			sb.append(
					"<tr><td style='font-size:15px'>Explore Business Banking advanced features for small and medium sized businesses through your <a href='"
							+ bbLink + "'>browser</a>.</td></tr>");
			sb.append(
					"<tr><td style='font-size:15px'>To experience Business Banking, use the credentials:<br></br></td></tr>");
			sb.append(
					"<tr><td style='font-size:15px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Username:</b>&nbsp;&nbsp;"
							+ username + "SB" + "</td></tr>");
			sb.append(
					"<tr><td style='font-size:15px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Password:</b>&nbsp;&nbsp;"
							+ password + "</br><br></td></tr>");
		}
//		sb.append(
//				"<tr><td style='font-size:15px;'>Access the Consumer Unsecured Loans (Personal Loans, Credit cards and Vehicle Loans) application to explore a full digital first experience through your <a href='"
//						+ loansLink + "'>browser</a>.</td></tr>");
//		sb.append(
//				"<tr><td style='font-size:15px;'>You don't need credentials - just click on Apply Now to start your amazing loans journey.</td></tr>");
		sb.append(
				"<tr><td style='font-size:15px;'>If you have any questions or need for further support, please email us at <u style='color:mediumblue'>DBXDemos@Kony.com</u>.");
		sb.append("</td></tr>");
		sb.append("</table>");

		sb.append("<table style='border:1px;margin-top:18px'>");
		sb.append("<tr><td style='font-size:15px'>All the best, <br></br>Your Friends at Temenos</td></tr>");
		sb.append(
				"<tr><td><small>Please ignore if you got this email by mistake or if you had not registered for this site.</small><br></td></tr>");
		sb.append("<tr><td style='font-size:12px'>Copyright &copy; " + year
				+ " Infinity Retail Banking. All rights reserved.</td></tr>");
		sb.append("<tr><td style='font-size:12px'><u style='color:mediumblue'>https://dbx.kony.com</u></td></tr>");
		sb.append("</table>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}

	public static void main(String[] args) {
		try {
			MailHelper.sendMail("InfinityDemos@temenos.com", "Your Credentials ï¿½ Kony DBX Retail Banking",
					"sowmya.vandanapu@temenos.com", "rbtest123", "rbtest123", "madhava", "United States", null);
		} catch (Exception e) {
			alert.prepareError(e).log();
		}
	}
}