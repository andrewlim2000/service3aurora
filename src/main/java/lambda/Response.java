package lambda;

import java.util.List;

/**
 *
 */
public class Response extends saaf.Response {

    private List<String> rows;

    /**
     * @return the rows
     */
    public List<String> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<String> rows) {
        this.rows = rows;
    }
}