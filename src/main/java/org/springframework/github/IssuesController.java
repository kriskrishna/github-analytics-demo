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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/issues")
class IssuesController {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final IssuesService service;

	IssuesController(IssuesService service) {
		this.service = service;
	}

	@GetMapping("/count")
	public long count() {
		long size = service.numberOfIssues();
		log.info("Size of issues equals [{}]", size);
		return size;
	}

	@GetMapping
	public List<IssueDto> allIssues() {
		return service.allIssues();
	}


	@RequestMapping(
			value = "/{id}",
			method = RequestMethod.GET)
	public ResponseEntity<Issues> getOne(@PathVariable("id") Long id) throws Exception {
		Issues entity = service.getOne(id);
		return ok(entity);
	}

	@PostMapping
	public void data() {
		String time = LocalDateTime.now().toString();
		String repo = "spring-cloud/" + time;
		log.info("Will store an issue name [{}], repo [{}]", time, repo);
		service.save(time, repo);
	}

	@DeleteMapping
	public void delete() {
		service.deleteAll();
	}

}

