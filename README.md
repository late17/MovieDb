# Movie DB Application for Interview based on this task: https://github.com/AppSci/android-test-task

### Before launch you should have
- Android Studio Koala (https://developer.android.com/studio) to support Kotlin 2.0
- Set API_KEY_MOVIE_DB="YOUR_API_KEY" in local.properties

### What's done:
- All Functional requirements
- Optional Requirements: Implement movie sharing via any existing provider

### Technologies: 
- Kotlin 2.0
- Jetpack Compose
- Hilt
- Ktor
- Room
- Coil
- Junit + Mockito

### Note that: 
- The API method is not optimised for this kind of work https://developer.themoviedb.org/reference/discover-movie. Page 12 can return the film that should be at the top of the list :). Because of these limitations, the mapping in the VM was done as it is.
- There is no login, as I didn't have time to implement it, but you can see the setup of the login screen, the navbar and the user state. You can just ignore it.
- Have a nice day.
