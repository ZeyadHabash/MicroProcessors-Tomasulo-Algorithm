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
    private Double immediateValue; // if using immediate value for operand or Address for load/store
    private int issue;
    private int executionStart;
    private int executionEnd;
    private int writeResult;

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

    // Constructor for L.D
    public Instruction(String operation, Register destination, Double immediateValue) {
        this.operation = operation;
        this.destination = destination; // destination register if L.D, source register if BNEZ
        this.immediateValue = immediateValue; // address if L.D, label if BNEZ
        this.issue = -1;
        this.executionStart = -1;
        this.executionEnd = -1;
        this.writeResult = -1;
    }

    // Constructor for S.D and BNEZ
    public Instruction(String operation, Double immediateValue, Register operand1) {
        this.operation = operation;
        this.immediateValue = immediateValue; // address
        this.operand1 = operand1; // source register
        this.issue = -1;
        this.executionStart = -1;
        this.executionEnd = -1;
        this.writeResult = -1;
    }

    // Constructor for ADDI, SUBI
    public Instruction(String operation, Register destination, Register operand1, Double immediateValue) {
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

    public Double getImmediateValue() {
        return immediateValue;
    }

    public void setImmediateValue(Double immediateValue) {
        this.immediateValue = immediateValue;
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
        String res = "";
        res += this.operation + "\t";
        if (this.destination != null) {
            res += this.destination.getLabel() + "\t";
        } else if (this.operation.equals("BNEZ")) {
            res += this.immediateValue + "\t";
        }

        if (this.operation.equals("L.D") || this.operation.equals("BNEZ")) {
            res += 0 + "\t";
        }
        if (this.operand1 != null) {
            res += this.operand1.getLabel() + "\t";
        }
        if (this.operation.equals("S.D"))
            res += 0 + "\t";
        if (this.operand2 != null) {
            res += this.operand2.getLabel() + "\t";
        }
        if (this.operation.equals("L.D") || this.operation.equals("S.D") || this.operation.equals("ADDI") || this.operation.equals("SUBI")) {
            res += this.immediateValue + "\t";
        }
        if (this.issue >= 0) {
            res += this.issue + "\t";
            if (this.executionStart >= 0) {
                res += this.executionStart + "...";
                if (this.executionEnd >= executionStart) {
                    res += this.executionEnd + "\t";
                    if (this.writeResult >= executionEnd)
                        res += this.writeResult + "\t";
                }
            }
        }
        return res;
    }
}
