/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.jwt;

import java.io.Serial;
import java.io.Serializable;

public class JwtTokenResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 8317676219297719109L;

    private final String token;

    JwtTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
