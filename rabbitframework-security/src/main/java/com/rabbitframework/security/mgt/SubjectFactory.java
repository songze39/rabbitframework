package com.rabbitframework.security.mgt;

import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.subject.SubjectContext;

/**
 * 构造{@link com.rabbitframework.security.subject.Subject}
 */
public interface SubjectFactory {
    Subject createSubject(SubjectContext context);
}
