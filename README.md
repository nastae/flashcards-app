# Flashcards Application

## Work Daily

### Run backend:
```
cd flashcards-service
./mvnw spring-boot:run
```

### Run frontend:
```
cd flashcards-ui
npm start
```
### Alternative (Advanced, Optional Later)
You can build Angular and serve it from Spring Boot:
```
cd frontend
npm run build
```
Copy /dist → backend/src/main/resources/static
Then Spring Boot serves frontend

