package com.toolrental.factory;

import com.toolrental.exception.InventoryException;
import com.toolrental.model.Tool;
import com.toolrental.model.ToolType;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryManager implements IInventoryManager {
    private HashMap<String, Tool> toolRepository;
    private HashMap<String, ToolType> toolsTypeRepository;

    private static Logger LOGGER = Logger.getLogger("Inventory");

    private static InventoryManager instance;


    // Static method to return the singleton instance
    public static InventoryManager getInstance() {
        if (instance == null) {
            synchronized (InventoryManager.class) {
                if (instance == null) {
                    instance = new InventoryManager();
                }
            }
        }
        return instance;
    }
    public InventoryManager() {
        initializeToolsTypes();
        initializeTools();

    }


    public void initializeToolsTypes() {
        toolsTypeRepository = new HashMap<String, ToolType>();
        // Add some sample tools
        this.addToolType(new ToolType("1", "Ladder", 1.99, true, true, false));
        this.addToolType(new ToolType("2", "Chainsaw", 1.49, true, false, true));
        this.addToolType(new ToolType("3", "Jackhammer", 2.99, true, false, false));
    }

    public void initializeTools() {
        toolRepository = new HashMap<String, Tool>();
        // Add some sample tools
        this.addTool(new Tool("CHNS","Stihl", getToolType("Chainsaw")));
        this.addTool(new Tool("LADW", "Werner", getToolType("Ladder")));
        this.addTool(new Tool("JAKD", "DeWalt", getToolType("Jackhammer")));
        this.addTool(new Tool("JAKR", "Ridgid", getToolType("Jackhammer")));
    }

    public String displayToolList() {

        StringBuilder aBuilder = new StringBuilder();
        aBuilder.append("Available Tools:");
        aBuilder.append("\n");
        aBuilder.append("=================");
        aBuilder.append("\n");
        toolRepository.forEach((key, value) -> {
            aBuilder.append(key + " " + value.getToolType().getType() + " " + value.getBrand() + " " + value.getToolType().getDailyCharge());
            aBuilder.append("\n");
        });
        aBuilder.append("=================");
        aBuilder.append("\n");
        return aBuilder.toString();
    }

    public void addTool(Tool tool) {
        this.toolRepository.put(tool.getCode(), tool);
    }

    public void removeTool(Tool tool) {
        this.toolRepository.remove(tool.getCode());
    }

    public Tool getTool(String toolCode) {
        if(toolCode != null && !toolCode.isBlank()) {
            return toolRepository.get(toolCode);
        } else {
            throw new InventoryException("Tool code can not be empty");
        }
    }

    public void addToolType(ToolType toolType) {
        this.toolsTypeRepository.put(toolType.getType(), toolType);
    }

    public void removeTool(ToolType toolType) {
        this.toolsTypeRepository.remove(toolType.getType());
    }

    public ToolType getToolType(String toolType) {
        if(toolType != null && !toolType.isBlank()) {
            return toolsTypeRepository.get(toolType);
        } else {
            throw new InventoryException("Tool type code can not be empty");
        }
    }

    public boolean checkOutTool(Tool tool) {
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Start checkOutTool " + tool.getCode());
        }
        //Do whatever necessary to check out this tool and mark the inventory as unavailable
        return true;
    }
}