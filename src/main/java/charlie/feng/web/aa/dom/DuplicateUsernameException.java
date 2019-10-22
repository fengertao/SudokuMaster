/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Todo add user name into error msg
//Todo remove stack trace from /error page
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User name was occupied.")
public class DuplicateUsernameException extends RuntimeException {
}
