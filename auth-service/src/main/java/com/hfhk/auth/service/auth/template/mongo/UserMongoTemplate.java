package com.hfhk.auth.service.auth.template.mongo;

import com.hfhk.cairo.auth.domain.request.UserFindRequest;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.auth.service.auth.domain.mongo.UserMongo;
import org.springframework.data.domain.Pageable;

public interface UserMongoTemplate {

	Page<UserMongo> pageFind(UserFindRequest request, Pageable pageable);
}
