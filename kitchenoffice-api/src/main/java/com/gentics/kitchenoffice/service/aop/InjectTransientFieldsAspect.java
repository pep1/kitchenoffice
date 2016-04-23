package com.gentics.kitchenoffice.service.aop;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.Image;
import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.service.ImageService;
import com.gentics.kitchenoffice.service.StorageService;

@Aspect
@Component
@Scope("singleton")
public class InjectTransientFieldsAspect {

	private static Logger log = LoggerFactory.getLogger(InjectTransientFieldsAspect.class);

	@Autowired
	private StorageService storageService;

	@Autowired
	private ImageService imageService;

	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	@Pointcut("execution(* com.gentics.kitchenoffice.service.*Service.*(..))")
	public void methodsInServicePackage() {
	};

	@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void methodsHavingTransactionalAnnotation() {
	};

	@SuppressWarnings("unchecked")
	@Around("methodsInServicePackage() && methodsHavingTransactionalAnnotation()")
	private Object injectTransientFields(ProceedingJoinPoint pjp) throws Throwable {
		Object retVal = pjp.proceed();

		Collection<?> returnCollection = null;

		if (retVal instanceof Collection<?> && ((Collection<?>) retVal).size() > 0) {
			returnCollection = ((Collection<?>) retVal);

		} else if (retVal instanceof Page && ((Page<?>) retVal).getContent().size() > 0) {
			returnCollection = ((Page<?>) retVal).getContent();
		}

		if (returnCollection != null) {
			// check type of collection values
			Object object = returnCollection.iterator().next();

			if (object instanceof Event) {
				for (Event event : ((Collection<Event>) returnCollection)) {
					injectTransientFields(event.getLocation());
				}
			}

			if (object instanceof Location) {
				injectTransientFields((Collection<Location>) returnCollection);
			}
		} else {
			// handle single results
			if (retVal instanceof Location) {
				injectTransientFields((Location) retVal);
			}

			if (retVal instanceof Event) {
				injectTransientFields(((Event) retVal).getLocation());
			}
		}

		return retVal;
	};

	@SuppressWarnings("unused")
	private Class<?> getReturnType(ProceedingJoinPoint pjp) {
		Signature signature = pjp.getSignature();
		Class<?> type = null;

		if (signature instanceof MethodSignature) {
			type = ((MethodSignature) signature).getReturnType();
		}

		return type;
	}

	private void injectTransientFields(Location location) {
		Set<Location> helper = new HashSet<Location>();
		helper.add(location);
		injectTransientFields(helper);
	}

	private void injectTransientFields(Collection<Location> locations) {
		Image image;

		for (Location location : locations) {
			if (location != null) {
				image = location.getImage();

				// image url
				if (image != null) {
					image.setUrl(storageService.getStorage().getStorableUrl(image));
				}
			}
		}

	}

}
