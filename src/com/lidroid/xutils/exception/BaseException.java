/*    */ package com.lidroid.xutils.exception;
/*    */ 
/*    */ public class BaseException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public BaseException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public BaseException(String detailMessage)
/*    */   {
/* 30 */     super(detailMessage);
/*    */   }
/*    */ 
/*    */   public BaseException(String detailMessage, Throwable throwable) {
/* 34 */     super(detailMessage, throwable);
/*    */   }
/*    */ 
/*    */   public BaseException(Throwable throwable) {
/* 38 */     super(throwable);
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.exception.BaseException
 * JD-Core Version:    0.6.0
 */