setup:
	bash setup.sh
build:
	./mvnw clean install
test:
	bash test.sh
run:
	./mvnw spring-boot:run
clean:
	./mvnw clean