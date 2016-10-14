/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import android.text.TextUtils;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ 
/*    */ public class ShortColumnConverter
/*    */   implements ColumnConverter<Short>
/*    */ {
/*    */   public Short getFieldValue(Cursor cursor, int index)
/*    */   {
/* 15 */     return cursor.isNull(index) ? null : Short.valueOf(cursor.getShort(index));
/*    */   }
/*    */ 
/*    */   public Short getFieldValue(String fieldStringValue)
/*    */   {
/* 20 */     if (TextUtils.isEmpty(fieldStringValue)) return null;
/* 21 */     return Short.valueOf(fieldStringValue);
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(Short fieldValue)
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
 * Qualified Name:     com.lidroid.xutils.db.converter.ShortColumnConverter
 * JD-Core Version:    0.6.0
 */