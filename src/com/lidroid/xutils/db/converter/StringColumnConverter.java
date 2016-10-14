/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ 
/*    */ public class StringColumnConverter
/*    */   implements ColumnConverter<String>
/*    */ {
/*    */   public String getFieldValue(Cursor cursor, int index)
/*    */   {
/* 14 */     return cursor.isNull(index) ? null : cursor.getString(index);
/*    */   }
/*    */ 
/*    */   public String getFieldValue(String fieldStringValue)
/*    */   {
/* 19 */     return fieldStringValue;
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(String fieldValue)
/*    */   {
/* 24 */     return fieldValue;
/*    */   }
/*    */ 
/*    */   public ColumnDbType getColumnDbType()
/*    */   {
/* 29 */     return ColumnDbType.TEXT;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.StringColumnConverter
 * JD-Core Version:    0.6.0
 */