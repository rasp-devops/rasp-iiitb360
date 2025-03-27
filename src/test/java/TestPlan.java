import ResourceOperations.*;
import java.util.ArrayList;
import java.util.List;


public class TestPlan
{
    String planName = "SamplePlan";
    List<ResOp> resOps = new ArrayList<>();
    public String getPlanName()
    {
        return planName;
    }
    public void setPlanName(String planName)
    {
        this.planName = planName;
    }
    public List<ResOp> getResOps()
    {
        return resOps;
    }
    public void addResOp(ResOp resOp)
    {
        resOps.add(resOp);
    }

}
