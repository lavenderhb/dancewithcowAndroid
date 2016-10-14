/*    */ package com.lidroid.xutils.db.sqlite;
/*    */ 
/*    */ public enum ColumnDbType
/*    */ {
/*  8 */   INTEGER("INTEGER"), REAL("REAL"), TEXT("TEXT"), BLOB("BLOB");
/*    */ 
/*    */   private String value;
/*    */ 
/* 13 */   private ColumnDbType(String value) { this.value = value;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 18 */     return this.value;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.sqlite.ColumnDbType
 * JD-Core Version:    0.6.0
 */