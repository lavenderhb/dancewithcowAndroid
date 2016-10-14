/*     */ package com.lidroid.xutils.db.sqlite;
/*     */ 
/*     */ import android.text.TextUtils;
/*     */ import com.lidroid.xutils.db.converter.ColumnConverterFactory;
/*     */ import com.lidroid.xutils.db.table.ColumnUtils;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class WhereBuilder
/*     */ {
/*     */   private final List<String> whereItems;
/*     */ 
/*     */   private WhereBuilder()
/*     */   {
/*  37 */     this.whereItems = new ArrayList();
/*     */   }
/*     */ 
/*     */   public static WhereBuilder b()
/*     */   {
/*  46 */     return new WhereBuilder();
/*     */   }
/*     */ 
/*     */   public static WhereBuilder b(String columnName, String op, Object value)
/*     */   {
/*  58 */     WhereBuilder result = new WhereBuilder();
/*  59 */     result.appendCondition(null, columnName, op, value);
/*  60 */     return result;
/*     */   }
/*     */ 
/*     */   public WhereBuilder and(String columnName, String op, Object value)
/*     */   {
/*  72 */     appendCondition(this.whereItems.size() == 0 ? null : "AND", columnName, op, value);
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   public WhereBuilder or(String columnName, String op, Object value)
/*     */   {
/*  85 */     appendCondition(this.whereItems.size() == 0 ? null : "OR", columnName, op, value);
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   public WhereBuilder expr(String expr) {
/*  90 */     this.whereItems.add(" " + expr);
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   public WhereBuilder expr(String columnName, String op, Object value) {
/*  95 */     appendCondition(null, columnName, op, value);
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */   public int getWhereItemSize() {
/* 100 */     return this.whereItems.size();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 105 */     if (this.whereItems.size() == 0) {
/* 106 */       return "";
/*     */     }
/* 108 */     StringBuilder sb = new StringBuilder();
/* 109 */     for (String item : this.whereItems) {
/* 110 */       sb.append(item);
/*     */     }
/* 112 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private void appendCondition(String conj, String columnName, String op, Object value) {
/* 116 */     StringBuilder sqlSb = new StringBuilder();
/*     */ 
/* 118 */     if (this.whereItems.size() > 0) {
/* 119 */       sqlSb.append(" ");
/*     */     }
/*     */ 
/* 123 */     if (!TextUtils.isEmpty(conj)) {
/* 124 */       sqlSb.append(conj + " ");
/*     */     }
/*     */ 
/* 128 */     sqlSb.append(columnName);
/*     */ 
/* 131 */     if ("!=".equals(op))
/* 132 */       op = "<>";
/* 133 */     else if ("==".equals(op)) {
/* 134 */       op = "=";
/*     */     }
/*     */ 
/* 138 */     if (value == null) {
/* 139 */       if ("=".equals(op))
/* 140 */         sqlSb.append(" IS NULL");
/* 141 */       else if ("<>".equals(op))
/* 142 */         sqlSb.append(" IS NOT NULL");
/*     */       else
/* 144 */         sqlSb.append(" " + op + " NULL");
/*     */     }
/*     */     else {
/* 147 */       sqlSb.append(" " + op + " ");
/*     */ 
/* 149 */       if ("IN".equalsIgnoreCase(op)) {
/* 150 */         Iterable items = null;
/*     */         int i;
/* 151 */         if ((value instanceof Iterable)) {
/* 152 */           items = (Iterable)value;
/* 153 */         } else if (value.getClass().isArray()) {
/* 154 */           ArrayList arrayList = new ArrayList();
/* 155 */           int len = Array.getLength(value);
/* 156 */           for (i = 0; i < len; i++) {
/* 157 */             arrayList.add(Array.get(value, i));
/*     */           }
/* 159 */           items = arrayList;
/*     */         }
/* 161 */         if (items != null) {
/* 162 */           StringBuffer stringBuffer = new StringBuffer("(");
                Iterator i0;
/* 163 */           for (i0 = items.iterator(); i0.hasNext(); ) { Object item = i0.next();
/* 164 */             Object itemColValue = ColumnUtils.convert2DbColumnValueIfNeeded(item);
/* 165 */             if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(itemColValue.getClass()))) {
/* 166 */               String valueStr = itemColValue.toString();
/* 167 */               if (valueStr.indexOf('\'') != -1) {
/* 168 */                 valueStr = valueStr.replace("'", "''");
/*     */               }
/* 170 */               stringBuffer.append("'" + valueStr + "'");
/*     */             } else {
/* 172 */               stringBuffer.append(itemColValue);
/*     */             }
/* 174 */             stringBuffer.append(",");
/*     */           }
/* 176 */           stringBuffer.deleteCharAt(stringBuffer.length() - 1);
/* 177 */           stringBuffer.append(")");
/* 178 */           sqlSb.append(stringBuffer.toString());
/*     */         } else {
/* 180 */           throw new IllegalArgumentException("value must be an Array or an Iterable.");
/*     */         }
/* 182 */       } else if ("BETWEEN".equalsIgnoreCase(op)) {
/* 183 */         Iterable items = null;
/* 184 */         if ((value instanceof Iterable)) {
/* 185 */           items = (Iterable)value;
/* 186 */         } else if (value.getClass().isArray()) {
/* 187 */           ArrayList arrayList = new ArrayList();
/* 188 */           int len = Array.getLength(value);
/* 189 */           for (int i = 0; i < len; i++) {
/* 190 */             arrayList.add(Array.get(value, i));
/*     */           }
/* 192 */           items = arrayList;
/*     */         }
/* 194 */         if (items != null) {
/* 195 */           Iterator iterator = items.iterator();
/* 196 */           if (!iterator.hasNext()) throw new IllegalArgumentException("value must have tow items.");
/* 197 */           Object start = iterator.next();
/* 198 */           if (!iterator.hasNext()) throw new IllegalArgumentException("value must have tow items.");
/* 199 */           Object end = iterator.next();
/*     */ 
/* 201 */           Object startColValue = ColumnUtils.convert2DbColumnValueIfNeeded(start);
/* 202 */           Object endColValue = ColumnUtils.convert2DbColumnValueIfNeeded(end);
/*     */ 
/* 204 */           if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(startColValue.getClass()))) {
/* 205 */             String startStr = startColValue.toString();
/* 206 */             if (startStr.indexOf('\'') != -1) {
/* 207 */               startStr = startStr.replace("'", "''");
/*     */             }
/* 209 */             String endStr = endColValue.toString();
/* 210 */             if (endStr.indexOf('\'') != -1) {
/* 211 */               endStr = endStr.replace("'", "''");
/*     */             }
/* 213 */             sqlSb.append("'" + startStr + "'");
/* 214 */             sqlSb.append(" AND ");
/* 215 */             sqlSb.append("'" + endStr + "'");
/*     */           } else {
/* 217 */             sqlSb.append(startColValue);
/* 218 */             sqlSb.append(" AND ");
/* 219 */             sqlSb.append(endColValue);
/*     */           }
/*     */         } else {
/* 222 */           throw new IllegalArgumentException("value must be an Array or an Iterable.");
/*     */         }
/*     */       } else {
/* 225 */         value = ColumnUtils.convert2DbColumnValueIfNeeded(value);
/* 226 */         if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(value.getClass()))) {
/* 227 */           String valueStr = value.toString();
/* 228 */           if (valueStr.indexOf('\'') != -1) {
/* 229 */             valueStr = valueStr.replace("'", "''");
/*     */           }
/* 231 */           sqlSb.append("'" + valueStr + "'");
/*     */         } else {
/* 233 */           sqlSb.append(value);
/*     */         }
/*     */       }
/*     */     }
/* 237 */     this.whereItems.add(sqlSb.toString());
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.sqlite.WhereBuilder
 * JD-Core Version:    0.6.0
 */