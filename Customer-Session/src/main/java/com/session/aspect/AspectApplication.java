package com.session.aspect;

import java.util.Arrays;
import static com.session.constant.ConstantUsage.*;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AspectApplication {

	@Before(value = ASPECT_EXECUTION_PATH)
	public void beforeAdvice(JoinPoint point) {
		log.info("*** inside advice1");
		Signature signature = point.getSignature();
		Object[] args = point.getArgs();
		List<Object> argsList = Arrays.asList(args);
		log.info(ASPECT_ARGS + argsList);
		log.info(ASPECT_SIGNATURE + signature);
	}

	@AfterReturning(value = ASPECT_EXECUTION_PATH, returning = "result")
	public void afterAdice(JoinPoint point, Object result) {
		log.info("*** inside advice2");
		Signature signature = point.getSignature();
		Object[] args = point.getArgs();
		List<Object> argsList = Arrays.asList(args);
		log.info(ASPECT_ARGS + argsList);
		log.info(ASPECT_SIGNATURE + signature);
		log.info(ASPECT_RESULT + result);
	}

}
