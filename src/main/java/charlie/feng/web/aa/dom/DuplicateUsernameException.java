/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Todo remove stack trace from /error page
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User name is occupied.")
public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String msg) {
        super(msg);
    }

}
