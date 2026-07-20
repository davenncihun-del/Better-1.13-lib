package com.betterversions.migrations.v1_12_to_1_13.visitors;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.NameExpr;

public class BlockStateVisitor extends ModifierVisitor<JsonNode> {
    
    @Override
    public Visitable visit(MethodCallExpr n, JsonNode blocksRules) {
        // Search for metadata argument checks/flattening like: new ItemStack(Blocks.WOOL, 1, 14) -> new ItemStack(Blocks.RED_WOOL)
        // For simplicity, we just look for getStateFromMeta(meta) on field accesses to Blocks.WOOL, etc.
        // Or we just find new ItemStack(Blocks.WOOL, count, meta)
        // This is a naive implementation for the proof of concept.
        
        if (n.getNameAsString().equals("ItemStack") || n.getNameAsString().equals("new ItemStack")) {
            if (n.getArguments().size() >= 3 && blocksRules.has("block_states")) {
                JsonNode states = blocksRules.get("block_states");
                String blockName = n.getArgument(0).toString(); // e.g. Blocks.WOOL
                
                if (states.has(blockName) && n.getArgument(2) instanceof IntegerLiteralExpr) {
                    JsonNode metadataMap = states.get(blockName).get("metadata");
                    String metaVal = ((IntegerLiteralExpr) n.getArgument(2)).getValue();
                    
                    if (metadataMap.has(metaVal)) {
                        // We found a flattening target! Replace the block argument
                        String newBlock = metadataMap.get(metaVal).asText();
                        n.setArgument(0, new NameExpr(newBlock));
                        
                        // Remove the metadata argument from ItemStack since 1.13 doesn't have it
                        n.getArguments().remove(2);
                    }
                }
            }
        }

        return super.visit(n, blocksRules);
    }
}
