package nikoBlex.tools.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailManager {
//	private static final String SMTP_HOST_NAME = "auth.smtp.1and1.fr";
	//private static final String SMTP_AUTH_PWD  = "me@ndmysh0ps";
	//private static final String MAIL_POSTMASTER="postmaster@meandmyshops.com";

    
	public static void sendMail(String subject,String content,String[] destinationsArray,final String source,final String passwordSource,String hostname) throws Exception
	{
		Properties props=new java.util.Properties();
		props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", hostname);
        props.put("mail.smtp.user", source);
        props.put("mail.smtp.password", passwordSource);        
        props.put("mail.smtp.auth", "true");
        
        Authenticator authenticator = new Authenticator () {
        	public PasswordAuthentication getPasswordAuthentication(){
        		
        	return new PasswordAuthentication(source,passwordSource);
        	}
        };
        
        Session mailSession=Session.getDefaultInstance(props, authenticator);
	
        MimeMessage mailMessage=new MimeMessage(mailSession);
		mailMessage.setFrom(new InternetAddress(source));
		for(String destination:destinationsArray)
			mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
		mailMessage.setSubject(subject);
		mailMessage.setHeader("Content-Type", "text/html; charset=UTF-8" );
		mailMessage.setContent(content, "text/html");		
		Transport transport = mailSession.getTransport();		
		transport.connect();
		transport.sendMessage(mailMessage,mailMessage.getRecipients(Message.RecipientType.TO));
        transport.close();
	}
	
	
}