package com.pep1.kitchenoffice.service;

import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.PostConstruct;

import com.pep1.kitchenoffice.data.user.User;
import org.apache.commons.lang.CharEncoding;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.jobs.ee.mail.SendMailJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

@Service
@Scope("singleton")
public class MailService {

	private static Logger log = LoggerFactory.getLogger(MailService.class);

	private static final String MAIL_TEMPLATEPATH_KEY = "mail.templatepath";

	private static final String MAIL_SENDER_KEY = "mail.sender";

	private static final String MAIL_MIME_CONTENTTYPE_KEY = "mail.mime.contenttype";

	private static final String MAIL_SMTP_HOST_KEY = "mail.smtp.host";

	public static final String TEMPLATE_EXTENSION = "vm";

	public static final String LASTNAME_KEY = "lastname";

	public static final String FIRSTNAME_KEY = "firstname";

	public static final String USERNAME_KEY = "username";

	public static final String JOB_GROUP = "sendMailJob";

	public static final String JOB_TRIGGER = "sendMailTrigger";

	public static final String MAIL_JOB_DETAIL_KEY = "sendmailJobDetail";

	@Value("${mail.propertypath}")
	public String mailpropertiesPath;

	private String contentType;

	private String templatePath;

	private String smtpHost;

	private String sender;

	private Properties mailProperties;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private SchedulerFactoryBean scheduler;

	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ...");

		try {
			Resource resource = new ClassPathResource(mailpropertiesPath);
			mailProperties = PropertiesLoaderUtils.loadProperties(resource);
			Assert.notEmpty(mailProperties);

			templatePath = mailProperties.getProperty(MAIL_TEMPLATEPATH_KEY);
			mailProperties.remove(MAIL_TEMPLATEPATH_KEY);
			Assert.hasLength(templatePath);

			sender = mailProperties.getProperty(MAIL_SENDER_KEY);
			mailProperties.remove(MAIL_SENDER_KEY);
			Assert.hasLength(sender);

			contentType = mailProperties.getProperty(MAIL_MIME_CONTENTTYPE_KEY);
			mailProperties.remove(MAIL_MIME_CONTENTTYPE_KEY);
			Assert.hasLength(contentType);

			smtpHost = mailProperties.getProperty(MAIL_SMTP_HOST_KEY);
			Assert.hasLength(smtpHost);

			if (!"/".equals(templatePath.substring(templatePath.length() - 1))) {
				templatePath += templatePath + "/";
			}

			if (log.isDebugEnabled()) {
				log.debug("Mail Properties:\n " + mailProperties);
				log.debug("Mail smtp host: " + smtpHost);
				log.debug("Mail template path: " + templatePath);
				log.debug("Content Type: " + contentType);
				log.debug("Sender: " + sender);
			}

		} catch (IOException e) {
			log.error("Error while loading mail properties", e);
		}
	}

	public void sendMailToUser(User user, String subject, String templateName, Map<String, Object> replacements) {

		Collection<User> users = new HashSet<User>();
		users.add(user);

		sendMailToUsers(users, subject, templateName, replacements);
	}

	public void sendMailToUsers(Collection<User> users, String subject, String templateName,
			Map<String, Object> replacements) {

		Assert.hasText(subject);
		Assert.hasText(templateName);

		if (replacements == null) {
			replacements = new HashMap<String, Object>();
		}

		String messageBody;

		for (User user : users) {
			try {
				// evaluate message with velocity
				messageBody = evaluateTemplate(user, templateName, replacements);
				// create and trigger mail job
				createAndTriggerMailJob(user, subject, messageBody);
			} catch (SchedulerException e) {
				log.error("Error while Mail job creation", e);
			} catch (VelocityException e) {
				log.error("Error while evaluating mail template", e);
			}
		}
	}

	private String evaluateTemplate(User user, String templateName, Map<String, Object> replacements) {

		replacements.put(USERNAME_KEY, user.getUsername());
		replacements.put(FIRSTNAME_KEY, user.getFirstName());
		replacements.put(LASTNAME_KEY, user.getLastName());

		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath + templateName,
				CharEncoding.UTF_8, replacements);
	}

	private void createAndTriggerMailJob(User user, String subject, String message) throws SchedulerException {

		JobDataMap map = new JobDataMap();
		String jobDetailName = MAIL_JOB_DETAIL_KEY + "_user_" + user.getId();

		// add all mail properties
		for (String key : mailProperties.stringPropertyNames()) {
			if (key.startsWith("mail.")) {
				map.put(key, mailProperties.getProperty(key));
			}
		}

		// put the data into the map
		map.put(SendMailJob.PROP_SMTP_HOST, smtpHost);
		map.put(SendMailJob.PROP_SENDER, sender);
		map.put(SendMailJob.PROP_CONTENT_TYPE, contentType);
		map.put(SendMailJob.PROP_RECIPIENT, user.getEmail());
		map.put(SendMailJob.PROP_SUBJECT, subject);
		map.put(SendMailJob.PROP_MESSAGE, message);

		JobKey jobKey = new JobKey(jobDetailName, JOB_GROUP);

		// create new Job
		JobDetail job = JobBuilder.newJob(SendMailJob.class).usingJobData(map).storeDurably(true).withIdentity(jobKey)
				.build();

		// create a new job trigger
		Trigger trigger = newTrigger().withIdentity(JOB_TRIGGER + UUID.randomUUID().toString(), JOB_GROUP).startNow()
				.build();

		// schedule the new job
		scheduler.getScheduler().scheduleJob(job, trigger);
	}
}
