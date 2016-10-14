/*    */ package com.lidroid.xutils.exception;
/*    */ 
/*    */ public class HttpException extends BaseException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private int exceptionCode;
/*    */ 
/*    */   public HttpException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public HttpException(String detailMessage)
/*    */   {
/* 27 */     super(detailMessage);
/*    */   }
/*    */ 
/*    */   public HttpException(String detailMessage, Throwable throwable) {
/* 31 */     super(detailMessage, throwable);
/*    */   }
/*    */ 
/*    */   public HttpException(Throwable throwable) {
/* 35 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public HttpException(int exceptionCode)
/*    */   {
/* 42 */     this.exceptionCode = exceptionCode;
/*    */   }
/*    */ 
/*    */   public HttpException(int exceptionCode, String detailMessage)
/*    */   {
/* 50 */     super(detailMessage);
/* 51 */     this.exceptionCode = exceptionCode;
/*    */   }
/*    */ 
/*    */   public HttpException(int exceptionCode, String detailMessage, Throwable throwable)
/*    */   {
/* 60 */     super(detailMessage, throwable);
/* 61 */     this.exceptionCode = exceptionCode;
/*    */   }
/*    */ 
/*    */   public HttpException(int exceptionCode, Throwable throwable)
/*    */   {
/* 69 */     super(throwable);
/* 70 */     this.exceptionCode = exceptionCode;
/*    */   }
/*    */ 
/*    */   public int getExceptionCode()
/*    */   {
/* 77 */     return this.exceptionCode;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.exception.HttpException
 * JD-Core Version:    0.6.0
 */