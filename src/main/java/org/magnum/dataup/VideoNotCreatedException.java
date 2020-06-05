package org.magnum.dataup;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class VideoNotCreatedException extends IOException {

	public VideoNotCreatedException(String message) {
		super(message);
	}

}
