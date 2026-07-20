package com.better113.visitors;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class AdvancedMethodVisitor extends ModifierVisitor<JsonNode> {

    @Override
    public Visitable visit(MethodCallExpr n, JsonNode advancedRules) {
        if (advancedRules == null || !advancedRules.has("rules")) {
            return super.visit(n, advancedRules);
        }

        JsonNode rules = advancedRules.get("rules");
        for (JsonNode rule : rules) {
            String kind = rule.has("kind") ? rule.get("kind").asText() : "";
            String methodName = rule.has("methodName") ? rule.get("methodName").asText() : "";
            
            if (!n.getNameAsString().equals(methodName)) {
                continue;
            }

            if ("METHOD_CALL_ARGS_TO_OBJECT".equals(kind)) {
                // e.g. getBlockState(x, y, z) -> getBlockState(new BlockPos(x, y, z))
                if (rule.has("matchSignature") && n.getArguments().size() == rule.get("matchSignature").size()) {
                    String wrapperConstructor = rule.has("wrapperConstructor") ? rule.get("wrapperConstructor").asText() : "";
                    if (!wrapperConstructor.isEmpty()) {
                        // Extract class name from FQN, e.g., net.minecraft.util.math.BlockPos -> BlockPos
                        String className = wrapperConstructor.substring(wrapperConstructor.lastIndexOf('.') + 1);
                        
                        NodeList<Expression> originalArgs = new NodeList<>(n.getArguments());
                        ObjectCreationExpr blockPosCreation = new ObjectCreationExpr(null, new ClassOrInterfaceType(null, className), originalArgs);
                        
                        n.getArguments().clear();
                        n.addArgument(blockPosCreation);
                        return super.visit(n, advancedRules);
                    }
                }
            } else if ("METHOD_CALL_OBJECT_TO_ARGS".equals(kind)) {
                // e.g. getBlockState(pos) -> getBlockState(pos.getX(), pos.getY(), pos.getZ())
                if (rule.has("matchSignature") && n.getArguments().size() == 1) {
                    Expression arg = n.getArgument(0);
                    if (rule.has("unwrapAccessors")) {
                        JsonNode accessors = rule.get("unwrapAccessors");
                        
                        NodeList<Expression> newArgs = new NodeList<>();
                        for (JsonNode accessor : accessors) {
                            String accName = accessor.asText();
                            newArgs.add(new MethodCallExpr(arg, accName));
                        }
                        
                        n.setArguments(newArgs);
                        return super.visit(n, advancedRules);
                    }
                }
            }
        }

        return super.visit(n, advancedRules);
    }
}
