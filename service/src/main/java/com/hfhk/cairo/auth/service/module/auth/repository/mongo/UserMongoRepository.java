package com.hfhk.cairo.auth.service.module.auth.repository.mongo;

import com.hfhk.cairo.auth.service.module.auth.domain.mongo.UserMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMongoRepository extends MongoRepository<UserMongo, String> {

}
