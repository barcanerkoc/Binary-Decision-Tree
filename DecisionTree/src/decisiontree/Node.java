package decisiontree;

public class Node{
    
    private String className;
    private String status;
    private Node trueNode;
    private Node falseNode;
    
    public Node(String className, String status){
        
        this.className = className;
        this.status = status;
        
    }
    
    public Node(String className){
        
        this.className = className;
        
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Node getTrueNode() {
        return trueNode;
    }

    public void setTrueNode(Node trueNode) {
        this.trueNode = trueNode;
    }

    public Node getFalseNode() {
        return falseNode;
    }

    public void setFalseNode(Node falseNode) {
        this.falseNode = falseNode;
    }
    
}
