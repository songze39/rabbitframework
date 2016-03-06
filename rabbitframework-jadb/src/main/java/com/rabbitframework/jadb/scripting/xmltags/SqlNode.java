package com.rabbitframework.jadb.scripting.xmltags;

import com.rabbitframework.jadb.scripting.DynamicContext;

/**
 * sql xml节点接口
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
