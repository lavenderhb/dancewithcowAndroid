/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import android.text.TextUtils;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ 
/*    */ public class DoubleColumnConverter
/*    */   implements ColumnConverter<Double>
/*    */ {
/*    */   public Double getFieldValue(Cursor cursor, int index)
/*    */   {
/* 15 */     return cursor.isNull(index) ? null : Double.valueOf(cursor.getDouble(index));
/*    */   }
/*    */ 
/*    */   public Double getFieldValue(String fieldStringValue)
/*    */   {
/* 20 */     if (TextUtils.isEmpty(fieldStringValue)) return null;
/* 21 */     return Double.valueOf(fieldStringValue);
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(Double fieldValue)
/*    */   {
/* 26 */     return fieldValue;
/*    */   }
/*    */ 
/*    */   public ColumnDbType getColumnDbType()
/*    */   {
/* 31 */     return ColumnDbType.REAL;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.DoubleColumnConverter
 * JD-Core Version:    0.6.0
 */