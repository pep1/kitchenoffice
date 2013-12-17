package com.gentics.kitchenoffice.service;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.jobs.ee.mail.SendMailJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.user.User;

@Service
@Scope("singleton")
public class MailService {
	
	private static Logger log = Logger.getLogger(MailService.class);
	
	public static final String TEMPLATE_EXTENSION = "vm";

	public static final String LASTNAME_KEY = "lastname";

	public static final String FIRSTNAME_KEY = "firstname";

	public static final String USERNAME_KEY = "username";

	public static final String JOB_GROUP = "sendMailJob";

	public static final String JOB_TRIGGER = "sendMailTrigger";

	public static final String MAIL_JOB_DETAIL_KEY = "sendmailJobDetail";

	public static final String CONTENT_TYPE = "text/html";

	public static final String SMTP_PORT_KEY = "mail.smtp.port";
	
	public static final String SMTP_AUTH_KEY = "mail.smtp.auth";

	@Value("${mail.templatepath}")
	private String templatePath;
	
	@Value("${mail.smtp.host}")
	private String smtpHost;
	
	@Value("${mail.smtp.port}")
	private String smtpPort;
	
	@Value("${mail.smtp.auth}")
	private String smtpAuth;

	@Value("${mail.sender}")
	private String sender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private SchedulerFactoryBean scheduler;
	
	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ...");
		
		Assert.hasLength(templatePath);
		Assert.hasLength(smtpHost);
		Assert.hasLength(smtpPort);
		Assert.hasLength(templatePath);
		
		if(!"/".equals(templatePath.substring(templatePath.length() - 1))) {
			templatePath += templatePath + "/";
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

		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath + templateName, CharEncoding.UTF_8,
				replacements);
	}

	private void createAndTriggerMailJob(User user, String subject, String message) throws SchedulerException {

		JobDataMap map = new JobDataMap();
		String jobDetailName = MAIL_JOB_DETAIL_KEY + "_user_" + user.getId();

		// put the data into the map
		map.put(SendMailJob.PROP_SMTP_HOST, smtpHost);
		map.put(SMTP_AUTH_KEY, smtpAuth);
		map.put(SMTP_PORT_KEY, smtpPort);
		map.put(SendMailJob.PROP_SENDER, sender);
		map.put(SendMailJob.PROP_CONTENT_TYPE, CONTENT_TYPE);
		map.put(SendMailJob.PROP_RECIPIENT, user.getEmail());
		map.put(SendMailJob.PROP_SUBJECT, subject);
		map.put(SendMailJob.PROP_MESSAGE, message);

		JobKey jobKey = new JobKey(jobDetailName , JOB_GROUP);

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
