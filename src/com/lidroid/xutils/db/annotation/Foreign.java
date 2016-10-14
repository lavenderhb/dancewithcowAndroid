package com.lidroid.xutils.db.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Foreign
{
  public abstract String column();

  public abstract String foreign();
}

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.annotation.Foreign
 * JD-Core Version:    0.6.0
 */