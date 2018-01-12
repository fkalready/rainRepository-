package cn.itcast.erp.entity;

import java.util.List;

public class Tree {

    private String id; //节点的id值
    private String text;//节点的名称
    private boolean checked;//节点是否被选择
    private List<Tree> children;//节点的子节点

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }
}
