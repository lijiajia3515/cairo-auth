package com.lijiajia3515.cairo.auth;

import com.lijiajia3515.cairo.auth.modules.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Metadata implements Serializable {
	private Action created;
	private Action lastModified;

	@Data
	@Accessors(chain = true)

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Action implements Serializable {
		private User user;
		private LocalDateTime at;
	}
}
