public class Instruction {
    private Operations operation;
    private int destination;
    private int operand1; // j, 0 in case of load/store
    private int operand2; // k, use for effective address of load/store
    private int issue;
    private int executionStart;
    private int executionEnd;
    private int writeResult;

    public Instruction(Operations operation, int destination, int operand1, int operand2) {
        this.operation = operation;
        this.destination = destination;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.issue = -1;
        this.executionStart = -1;
        this.executionEnd = -1;
        this.writeResult = -1;
    }

    public Operations getOperation() {
        return operation;
    }

    public void setOperation(Operations operation) {
        this.operation = operation;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getOperand1() {
        return operand1;
    }

    public void setOperand1(int operand1) {
        this.operand1 = operand1;
    }

    public int getOperand2() {
        return operand2;
    }

    public void setOperand2(int operand2) {
        this.operand2 = operand2;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }

    public int getExecutionStart() {
        return executionStart;
    }

    public void setExecutionStart(int executionStart) {
        this.executionStart = executionStart;
    }

    public int getExecutionEnd() {
        return executionEnd;
    }

    public void setExecutionEnd(int executionEnd) {
        this.executionEnd = executionEnd;
    }

    public int getWriteResult() {
        return writeResult;
    }

    public void setWriteResult(int writeResult) {
        this.writeResult = writeResult;
    }

    public String toString() {
        String result = "";
        result += this.operation.toString() + " ";
        result += this.destination + " ";
        result += this.operand1 + " ";
        result += this.operand2 + " ";
        result += this.issue + " ";
        result += this.executionStart + " ";
        result += this.executionEnd + " ";
        result += this.writeResult;
        return result;
    }
}
