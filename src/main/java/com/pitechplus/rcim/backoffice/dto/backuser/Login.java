package com.pitechplus.rcim.backoffice.dto.backuser;

import lombok.*;

/**
 * Created by dgliga on 15.02.2017.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Login {

    private String login;
    private String password;
}
