import java.io.PrintWriter;

public class SymbolTable extends HashTable {

    @Override
    public void add(String value) {
        super.add(value);
    }

    @Override
    public String getValue(String value) {
        return super.getValue(value);
    }

    @Override
    public boolean contains(String value) {
        return super.contains(value);
    }

    @Override
    public int getPosition(String value) {
        return super.getPosition(value);
    }

    @Override
    public void print(PrintWriter out){
        super.print(out);
    }
}
