package com.hoanganhtuan95ptit.autobind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation dùng để đánh dấu class có thể auto bind.
 * Hỗ trợ nhiều class cùng lúc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoBind {
    Class<?>[] value();
}