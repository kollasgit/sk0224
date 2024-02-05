package com.toolrental.factory;

import com.toolrental.model.Tool;

public interface IInventoryManager {
    String displayToolList();

    void addTool(Tool tool);

    void removeTool(Tool tool);

    public Tool getTool(String toolCode);

    public boolean checkOutTool(Tool tool);
}
