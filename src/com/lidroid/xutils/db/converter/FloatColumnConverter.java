/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import android.text.TextUtils;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ 
/*    */ public class FloatColumnConverter
/*    */   implements ColumnConverter<Float>
/*    */ {
/*    */   public Float getFieldValue(Cursor cursor, int index)
/*    */   {
/* 15 */     return cursor.isNull(index) ? null : Float.valueOf(cursor.getFloat(index));
/*    */   }
/*    */ 
/*    */   public Float getFieldValue(String fieldStringValue)
/*    */   {
/* 20 */     if (TextUtils.isEmpty(fieldStringValue)) return null;
/* 21 */     return Float.valueOf(fieldStringValue);
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(Float fieldValue)
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
 * Qualified Name:     com.lidroid.xutils.db.converter.FloatColumnConverter
 * JD-Core Version:    0.6.0
 */