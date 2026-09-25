SHELL := /bin/bash
.DEFAULT_GOAL := help

.PHONY: help setup doctor up down restart logs ps psql build run test clean fresh add-deps remove-deps

## Setup & diagnostics -------------------------------------------------------

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-16s\033[0m %s\n", $$1, $$2}'

setup: ## Install everything needed (Java, Docker, .env) and boot dependencies. Windows: use scripts\setup.ps1
	bash setup.sh

doctor: ## Check that the local environment is correctly configured
	bash scripts/doctor.sh

## Local infrastructure -------------------------------------------------------

up: ## Start local dependencies (Postgres) in the background
	docker compose up -d

down: ## Stop local dependencies
	docker compose down

restart: down up ## Restart local dependencies

logs: ## Tail docker compose logs
	docker compose logs -f

ps: ## Show status of local dependencies
	docker compose ps

psql: ## Open a psql shell against the local database
	docker compose exec postgres psql -U $${POSTGRES_USER:-dev} -d $${POSTGRES_DB:-dbserver_voting_db}

## Build & run -----------------------------------------------------------

build: ## Compile and package the application
	./mvnw clean install

run: ## Run the application (dev profile)
	./mvnw spring-boot:run -Dspring.profiles.active=dev

test: ## Run the test suite
	bash test.sh

clean: ## Remove build outputs
	./mvnw clean

fresh: clean up build ## Clean, start dependencies and rebuild from scratch

## Dependency management --------------------------------------------------

# Usage:
#   make add-deps deps="org.springframework.boot:spring-boot-starter-validation"
#   make add-deps deps="groupId:artifactId:version"
#   make add-deps groupId:artifactId                   (positional form also works)
#   make add-deps scope=test deps="org.projectlombok:lombok"
add-deps: ## Add one or more Maven dependencies (deps="groupId:artifactId[:version] ...")
	@deps="$(deps) $(filter-out $@,$(MAKECMDGOALS))"; \
	if [ -z "$$(echo $$deps | xargs)" ]; then \
		echo "Usage: make add-deps deps=\"groupId:artifactId:version\" [scope=test]"; \
		exit 1; \
	fi; \
	bash scripts/add-dependency.sh $(if $(scope),--scope=$(scope),) $$deps

remove-deps: ## Remove one or more Maven dependencies (deps="artifactId ...")
	@deps="$(deps) $(filter-out $@,$(MAKECMDGOALS))"; \
	if [ -z "$$(echo $$deps | xargs)" ]; then \
		echo "Usage: make remove-deps deps=\"artifactId\""; \
		exit 1; \
	fi; \
	bash scripts/remove-dependency.sh $$deps

# Allows `make add-deps groupId:artifactId` by swallowing extra positional
# arguments as no-op targets instead of "No rule to make target" errors.
%:
	@: