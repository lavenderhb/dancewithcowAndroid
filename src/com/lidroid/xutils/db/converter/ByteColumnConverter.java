/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import android.text.TextUtils;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ 
/*    */ public class ByteColumnConverter
/*    */   implements ColumnConverter<Byte>
/*    */ {
/*    */   public Byte getFieldValue(Cursor cursor, int index)
/*    */   {
/* 15 */     return cursor.isNull(index) ? null : Byte.valueOf((byte)cursor.getInt(index));
/*    */   }
/*    */ 
/*    */   public Byte getFieldValue(String fieldStringValue)
/*    */   {
/* 20 */     if (TextUtils.isEmpty(fieldStringValue)) return null;
/* 21 */     return Byte.valueOf(fieldStringValue);
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(Byte fieldValue)
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
 * Qualified Name:     com.lidroid.xutils.db.converter.ByteColumnConverter
 * JD-Core Version:    0.6.0
 */