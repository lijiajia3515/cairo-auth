package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.domain.user.UserPageFindRequest;
import com.hfhk.cairo.core.page.Page;

public interface UserMongoTemplate {

	Page<UserMongo> pageFind(UserPageFindRequest request);
}
