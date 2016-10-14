/*    */ package com.lidroid.xutils.db.converter;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import android.text.TextUtils;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ 
/*    */ public class CharColumnConverter
/*    */   implements ColumnConverter<Character>
/*    */ {
/*    */   public Character getFieldValue(Cursor cursor, int index)
/*    */   {
/* 15 */     return cursor.isNull(index) ? null : Character.valueOf((char)cursor.getInt(index));
/*    */   }
/*    */ 
/*    */   public Character getFieldValue(String fieldStringValue)
/*    */   {
/* 20 */     if (TextUtils.isEmpty(fieldStringValue)) return null;
/* 21 */     return Character.valueOf(fieldStringValue.charAt(0));
/*    */   }
/*    */ 
/*    */   public Object fieldValue2ColumnValue(Character fieldValue)
/*    */   {
/* 26 */     if (fieldValue == null) return null;
/* 27 */     return Integer.valueOf(fieldValue.charValue());
/*    */   }
/*    */ 
/*    */   public ColumnDbType getColumnDbType()
/*    */   {
/* 32 */     return ColumnDbType.INTEGER;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.CharColumnConverter
 * JD-Core Version:    0.6.0
 */