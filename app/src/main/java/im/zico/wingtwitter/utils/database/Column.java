
package im.zico.wingtwitter.utils.database;

public class Column {

    public static enum Constraint {
        NOTHING("NOTHING"),
        UNIQUE("UNIQUE"), NOT("NOT"), NULL("NULL"), NOTNULL("NOT NULL"), CHECK("CHECK"), FOREIGN_KEY("FOREIGN KEY"),
        PRIMARY_KEY("PRIMARY KEY");

        private String value;

        private Constraint(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static enum DataType {
        NULL("NULL"), INTEGER("INTEGER"), REAL("REAL"), TEXT("TEXT"),
        BLOB("BLOB"), INTEGER_1("INTEGER(1)");

        private String value;

        private DataType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private String mColumnName;

    private Constraint mConstraint;

    private DataType mDataType;

    public Column(String columnName, Constraint constraint, DataType dataType) {
        mColumnName = columnName;
        mConstraint = constraint;
        mDataType = dataType;
    }

    public String getColumnName() {
        return mColumnName;
    }

    public Constraint getConstraint() {
        return mConstraint;
    }

    public DataType getDataType() {
        return mDataType;
    }
}
