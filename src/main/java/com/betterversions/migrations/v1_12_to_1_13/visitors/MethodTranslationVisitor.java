package com.betterversions.migrations.v1_12_to_1_13.visitors;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;

public class MethodTranslationVisitor extends ModifierVisitor<JsonNode> {
    
    @Override
    public Visitable visit(MethodCallExpr n, JsonNode mappings) {
        // Translate obfuscated method names
        if (mappings.has("methods")) {
            JsonNode methods = mappings.get("methods");
            String methodName = n.getNameAsString();
            if (methods.has(methodName)) {
                n.setName(methods.get(methodName).asText());
            }
        }
        return super.visit(n, mappings);
    }

    @Override
    public Visitable visit(FieldAccessExpr n, JsonNode mappings) {
        // Translate obfuscated field names
        if (mappings.has("fields")) {
            JsonNode fields = mappings.get("fields");
            String fieldName = n.getNameAsString();
            if (fields.has(fieldName)) {
                n.setName(fields.get(fieldName).asText());
            }
        }
        return super.visit(n, mappings);
    }

    @Override
    public Visitable visit(NameExpr n, JsonNode mappings) {
        // Translate standalone name expressions if they match fields
        if (mappings.has("fields")) {
            JsonNode fields = mappings.get("fields");
            String name = n.getNameAsString();
            if (fields.has(name)) {
                n.setName(fields.get(name).asText());
            }
        }
        return super.visit(n, mappings);
    }
}
