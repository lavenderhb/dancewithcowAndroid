/*     */ package com.lidroid.xutils.db.table;
/*     */ 
/*     */ import android.text.TextUtils;
/*     */ import com.lidroid.xutils.db.annotation.Table;
/*     */ import com.lidroid.xutils.db.converter.ColumnConverterFactory;
/*     */ import com.lidroid.xutils.util.LogUtils;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public class TableUtils
/*     */ {
/*  53 */   private static ConcurrentHashMap<String, HashMap<String, Column>> entityColumnsMap = new ConcurrentHashMap();
/*     */ 
/* 117 */   private static ConcurrentHashMap<String, Id> entityIdMap = new ConcurrentHashMap();
/*     */ 
/*     */   public static String getTableName(Class<?> entityType)
/*     */   {
/*  35 */     Table table = (Table)entityType.getAnnotation(Table.class);
/*  36 */     if ((table == null) || (TextUtils.isEmpty(table.name()))) {
/*  37 */       return entityType.getName().replace('.', '_');
/*     */     }
/*  39 */     return table.name();
/*     */   }
/*     */ 
/*     */   public static String getExecAfterTableCreated(Class<?> entityType) {
/*  43 */     Table table = (Table)entityType.getAnnotation(Table.class);
/*  44 */     if (table != null) {
/*  45 */       return table.execAfterTableCreated();
/*     */     }
/*  47 */     return null;
/*     */   }
/*     */ 
/*     */   static synchronized HashMap<String, Column> getColumnMap(Class<?> entityType)
/*     */   {
/*  58 */     if (entityColumnsMap.containsKey(entityType.getName())) {
/*  59 */       return (HashMap)entityColumnsMap.get(entityType.getName());
/*     */     }
/*     */ 
/*  62 */     HashMap columnMap = new HashMap();
/*  63 */     String primaryKeyFieldName = getPrimaryKeyFieldName(entityType);
/*  64 */     addColumns2Map(entityType, primaryKeyFieldName, columnMap);
/*  65 */     entityColumnsMap.put(entityType.getName(), columnMap);
/*     */ 
/*  67 */     return columnMap;
/*     */   }
/*     */ 
/*     */   private static void addColumns2Map(Class<?> entityType, String primaryKeyFieldName, HashMap<String, Column> columnMap) {
/*  71 */     if (Object.class.equals(entityType)) return; try
/*     */     {
/*  73 */       Field[] fields = entityType.getDeclaredFields();
/*  74 */       for (Field field : fields) {
/*  75 */         if ((ColumnUtils.isTransient(field)) || (Modifier.isStatic(field.getModifiers()))) {
/*     */           continue;
/*     */         }
/*  78 */         if (ColumnConverterFactory.isSupportColumnConverter(field.getType())) {
/*  79 */           if (!field.getName().equals(primaryKeyFieldName)) {
/*  80 */             Column column = new Column(entityType, field);
/*  81 */             if (!columnMap.containsKey(column.getColumnName()))
/*  82 */               columnMap.put(column.getColumnName(), column);
/*     */           }
/*     */         }
/*  85 */         else if (ColumnUtils.isForeign(field)) {
/*  86 */           Foreign column = new Foreign(entityType, field);
/*  87 */           if (!columnMap.containsKey(column.getColumnName()))
/*  88 */             columnMap.put(column.getColumnName(), column);
/*     */         }
/*  90 */         else if (ColumnUtils.isFinder(field)) {
/*  91 */           Finder column = new Finder(entityType, field);
/*  92 */           if (!columnMap.containsKey(column.getColumnName())) {
/*  93 */             columnMap.put(column.getColumnName(), column);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*  98 */       if (!Object.class.equals(entityType.getSuperclass()))
/*  99 */         addColumns2Map(entityType.getSuperclass(), primaryKeyFieldName, columnMap);
/*     */     }
/*     */     catch (Throwable e) {
/* 102 */       LogUtils.e(e.getMessage(), e);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Column getColumnOrId(Class<?> entityType, String columnName)
/*     */   {
/* 108 */     if (getPrimaryKeyColumnName(entityType).equals(columnName)) {
/* 109 */       return getId(entityType);
/*     */     }
/* 111 */     return (Column)getColumnMap(entityType).get(columnName);
/*     */   }
/*     */ 
/*     */   static synchronized Id getId(Class<?> entityType)
/*     */   {
/* 121 */     if (Object.class.equals(entityType)) {
/* 122 */       throw new RuntimeException("field 'id' not found");
/*     */     }
/*     */ 
/* 125 */     if (entityIdMap.containsKey(entityType.getName())) {
/* 126 */       return (Id)entityIdMap.get(entityType.getName());
/*     */     }
/*     */ 
/* 129 */     Field primaryKeyField = null;
/* 130 */     Field[] fields = entityType.getDeclaredFields();
/* 131 */     if (fields != null)
/*     */     {
/* 133 */       for (Field field : fields) {
/* 134 */         if (field.getAnnotation(com.lidroid.xutils.db.annotation.Id.class) != null) {
/* 135 */           primaryKeyField = field;
/* 136 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 140 */       if (primaryKeyField == null) {
/* 141 */         for (Field field : fields) {
/* 142 */           if (("id".equals(field.getName())) || ("_id".equals(field.getName()))) {
/* 143 */             primaryKeyField = field;
/* 144 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 150 */     if (primaryKeyField == null) {
/* 151 */       return getId(entityType.getSuperclass());
/*     */     }
/*     */ 
/* 154 */     Id id = new Id(entityType, primaryKeyField);
/* 155 */     entityIdMap.put(entityType.getName(), id);
/* 156 */     return id;
/*     */   }
/*     */ 
/*     */   private static String getPrimaryKeyFieldName(Class<?> entityType) {
/* 160 */     Id id = getId(entityType);
/* 161 */     return id == null ? null : id.getColumnField().getName();
/*     */   }
/*     */ 
/*     */   private static String getPrimaryKeyColumnName(Class<?> entityType) {
/* 165 */     Id id = getId(entityType);
/* 166 */     return id == null ? null : id.getColumnName();
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.table.TableUtils
 * JD-Core Version:    0.6.0
 */