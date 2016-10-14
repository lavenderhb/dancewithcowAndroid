/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import android.text.TextUtils;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ 
/*    */ public class IntegerColumnConverter
/*    */   implements ColumnConverter<Integer>
/*    */ {
/*    */   public Integer getFieldValue(Cursor cursor, int index)
/*    */   {
/* 15 */     return cursor.isNull(index) ? null : Integer.valueOf(cursor.getInt(index));
/*    */   }
/*    */ 
/*    */   public Integer getFieldValue(String fieldStringValue)
/*    */   {
/* 20 */     if (TextUtils.isEmpty(fieldStringValue)) return null;
/* 21 */     return Integer.valueOf(fieldStringValue);
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(Integer fieldValue)
/*    */   {
/* 26 */     return fieldValue;
/*    */   }
/*    */ 
/*    */   public ColumnDbType getColumnDbType()
/*    */   {
/* 31 */     return ColumnDbType.INTEGER;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.IntegerColumnConverter
 * JD-Core Version:    0.6.0
 */