/*    */ package com.lidroid.xutils.db.sqlite;
/*    */ 
/*    */ import com.lidroid.xutils.db.table.ColumnUtils;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ public class SqlInfo
/*    */ {
/*    */   private String sql;
/*    */   private LinkedList<Object> bindArgs;
/*    */ 
/*    */   public SqlInfo()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SqlInfo(String sql)
/*    */   {
/* 31 */     this.sql = sql;
/*    */   }
/*    */ 
/*    */   public SqlInfo(String sql, Object[] bindArgs) {
/* 35 */     this.sql = sql;
/* 36 */     addBindArgs(bindArgs);
/*    */   }
/*    */ 
/*    */   public String getSql() {
/* 40 */     return this.sql;
/*    */   }
/*    */ 
/*    */   public void setSql(String sql) {
/* 44 */     this.sql = sql;
/*    */   }
/*    */ 
/*    */   public LinkedList<Object> getBindArgs() {
/* 48 */     return this.bindArgs;
/*    */   }
/*    */ 
/*    */   public Object[] getBindArgsAsArray() {
/* 52 */     if (this.bindArgs != null) {
/* 53 */       return this.bindArgs.toArray();
/*    */     }
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */   public String[] getBindArgsAsStrArray() {
/* 59 */     if (this.bindArgs != null) {
/* 60 */       String[] strings = new String[this.bindArgs.size()];
/* 61 */       for (int i = 0; i < this.bindArgs.size(); i++) {
/* 62 */         Object value = this.bindArgs.get(i);
/* 63 */         strings[i] = (value == null ? null : value.toString());
/*    */       }
/* 65 */       return strings;
/*    */     }
/* 67 */     return null;
/*    */   }
/*    */ 
/*    */   public void addBindArg(Object arg) {
/* 71 */     if (this.bindArgs == null) {
/* 72 */       this.bindArgs = new LinkedList();
/*    */     }
/*    */ 
/* 75 */     this.bindArgs.add(ColumnUtils.convert2DbColumnValueIfNeeded(arg));
/*    */   }
/*    */ 
/*    */   void addBindArgWithoutConverter(Object arg) {
/* 79 */     if (this.bindArgs == null) {
/* 80 */       this.bindArgs = new LinkedList();
/*    */     }
/*    */ 
/* 83 */     this.bindArgs.add(arg);
/*    */   }
/*    */ 
/*    */   public void addBindArgs(Object[] bindArgs) {
/* 87 */     if (bindArgs != null)
/* 88 */       for (Object arg : bindArgs)
/* 89 */         addBindArg(arg);
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.sqlite.SqlInfo
 * JD-Core Version:    0.6.0
 */