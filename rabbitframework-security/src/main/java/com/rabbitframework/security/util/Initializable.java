
package com.rabbitframework.security.util;

import com.rabbitframework.security.SecurityException;

/**
 * 初始化公共接口
 */
public interface Initializable {
    void init() throws SecurityException;

}
