/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ 
/*    */ public class ByteArrayColumnConverter
/*    */   implements ColumnConverter<byte[]>
/*    */ {
/*    */   public byte[] getFieldValue(Cursor cursor, int index)
/*    */   {
/* 14 */     return cursor.isNull(index) ? null : cursor.getBlob(index);
/*    */   }
/*    */ 
/*    */   public byte[] getFieldValue(String fieldStringValue)
/*    */   {
/* 19 */     return null;
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(byte[] fieldValue)
/*    */   {
/* 24 */     return fieldValue;
/*    */   }
/*    */ 
/*    */   public ColumnDbType getColumnDbType()
/*    */   {
/* 29 */     return ColumnDbType.BLOB;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.ByteArrayColumnConverter
 * JD-Core Version:    0.6.0
 */