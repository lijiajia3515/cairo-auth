package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.request.UserFindRequest;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.auth.domain.mongo.UserMongo;
import org.springframework.data.domain.Pageable;

public interface UserMongoTemplate {

	Page<UserMongo> pageFind(UserFindRequest request, Pageable pageable);
}
