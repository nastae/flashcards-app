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

### Improvements:
1. Check which dependencies should explicitly to set versions and add to them.
2. Update repositories, services and controllers to use pages, sortings and filtering (filtering optional).
3. Add groups creation page (and groups and flashcards ui pages, routings, components and services)
4. Fix double content when go to /groups/create page