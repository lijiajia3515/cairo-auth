package com.hfhk.auth.service.auth.repository.mongo;

import com.hfhk.auth.service.auth.domain.mongo.UserMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMongoRepository extends MongoRepository<UserMongo, String> {

}
