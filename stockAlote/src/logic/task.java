package logic;

public class task {
    private int id;
    private String taskType;
    private String productName;
    private String productCategory;
    private String destinationCategory;
    private int productQty;
    private boolean isCompleted;
    private String startTime;
    private String endTime;
    private String doneBy;

    // Getters
    public int getId() {
        return id;
    }
    public String getTaskType() {
        return taskType;
    }
    public String getProductName() {
        return productName;
    }
    public String getProductCategory() {
        return productCategory;
    }
    public String getDestinationCategory() { return destinationCategory; }
    public int getProductQty() {
        return productQty;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public String getDoneBy() {
        return doneBy;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    public void setDestinationCategory(String destinationCategory) { this.destinationCategory = destinationCategory; }
    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
    }
}
