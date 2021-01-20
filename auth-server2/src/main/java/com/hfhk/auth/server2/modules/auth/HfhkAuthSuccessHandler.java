package com.hfhk.auth.server2.modules.auth;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class HfhkAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private final MongoTemplate mongoTemplate;

	public HfhkAuthSuccessHandler(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		this.updateLoginTime(authentication);
		super.onAuthenticationSuccess(request, response, authentication);
	}

	public void updateLoginTime(Authentication authentication) {

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			if (authentication.getPrincipal() instanceof AuthUser) {
				AuthUser authUser = (AuthUser) authentication.getPrincipal();
				LocalDateTime now = LocalDateTime.now();
				Query query = Query.query(Criteria.where(UserMongo.FIELD.UID).is(authUser.getUid()));
				Update update = Update.update(UserMongo.FIELD.LAST_LOGIN_AT, now);
				mongoTemplate.updateFirst(query, update, UserMongo.class, Mongo.Collection.USER);
			}
		}
	}
}
