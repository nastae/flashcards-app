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
+4. Fix double content when go to /groups/create page
4. Navigation bar (top or bottom)
+6. Group list page (cards) (Use mat-card) (Tap -> open group)
+7. Floating Action Button (FAB) (<button mat-fab color="primary">+</button>)
5. Near group list item show flashcards count
6. Group details page 👉 show flashcards inside
7. Empty state UX (very important) “No groups yet → CTA button”
8. Loading state (spinner) 
9. Toolbar create group button (optional)
10. Study navigation: spaced repetition, AI quiz and progress tracking (optional)
11. Groups navigation: public decks and deck sharing
12. Set scss style for group create and group edit in one place as it do the same styling (or find other solution) 
13. create reusable group-form component and reuse for create/edit
14. When MatSnackBar pop up, it is not able to press menu options 
15. Upload file, Cloudinary upload, AI-generated image, Camera upload (maybe not camera upload)
16. Maybe set mobile screen size redesigning when max-width is 600px
17. Select and implement differentiation: image-first learning, AI-generated flashcards, spaced repetition, smart review, mobile UX, public decks and AI explanations 
18. Third menu option make Profile (stats/settings later) (optional) and rethink if needed
19. Study menu option: When user opens Study: show all groups, show flashcard counts, “Continue learning” (Continue), “Start studying” (Start) and Review
20. Back button on toolar
21. Improve group view page UX
22. move mobile-nav component to separate directory (separate from groups)
23. On flashcard creation select create with translation to native language (like lithuanian) or add explanation
24. Fix layouts to be for mobile web application