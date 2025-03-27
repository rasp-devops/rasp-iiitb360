package ResourceOperations;


import java.util.Map;

public abstract class ResOp {
    private String resource;
    private String operation;
    public Map<String,Object> expectedValues;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public void setExpectedValues(Map<String,Object> expectedValues) {
        this.expectedValues = expectedValues;
    }

    public Map<String,Object> getExpectedValues()
    {
        return this.expectedValues;
    }
}
