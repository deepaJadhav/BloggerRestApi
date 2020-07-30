package com.demo.blogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandling {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@ControllerAdvice
	class GlobalControllerExceptionHandler {
		@ResponseStatus(HttpStatus.CONFLICT)  // 409
		@ExceptionHandler(DataIntegrityViolationException.class)
		public void handleConflict() {
			logger.error("Internal Server Error");
		}

		@ResponseStatus(HttpStatus.BAD_REQUEST)
		@ExceptionHandler({RequestRejectedException.class})
		ResponseEntity<?> onIllegalArgumentException() {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

		}

		@ExceptionHandler({BlogNotFoundException.class})
		ResponseEntity<?> blogNotFoundException() {
			return new ResponseEntity<>("Blog cannot be found", HttpStatus.NOT_FOUND);
		}

		@ExceptionHandler({PostNotFoundException.class})
		ResponseEntity<?> postNotFoundException() {
			return new ResponseEntity<>("Post cannot be found", HttpStatus.NOT_FOUND);
		}

		@ExceptionHandler({Exception.class})
		ResponseEntity<?> InternalErrorOrException() {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}


		@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "URL Does not match")
		ResponseEntity<?> requestNotFound() {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

		}



	}
	
}
