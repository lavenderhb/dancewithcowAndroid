/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import android.text.TextUtils;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class DateColumnConverter
/*    */   implements ColumnConverter<Date>
/*    */ {
/*    */   public Date getFieldValue(Cursor cursor, int index)
/*    */   {
/* 17 */     return cursor.isNull(index) ? null : new Date(cursor.getLong(index));
/*    */   }
/*    */ 
/*    */   public Date getFieldValue(String fieldStringValue)
/*    */   {
/* 22 */     if (TextUtils.isEmpty(fieldStringValue)) return null;
/* 23 */     return new Date(Long.valueOf(fieldStringValue).longValue());
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(Date fieldValue)
/*    */   {
/* 28 */     if (fieldValue == null) return null;
/* 29 */     return Long.valueOf(fieldValue.getTime());
/*    */   }
/*    */ 
/*    */   public ColumnDbType getColumnDbType()
/*    */   {
/* 34 */     return ColumnDbType.INTEGER;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.DateColumnConverter
 * JD-Core Version:    0.6.0
 */