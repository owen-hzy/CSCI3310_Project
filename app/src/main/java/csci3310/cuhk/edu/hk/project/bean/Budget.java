package csci3310.cuhk.edu.hk.project.bean;

import android.database.Cursor;

import csci3310.cuhk.edu.hk.project.db.BudgetTable;

public class Budget {

    public int id;
    public String name;
    public double value;

    public Budget() {

    }

    public Budget(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public static Budget fromCursor(Cursor cursor) {
        Budget budget = new Budget();

        budget.name = cursor.getString(cursor.getColumnIndex(BudgetTable.COLUMN_NAME));
        budget.id = cursor.getInt(cursor.getColumnIndex(BudgetTable._ID));
        budget.value = cursor.getDouble(cursor.getColumnIndex(BudgetTable.COLUMN_VALUE));

        return budget;
    }

}
