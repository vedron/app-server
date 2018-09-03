package com.app.common.security;


import java.lang.annotation.*;


/**
 * Desc: 忽略安全性检查
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginCompatibleSecurity {

}