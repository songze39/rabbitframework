package com.rabbitframework.jadb.scripting.xmltags;

import com.rabbitframework.jadb.scripting.DynamicContext;

import java.util.List;

/**
 * 混合sql节点
 */
public class MixedSqlNode implements SqlNode {
    private List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        for (SqlNode sqlNode : contents) {
            sqlNode.apply(context);
        }
        return true;
    }
}
