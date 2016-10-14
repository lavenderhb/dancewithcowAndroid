/*     */ package com.lidroid.xutils.db.sqlite;
/*     */ 
/*     */ import android.text.TextUtils;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DbModelSelector
/*     */ {
/*     */   private String[] columnExpressions;
/*     */   private String groupByColumnName;
/*     */   private WhereBuilder having;
/*     */   private Selector selector;
/*     */ 
/*     */   private DbModelSelector(Class<?> entityType)
/*     */   {
/*  34 */     this.selector = Selector.from(entityType);
/*     */   }
/*     */ 
/*     */   protected DbModelSelector(Selector selector, String groupByColumnName) {
/*  38 */     this.selector = selector;
/*  39 */     this.groupByColumnName = groupByColumnName;
/*     */   }
/*     */ 
/*     */   protected DbModelSelector(Selector selector, String[] columnExpressions) {
/*  43 */     this.selector = selector;
/*  44 */     this.columnExpressions = columnExpressions;
/*     */   }
/*     */ 
/*     */   public static DbModelSelector from(Class<?> entityType) {
/*  48 */     return new DbModelSelector(entityType);
/*     */   }
/*     */ 
/*     */   public DbModelSelector where(WhereBuilder whereBuilder) {
/*  52 */     this.selector.where(whereBuilder);
/*  53 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector where(String columnName, String op, Object value) {
/*  57 */     this.selector.where(columnName, op, value);
/*  58 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector and(String columnName, String op, Object value) {
/*  62 */     this.selector.and(columnName, op, value);
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector and(WhereBuilder where) {
/*  67 */     this.selector.and(where);
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector or(String columnName, String op, Object value) {
/*  72 */     this.selector.or(columnName, op, value);
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector or(WhereBuilder where) {
/*  77 */     this.selector.or(where);
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector expr(String expr) {
/*  82 */     this.selector.expr(expr);
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector expr(String columnName, String op, Object value) {
/*  87 */     this.selector.expr(columnName, op, value);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector groupBy(String columnName) {
/*  92 */     this.groupByColumnName = columnName;
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector having(WhereBuilder whereBuilder) {
/*  97 */     this.having = whereBuilder;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector select(String[] columnExpressions) {
/* 102 */     this.columnExpressions = columnExpressions;
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector orderBy(String columnName) {
/* 107 */     this.selector.orderBy(columnName);
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector orderBy(String columnName, boolean desc) {
/* 112 */     this.selector.orderBy(columnName, desc);
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector limit(int limit) {
/* 117 */     this.selector.limit(limit);
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector offset(int offset) {
/* 122 */     this.selector.offset(offset);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   public Class<?> getEntityType() {
/* 127 */     return this.selector.getEntityType();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 132 */     StringBuffer result = new StringBuffer();
/* 133 */     result.append("SELECT ");
/* 134 */     if ((this.columnExpressions != null) && (this.columnExpressions.length > 0)) {
/* 135 */       for (int i = 0; i < this.columnExpressions.length; i++) {
/* 136 */         result.append(this.columnExpressions[i]);
/* 137 */         result.append(",");
/*     */       }
/* 139 */       result.deleteCharAt(result.length() - 1);
/*     */     }
/* 141 */     else if (!TextUtils.isEmpty(this.groupByColumnName)) {
/* 142 */       result.append(this.groupByColumnName);
/*     */     } else {
/* 144 */       result.append("*");
/*     */     }
/*     */ 
/* 147 */     result.append(" FROM ").append(this.selector.tableName);
/* 148 */     if ((this.selector.whereBuilder != null) && (this.selector.whereBuilder.getWhereItemSize() > 0)) {
/* 149 */       result.append(" WHERE ").append(this.selector.whereBuilder.toString());
/*     */     }
/* 151 */     if (!TextUtils.isEmpty(this.groupByColumnName)) {
/* 152 */       result.append(" GROUP BY ").append(this.groupByColumnName);
/* 153 */       if ((this.having != null) && (this.having.getWhereItemSize() > 0)) {
/* 154 */         result.append(" HAVING ").append(this.having.toString());
/*     */       }
/*     */     }
/* 157 */     if (this.selector.orderByList != null) {
/* 158 */       for (int i = 0; i < this.selector.orderByList.size(); i++) {
/* 159 */         result.append(" ORDER BY ").append(((Selector.OrderBy)this.selector.orderByList.get(i)).toString());
/*     */       }
/*     */     }
/* 162 */     if (this.selector.limit > 0) {
/* 163 */       result.append(" LIMIT ").append(this.selector.limit);
/* 164 */       result.append(" OFFSET ").append(this.selector.offset);
/*     */     }
/* 166 */     return result.toString();
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.sqlite.DbModelSelector
 * JD-Core Version:    0.6.0
 */