public class Instruction {
    // ALl possible instructions
    // ADD.D, SUB.D, MUL.D, DIV.D, DADD, DSUB (register destination, 2 register operands)
    // L.D (register destination, Address)
    // S.D (Address, register source ~ operand)
    // ADDI, SUBI (register destination, register operand, immediate operand)
    // BNEZ (register source, immediate operand ~ label)
    private String operation;
    private Register destination;
    private Register operand1; // j, if using registers for operands
    private Register operand2; // k, if using registers for operands
    private double immediateValue; // if using immediate value for operand or Address for load/store
    private double issue;
    private double executionStart;
    private double executionEnd;
    private double writeResult;

    // Constructor for ADD.D, SUB.D, MUL.D, DIV.D, DADD, DSUB
    public Instruction(String operation, Register destination, Register operand1, Register operand2) {
        this.operation = operation;
        this.destination = destination;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.issue = -1;
        this.executionStart = -1;
        this.executionEnd = -1;
        this.writeResult = -1;
    }

    // Constructor for L.D and BNEZ
    public Instruction(String operation, Register destination, double immediateValue) {
        this.operation = operation;
        this.destination = destination; // destination register if L.D, source register if BNEZ
        this.immediateValue = immediateValue; // address if L.D, label if BNEZ
        this.issue = -1;
        this.executionStart = -1;
        this.executionEnd = -1;
        this.writeResult = -1;
    }

    // Constructor for S.D
    public Instruction(String operation, double immediateValue, Register operand1) {
        this.operation = operation;
        this.immediateValue = immediateValue; // address
        this.operand1 = operand1; // source register
        this.issue = -1;
        this.executionStart = -1;
        this.executionEnd = -1;
        this.writeResult = -1;
    }

    // Constructor for ADDI, SUBI
    public Instruction(String operation, Register destination, Register operand1, double immediateValue) {
        this.operation = operation;
        this.destination = destination; // destination register
        this.operand1 = operand1; // source register
        this.immediateValue = immediateValue; // immediate value
        this.issue = -1;
        this.executionStart = -1;
        this.executionEnd = -1;
        this.writeResult = -1;
    }


    // Getters and setters
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Register getDestination() {
        return destination;
    }

    public void setDestination(Register destination) {
        this.destination = destination;
    }

    public Register getOperand1() {
        return operand1;
    }

    public void setOperand1(Register operand1) {
        this.operand1 = operand1;
    }

    public Register getOperand2() {
        return operand2;
    }

    public void setOperand2(Register operand2) {
        this.operand2 = operand2;
    }

    public double getImmediateValue() {
        return immediateValue;
    }

    public void setImmediateValue(double immediateValue) {
        this.immediateValue = immediateValue;
    }

    public double getIssue() {
        return issue;
    }

    public void setIssue(double issue) {
        this.issue = issue;
    }

    public double getExecutionStart() {
        return executionStart;
    }

    public void setExecutionStart(double executionStart) {
        this.executionStart = executionStart;
    }

    public double getExecutionEnd() {
        return executionEnd;
    }

    public void setExecutionEnd(double executionEnd) {
        this.executionEnd = executionEnd;
    }

    public double getWriteResult() {
        return writeResult;
    }

    public void setWriteResult(double writeResult) {
        this.writeResult = writeResult;
    }
}
