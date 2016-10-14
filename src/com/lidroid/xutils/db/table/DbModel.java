/*    */ package com.lidroid.xutils.db.table;
/*    */ 
/*    */ import android.text.TextUtils;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class DbModel
/*    */ {
/* 29 */   private HashMap<String, String> dataMap = new HashMap();
/*    */ 
/*    */   public String getString(String columnName) {
/* 32 */     return (String)this.dataMap.get(columnName);
/*    */   }
/*    */ 
/*    */   public int getInt(String columnName) {
/* 36 */     return Integer.valueOf((String)this.dataMap.get(columnName)).intValue();
/*    */   }
/*    */ 
/*    */   public boolean getBoolean(String columnName) {
/* 40 */     String value = (String)this.dataMap.get(columnName);
/* 41 */     if (value != null) {
/* 42 */       return value.length() == 1 ? "1".equals(value) : Boolean.valueOf(value).booleanValue();
/*    */     }
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   public double getDouble(String columnName) {
/* 48 */     return Double.valueOf((String)this.dataMap.get(columnName)).doubleValue();
/*    */   }
/*    */ 
/*    */   public float getFloat(String columnName) {
/* 52 */     return Float.valueOf((String)this.dataMap.get(columnName)).floatValue();
/*    */   }
/*    */ 
/*    */   public long getLong(String columnName) {
/* 56 */     return Long.valueOf((String)this.dataMap.get(columnName)).longValue();
/*    */   }
/*    */ 
/*    */   public java.util.Date getDate(String columnName) {
/* 60 */     long date = Long.valueOf((String)this.dataMap.get(columnName)).longValue();
/* 61 */     return new java.util.Date(date);
/*    */   }
/*    */ 
/*    */   public java.sql.Date getSqlDate(String columnName) {
/* 65 */     long date = Long.valueOf((String)this.dataMap.get(columnName)).longValue();
/* 66 */     return new java.sql.Date(date);
/*    */   }
/*    */ 
/*    */   public void add(String columnName, String valueStr) {
/* 70 */     this.dataMap.put(columnName, valueStr);
/*    */   }
/*    */ 
/*    */   public HashMap<String, String> getDataMap()
/*    */   {
/* 77 */     return this.dataMap;
/*    */   }
/*    */ 
/*    */   public boolean isEmpty(String columnName)
/*    */   {
/* 85 */     return TextUtils.isEmpty((CharSequence)this.dataMap.get(columnName));
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.table.DbModel
 * JD-Core Version:    0.6.0
 */