package CodeWriters;
import ResourceOperations.*;

public abstract class JavaCodeWriter {
    public abstract StringBuilder giveCode(ResOp resOp);
    public static JavaCodeWriter getCodeWriter(ResOp resOp)
    {
        if (resOp.getOperation().equals("C"))
        {
            return new CreateCodeWriter();
        }
        else if(resOp.getOperation().equals("R"))
        {
            return new ReadCodeWriter();
        }
        else if(resOp.getOperation().equals("U"))
        {
            return new UpdateCodeWriter();
        }
        return null;
    }
}
