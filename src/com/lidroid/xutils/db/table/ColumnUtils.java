/*     */ package com.lidroid.xutils.db.table;
/*     */ 
/*     */ import android.text.TextUtils;
/*     */ import com.lidroid.xutils.db.annotation.Check;
/*     */ import com.lidroid.xutils.db.annotation.Column;
/*     */ import com.lidroid.xutils.db.annotation.Id;
/*     */ import com.lidroid.xutils.db.annotation.NotNull;
/*     */ import com.lidroid.xutils.db.annotation.Transient;
/*     */ import com.lidroid.xutils.db.annotation.Unique;
/*     */ import com.lidroid.xutils.db.converter.ColumnConverter;
/*     */ import com.lidroid.xutils.db.converter.ColumnConverterFactory;
/*     */ import com.lidroid.xutils.db.sqlite.FinderLazyLoader;
/*     */ import com.lidroid.xutils.db.sqlite.ForeignLazyLoader;
/*     */ import com.lidroid.xutils.util.LogUtils;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ColumnUtils
/*     */ {
/*  41 */   private static final HashSet<String> DB_PRIMITIVE_TYPES = new HashSet(14);
/*     */ 
/*     */   static {
/*  44 */     DB_PRIMITIVE_TYPES.add(Integer.TYPE.getName());
/*  45 */     DB_PRIMITIVE_TYPES.add(Long.TYPE.getName());
/*  46 */     DB_PRIMITIVE_TYPES.add(Short.TYPE.getName());
/*  47 */     DB_PRIMITIVE_TYPES.add(Byte.TYPE.getName());
/*  48 */     DB_PRIMITIVE_TYPES.add(Float.TYPE.getName());
/*  49 */     DB_PRIMITIVE_TYPES.add(Double.TYPE.getName());
/*     */ 
/*  51 */     DB_PRIMITIVE_TYPES.add(Integer.class.getName());
/*  52 */     DB_PRIMITIVE_TYPES.add(Long.class.getName());
/*  53 */     DB_PRIMITIVE_TYPES.add(Short.class.getName());
/*  54 */     DB_PRIMITIVE_TYPES.add(Byte.class.getName());
/*  55 */     DB_PRIMITIVE_TYPES.add(Float.class.getName());
/*  56 */     DB_PRIMITIVE_TYPES.add(Double.class.getName());
/*  57 */     DB_PRIMITIVE_TYPES.add(String.class.getName());
/*  58 */     DB_PRIMITIVE_TYPES.add(Boolean.class.getName());
/*     */   }
/*     */ 
/*     */   public static boolean isDbPrimitiveType(Class<?> fieldType) {
/*  62 */     return DB_PRIMITIVE_TYPES.contains(fieldType.getName());
/*     */   }
/*     */ 
/*     */   public static Method getColumnGetMethod(Class<?> entityType, Field field) {
/*  66 */     String fieldName = field.getName();
/*  67 */     Method getMethod = null;
/*  68 */     if (field.getType() == Boolean.TYPE) {
/*  69 */       getMethod = getBooleanColumnGetMethod(entityType, fieldName);
/*     */     }
/*  71 */     if (getMethod == null) {
/*  72 */       String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
/*     */       try {
/*  74 */         getMethod = entityType.getDeclaredMethod(methodName, new Class[0]);
/*     */       } catch (NoSuchMethodException e) {
/*  76 */         LogUtils.d(methodName + " not exist");
/*     */       }
/*     */     }
/*     */ 
/*  80 */     if ((getMethod == null) && (!Object.class.equals(entityType.getSuperclass()))) {
/*  81 */       return getColumnGetMethod(entityType.getSuperclass(), field);
/*     */     }
/*  83 */     return getMethod;
/*     */   }
/*     */ 
/*     */   public static Method getColumnSetMethod(Class<?> entityType, Field field) {
/*  87 */     String fieldName = field.getName();
/*  88 */     Method setMethod = null;
/*  89 */     if (field.getType() == Boolean.TYPE) {
/*  90 */       setMethod = getBooleanColumnSetMethod(entityType, field);
/*     */     }
/*  92 */     if (setMethod == null) {
/*  93 */       String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
/*     */       try {
/*  95 */         setMethod = entityType.getDeclaredMethod(methodName, new Class[] { field.getType() });
/*     */       } catch (NoSuchMethodException e) {
/*  97 */         LogUtils.d(methodName + " not exist");
/*     */       }
/*     */     }
/*     */ 
/* 101 */     if ((setMethod == null) && (!Object.class.equals(entityType.getSuperclass()))) {
/* 102 */       return getColumnSetMethod(entityType.getSuperclass(), field);
/*     */     }
/* 104 */     return setMethod;
/*     */   }
/*     */ 
/*     */   public static String getColumnNameByField(Field field)
/*     */   {
/* 109 */     Column column = (Column)field.getAnnotation(Column.class);
/* 110 */     if ((column != null) && (!TextUtils.isEmpty(column.column()))) {
/* 111 */       return column.column();
/*     */     }
/*     */ 
/* 114 */     Id id = (Id)field.getAnnotation(Id.class);
/* 115 */     if ((id != null) && (!TextUtils.isEmpty(id.column()))) {
/* 116 */       return id.column();
/*     */     }
/*     */ 
/* 119 */     com.lidroid.xutils.db.annotation.Foreign foreign = (com.lidroid.xutils.db.annotation.Foreign)field.getAnnotation(com.lidroid.xutils.db.annotation.Foreign.class);
/* 120 */     if ((foreign != null) && (!TextUtils.isEmpty(foreign.column()))) {
/* 121 */       return foreign.column();
/*     */     }
/*     */ 
/* 124 */     com.lidroid.xutils.db.annotation.Finder finder = (com.lidroid.xutils.db.annotation.Finder)field.getAnnotation(com.lidroid.xutils.db.annotation.Finder.class);
/* 125 */     if (finder != null) {
/* 126 */       return field.getName();
/*     */     }
/*     */ 
/* 129 */     return field.getName();
/*     */   }
/*     */ 
/*     */   public static String getForeignColumnNameByField(Field field)
/*     */   {
/* 134 */     com.lidroid.xutils.db.annotation.Foreign foreign = (com.lidroid.xutils.db.annotation.Foreign)field.getAnnotation(com.lidroid.xutils.db.annotation.Foreign.class);
/* 135 */     if (foreign != null) {
/* 136 */       return foreign.foreign();
/*     */     }
/*     */ 
/* 139 */     return field.getName();
/*     */   }
/*     */ 
/*     */   public static String getColumnDefaultValue(Field field) {
/* 143 */     Column column = (Column)field.getAnnotation(Column.class);
/* 144 */     if ((column != null) && (!TextUtils.isEmpty(column.defaultValue()))) {
/* 145 */       return column.defaultValue();
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean isTransient(Field field) {
/* 151 */     return field.getAnnotation(Transient.class) != null;
/*     */   }
/*     */ 
/*     */   public static boolean isForeign(Field field) {
/* 155 */     return field.getAnnotation(com.lidroid.xutils.db.annotation.Foreign.class) != null;
/*     */   }
/*     */ 
/*     */   public static boolean isFinder(Field field) {
/* 159 */     return field.getAnnotation(com.lidroid.xutils.db.annotation.Finder.class) != null;
/*     */   }
/*     */ 
/*     */   public static boolean isUnique(Field field) {
/* 163 */     return field.getAnnotation(Unique.class) != null;
/*     */   }
/*     */ 
/*     */   public static boolean isNotNull(Field field) {
/* 167 */     return field.getAnnotation(NotNull.class) != null;
/*     */   }
/*     */ 
/*     */   public static String getCheck(Field field)
/*     */   {
/* 175 */     Check check = (Check)field.getAnnotation(Check.class);
/* 176 */     if (check != null) {
/* 177 */       return check.value();
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */   public static Class<?> getForeignEntityType(Foreign foreignColumn)
/*     */   {
/* 185 */     Class result = foreignColumn.getColumnField().getType();
/* 186 */     if ((result.equals(ForeignLazyLoader.class)) || (result.equals(List.class))) {
/* 187 */       result = (Class)((java.lang.reflect.ParameterizedType)foreignColumn.getColumnField().getGenericType()).getActualTypeArguments()[0];
/*     */     }
/* 189 */     return result;
/*     */   }
/*     */ 
/*     */   public static Class<?> getFinderTargetEntityType(Finder finderColumn)
/*     */   {
/* 194 */     Class result = finderColumn.getColumnField().getType();
/* 195 */     if ((result.equals(FinderLazyLoader.class)) || (result.equals(List.class))) {
/* 196 */       result = (Class)((java.lang.reflect.ParameterizedType)finderColumn.getColumnField().getGenericType()).getActualTypeArguments()[0];
/*     */     }
/* 198 */     return result;
/*     */   }
/*     */ 
/*     */   public static Object convert2DbColumnValueIfNeeded(Object value)
/*     */   {
/* 203 */     Object result = value;
/* 204 */     if (value != null) {
/* 205 */       Class valueType = value.getClass();
/* 206 */       if (!isDbPrimitiveType(valueType)) {
/* 207 */         ColumnConverter converter = ColumnConverterFactory.getColumnConverter(valueType);
/* 208 */         if (converter != null)
/* 209 */           result = converter.fieldValue2ColumnValue(value);
/*     */         else {
/* 211 */           result = value;
/*     */         }
/*     */       }
/*     */     }
/* 215 */     return result;
/*     */   }
/*     */ 
/*     */   private static boolean isStartWithIs(String fieldName) {
/* 219 */     return (fieldName != null) && (fieldName.startsWith("is"));
/*     */   }
/*     */ 
/*     */   private static Method getBooleanColumnGetMethod(Class<?> entityType, String fieldName) {
/* 223 */     String methodName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
/* 224 */     if (isStartWithIs(fieldName))
/* 225 */       methodName = fieldName;
/*     */     try
/*     */     {
/* 228 */       return entityType.getDeclaredMethod(methodName, new Class[0]);
/*     */     } catch (NoSuchMethodException e) {
/* 230 */       LogUtils.d(methodName + " not exist");
/*     */     }
/* 232 */     return null;
/*     */   }
/*     */ 
/*     */   private static Method getBooleanColumnSetMethod(Class<?> entityType, Field field) {
/* 236 */     String fieldName = field.getName();
/* 237 */     String methodName = null;
/* 238 */     if (isStartWithIs(field.getName()))
/* 239 */       methodName = "set" + fieldName.substring(2, 3).toUpperCase() + fieldName.substring(3);
/*     */     else
/* 241 */       methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
/*     */     try
/*     */     {
/* 244 */       return entityType.getDeclaredMethod(methodName, new Class[] { field.getType() });
/*     */     } catch (NoSuchMethodException e) {
/* 246 */       LogUtils.d(methodName + " not exist");
/*     */     }
/* 248 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.table.ColumnUtils
 * JD-Core Version:    0.6.0
 */