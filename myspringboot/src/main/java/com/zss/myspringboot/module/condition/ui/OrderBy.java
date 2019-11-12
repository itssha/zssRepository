package com.zss.myspringboot.module.condition.ui;

public class OrderBy {
    private String column;
    private String direction;

    public OrderBy(String column, String direction) {
        this.column = column;
        this.direction = !"DESC".equalsIgnoreCase(direction) ? "ASC" : "DESC";
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isAsc() {
        return !"DESC".equalsIgnoreCase(this.direction);
    }

    public String toString() {
        return this.column + " " + this.direction;
    }

    public String toSqlString() {
        String sqlColumn = "";
        char[] arr$ = this.column.toCharArray();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            char c = arr$[i$];
            String ch = String.valueOf(c);
            if (ch.equals(ch.toUpperCase())) {
                ch = "_" + ch.toLowerCase();
            }

            sqlColumn = sqlColumn + ch;
        }

        return sqlColumn + " " + this.direction;
    }
}
