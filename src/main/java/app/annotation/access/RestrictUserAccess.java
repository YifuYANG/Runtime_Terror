package app.annotation.access;

import app.constant.UserLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestrictUserAccess {

    UserLevel requiredLevel() default UserLevel.CLIENT;

}
