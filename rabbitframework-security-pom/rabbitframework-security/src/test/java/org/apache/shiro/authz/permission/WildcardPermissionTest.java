package org.apache.shiro.authz.permission;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.Permission;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class WildcardPermissionTest {
	private Logger logger = LoggerFactory
			.getLogger(WildcardPermissionTest.class);

	@Test
	public void testParts() {
		// 结果需要精准匹配
		WildcardPermission permission = new WildcardPermission("/index:/index/test");
		List<Permission> perminfo = new ArrayList<>();
		perminfo.add(new WildcardPermission("/index/test"));
		perminfo.add(new WildcardPermission("/index"));
		for (Permission permission2 : perminfo) {
			logger.info("result:" + permission2.implies(permission));
		}

		logger.info(permission.getParts().toString());
	}
}
