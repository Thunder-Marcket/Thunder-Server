
package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String userName;
    private String phoneNumber;
    private String birth;
}
