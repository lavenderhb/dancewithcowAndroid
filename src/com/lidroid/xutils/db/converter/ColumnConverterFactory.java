/*     */ package com.lidroid.xutils.db.converter;
/*     */ 
/*     */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public class ColumnConverterFactory
/*     */ {
/*  65 */   private static final ConcurrentHashMap<String, ColumnConverter> columnType_columnConverter_map = new ConcurrentHashMap();
/*     */ 
/*  67 */   static { BooleanColumnConverter booleanColumnConverter = new BooleanColumnConverter();
/*  68 */     columnType_columnConverter_map.put(Boolean.TYPE.getName(), booleanColumnConverter);
/*  69 */     columnType_columnConverter_map.put(Boolean.class.getName(), booleanColumnConverter);
/*     */ 
/*  71 */     ByteArrayColumnConverter byteArrayColumnConverter = new ByteArrayColumnConverter();
/*  72 */     columnType_columnConverter_map.put(Boolean.class.getName(), byteArrayColumnConverter);
/*     */ 
/*  74 */     ByteColumnConverter byteColumnConverter = new ByteColumnConverter();
/*  75 */     columnType_columnConverter_map.put(Byte.TYPE.getName(), byteColumnConverter);
/*  76 */     columnType_columnConverter_map.put(Byte.class.getName(), byteColumnConverter);
/*     */ 
/*  78 */     CharColumnConverter charColumnConverter = new CharColumnConverter();
/*  79 */     columnType_columnConverter_map.put(Character.TYPE.getName(), charColumnConverter);
/*  80 */     columnType_columnConverter_map.put(Character.class.getName(), charColumnConverter);
/*     */ 
/*  82 */     DateColumnConverter dateColumnConverter = new DateColumnConverter();
/*  83 */     columnType_columnConverter_map.put(java.util.Date.class.getName(), dateColumnConverter);
/*     */ 
/*  85 */     DoubleColumnConverter doubleColumnConverter = new DoubleColumnConverter();
/*  86 */     columnType_columnConverter_map.put(Double.TYPE.getName(), doubleColumnConverter);
/*  87 */     columnType_columnConverter_map.put(Double.class.getName(), doubleColumnConverter);
/*     */ 
/*  89 */     FloatColumnConverter floatColumnConverter = new FloatColumnConverter();
/*  90 */     columnType_columnConverter_map.put(Float.TYPE.getName(), floatColumnConverter);
/*  91 */     columnType_columnConverter_map.put(Float.class.getName(), floatColumnConverter);
/*     */ 
/*  93 */     IntegerColumnConverter integerColumnConverter = new IntegerColumnConverter();
/*  94 */     columnType_columnConverter_map.put(Integer.TYPE.getName(), integerColumnConverter);
/*  95 */     columnType_columnConverter_map.put(Integer.class.getName(), integerColumnConverter);
/*     */ 
/*  97 */     LongColumnConverter longColumnConverter = new LongColumnConverter();
/*  98 */     columnType_columnConverter_map.put(Long.TYPE.getName(), longColumnConverter);
/*  99 */     columnType_columnConverter_map.put(Long.class.getName(), longColumnConverter);
/*     */ 
/* 101 */     ShortColumnConverter shortColumnConverter = new ShortColumnConverter();
/* 102 */     columnType_columnConverter_map.put(Short.TYPE.getName(), shortColumnConverter);
/* 103 */     columnType_columnConverter_map.put(Short.class.getName(), shortColumnConverter);
/*     */ 
/* 105 */     SqlDateColumnConverter sqlDateColumnConverter = new SqlDateColumnConverter();
/* 106 */     columnType_columnConverter_map.put(java.sql.Date.class.getName(), sqlDateColumnConverter);
/*     */ 
/* 108 */     StringColumnConverter stringColumnConverter = new StringColumnConverter();
/* 109 */     columnType_columnConverter_map.put(String.class.getName(), stringColumnConverter);
/*     */   }
/*     */ 
/*     */   public static ColumnConverter getColumnConverter(Class columnType)
/*     */   {
/*  19 */     if (columnType_columnConverter_map.containsKey(columnType.getName()))
/*  20 */       return (ColumnConverter)columnType_columnConverter_map.get(columnType.getName());
/*  21 */     if (ColumnConverter.class.isAssignableFrom(columnType))
/*     */       try {
/*  23 */         ColumnConverter columnConverter = (ColumnConverter)columnType.newInstance();
/*  24 */         if (columnConverter != null) {
/*  25 */           columnType_columnConverter_map.put(columnType.getName(), columnConverter);
/*     */         }
/*  27 */         return columnConverter;
/*     */       }
/*     */       catch (Throwable localThrowable) {
/*     */       }
/*  31 */     return null;
/*     */   }
/*     */ 
/*     */   public static ColumnDbType getDbColumnType(Class columnType) {
/*  35 */     ColumnConverter converter = getColumnConverter(columnType);
/*  36 */     if (converter != null) {
/*  37 */       return converter.getColumnDbType();
/*     */     }
/*  39 */     return ColumnDbType.TEXT;
/*     */   }
/*     */ 
/*     */   public static void registerColumnConverter(Class columnType, ColumnConverter columnConverter) {
/*  43 */     columnType_columnConverter_map.put(columnType.getName(), columnConverter);
/*     */   }
/*     */ 
/*     */   public static boolean isSupportColumnConverter(Class columnType) {
/*  47 */     if (columnType_columnConverter_map.containsKey(columnType.getName()))
/*  48 */       return true;
/*  49 */     if (ColumnConverter.class.isAssignableFrom(columnType))
/*     */       try {
/*  51 */         ColumnConverter columnConverter = (ColumnConverter)columnType.newInstance();
/*  52 */         if (columnConverter != null) {
/*  53 */           columnType_columnConverter_map.put(columnType.getName(), columnConverter);
/*     */         }
/*  55 */         return columnConverter == null;
/*     */       }
/*     */       catch (Throwable localThrowable) {
/*     */       }
/*  59 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.ColumnConverterFactory
 * JD-Core Version:    0.6.0
 */