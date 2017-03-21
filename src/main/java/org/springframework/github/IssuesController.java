/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.github;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issues")
class IssuesController {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final IssuesRepository repository;

	IssuesController(IssuesRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/count")
	public long count() {
		long size = repository.count();
		log.info("Size of issues equals [{}]", size);
		return size;
	}

	@GetMapping
	public List<IssueDto> allIssues() {
		List<IssueDto> dtos = new ArrayList<>();
		repository.findAll().forEach(i -> dtos.add(new IssueDto(i.getUsername(), i.getRepository())));
		return dtos;
	}

	@PostMapping
	public void data() {
		String time = LocalDateTime.now().toString();
		String repo = "spring-cloud/" + time;
		log.info("Will store an issue name [{}], repo [{}]", time, repo);
		store(time, repo);
	}

	@DeleteMapping
	public void delete() {
		clear();
	}

	void store(String user, String repo) {
		repository.save(new Issues(user, repo));
	}

	void clear() {
		repository.deleteAll();
	}

}

class IssueDto {
	private String userName;
	private String repository;

	IssueDto(String userName, String repository) {
		this.userName = userName;
		this.repository = repository;
	}

	IssueDto() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}
}
