package ResourceOperations;
import java.util.*;

public class ReadResOp extends ResOp{
    private Map<String,String> filters = null;
    private String queryId;
    private String name;

    public ReadResOp(String resource, String name, String operation, Map<String,String> filters, Map<String,Object> expectedValues){
        super.setResource(resource);
        super.setOperation(operation);
        this.filters = filters;
        this.name = name;
        super.setExpectedValues(expectedValues);
//        this.queryId = queryId;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String,String> filters){
        this.filters = filters;
    }
}
