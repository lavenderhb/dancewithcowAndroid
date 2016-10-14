/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import android.text.TextUtils;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ import java.sql.Date;
/*    */ 
/*    */ public class SqlDateColumnConverter
/*    */   implements ColumnConverter<Date>
/*    */ {
/*    */   public Date getFieldValue(Cursor cursor, int index)
/*    */   {
/* 15 */     return cursor.isNull(index) ? null : new Date(cursor.getLong(index));
/*    */   }
/*    */ 
/*    */   public Date getFieldValue(String fieldStringValue)
/*    */   {
/* 20 */     if (TextUtils.isEmpty(fieldStringValue)) return null;
/* 21 */     return new Date(Long.valueOf(fieldStringValue).longValue());
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(Date fieldValue)
/*    */   {
/* 26 */     if (fieldValue == null) return null;
/* 27 */     return Long.valueOf(fieldValue.getTime());
/*    */   }
/*    */ 
/*    */   public ColumnDbType getColumnDbType()
/*    */   {
/* 32 */     return ColumnDbType.INTEGER;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.SqlDateColumnConverter
 * JD-Core Version:    0.6.0
 */