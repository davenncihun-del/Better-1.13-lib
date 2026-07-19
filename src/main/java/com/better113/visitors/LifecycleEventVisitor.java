package com.better113.visitors;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class LifecycleEventVisitor extends ModifierVisitor<JsonNode> {
    
    @Override
    public Visitable visit(MethodDeclaration n, JsonNode eventsRules) {
        if (n.getParameters().size() == 1) {
            Parameter param = n.getParameter(0);
            if (param.getType() instanceof ClassOrInterfaceType) {
                String eventName = ((ClassOrInterfaceType) param.getType()).getNameAsString();
                
                if (eventsRules.has("events") && eventsRules.get("events").has(eventName)) {
                    JsonNode rule = eventsRules.get("events").get(eventName);
                    
                    // Replace parameter type
                    String newEventName = rule.get("replacement").asText();
                    ((ClassOrInterfaceType) param.getType()).setName(newEventName);
                    
                    // Add @SubscribeEvent annotation if it doesn't exist and we need it
                    if (rule.has("subscribeEvent") && rule.get("subscribeEvent").asBoolean()) {
                        boolean hasSubscribeEvent = n.getAnnotations().stream()
                            .anyMatch(a -> a.getNameAsString().equals("SubscribeEvent"));
                        
                        if (!hasSubscribeEvent) {
                            n.addAnnotation(new MarkerAnnotationExpr("SubscribeEvent"));
                        }

                        // Remove @EventHandler if present
                        n.getAnnotations().removeIf(a -> a.getNameAsString().equals("EventHandler"));
                    }
                }
            }
        }
        return super.visit(n, eventsRules);
    }
}
