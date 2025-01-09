package com.game.cdcs.bot.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

	@Around("execution(* com.game.cdcs.bot.service..*(..))") // Intercetta tutti i metodi del package
																// com.game.cdcs.bot.service
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().toShortString();
		logger.info("Entering method: " + methodName);

		long startTime = System.currentTimeMillis();
		try {
			Object result = joinPoint.proceed(); // Esegue il metodo
			long elapsedTime = System.currentTimeMillis() - startTime;
			logger.info("Exiting method: " + methodName + " | Execution time: " + elapsedTime + "ms");
			return result;
		} catch (Exception e) {
			logger.error("Exception in method: " + methodName, e);
			throw e;
		}
	}
}