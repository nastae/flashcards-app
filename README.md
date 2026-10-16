# Flashcards Application

## Work Daily

### Run backend:
```
cd flashcards-service
./mvnw spring-boot:run
```

#### Configure Cloudflare and Cloudinary Services
The application relies on environment variables for sensitive credentials (such as API tokens and secrets) which are mapped directly inside `application.yaml`.

##### Global Setup (Windows Machine)
To make these environment variables available globally across your Windows system (so IntelliJ, terminal windows, and other tools pick them up automatically):

1. Press the **Windows Key**, type **Environment Variables**, and select **Edit the system environment variables**.
2. Click the **Environment Variables...** button at the bottom right.
3. In the top window (**User variables for your user**), click **New...** (or choose **System variables** if you want them for all users).
4. Add your configuration keys:
    * **Variable name:** `CLOUDFLARE_API_TOKEN` | **Variable value:** `your_actual_token`
    * **Variable name:** `CLOUDINARY_URL` | **Variable value:** `your_actual_url`
5. Click **OK** on all windows to save.

*Note: Restart IntelliJ or your terminal if they were already open so they can load the new global environment variables.*

##### Alternative: Local IntelliJ Configuration Only
If you prefer not to set them globally on your machine:
1. Go to **Run** → **Edit Configurations...** in IntelliJ.
2. Select your Spring Boot application configuration.
3. In the **Environment variables** field, add your variables separated by semicolons (e.g., `CLOUDFLARE_API_TOKEN=your_token;CLOUDINARY_URL=your_url`).

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
25. (Flashcard -> create flashcard) Add loading when upload image into cloudinary and save flashcard request into database
26. Add edit flashcard page
27. (Done) Fix study page to allow to show image, when there is normal flashcard, prompt: when flashcard is normal, it should show image, how to make it? answer in short.
28. On AI image generation, what status (ImageGenerationStatus) should have, if image uploaded or inserted URL?
29. add a persisted job/outbox mechanism and recovery for stale PENDING/GENERATING jobs (ImageGenerationJobWorker)
30. add integration tests for the four main outcomes: no AI request, successful generation, failed generation, and retry. (ImageGenerationJobWorker)
31. Update README.md to setup Cloudinary and Cloudflare environment variables on a machine
32. Create translation to english from native language word, create native language dropdown, native or english word text input, and radio button or some other element to set translate automatically or translate by yourself