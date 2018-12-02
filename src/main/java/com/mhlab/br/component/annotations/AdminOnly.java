package com.mhlab.br.component.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 관리자 기능이 필요한 부분임을 알리는 어노테이션
 *
 * Created by MHLab on 03/12/2018..
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminOnly {

    /**
     * 기본값
     * @return
     */
    boolean isAdminOnly() default true;
}
