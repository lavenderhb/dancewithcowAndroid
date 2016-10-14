/*    */ package com.lidroid.xutils.exception;
/*    */ 
/*    */ public class DbException extends BaseException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public DbException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public DbException(String detailMessage)
/*    */   {
/* 25 */     super(detailMessage);
/*    */   }
/*    */ 
/*    */   public DbException(String detailMessage, Throwable throwable) {
/* 29 */     super(detailMessage, throwable);
/*    */   }
/*    */ 
/*    */   public DbException(Throwable throwable) {
/* 33 */     super(throwable);
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.exception.DbException
 * JD-Core Version:    0.6.0
 */