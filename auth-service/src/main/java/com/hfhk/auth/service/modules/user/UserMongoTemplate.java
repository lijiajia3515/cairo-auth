package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.user.UserPageFindRequest;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.auth.domain.mongo.UserMongo;

public interface UserMongoTemplate {

	Page<UserMongo> pageFind(UserPageFindRequest request);
}
