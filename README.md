# Kotlin Diary App

A simple diary application built with Kotlin and Jetpack Compose.

## Features

- Create diary entries with title and content
- View all diary entries in a list
- Edit existing diary entries
- Delete diary entries
- Modern UI with Material Design 3

## Architecture

This app follows the MVVM (Model-View-ViewModel) architectural pattern:

- **Model**: Room database entities and DAO
- **View**: Jetpack Compose UI screens
- **ViewModel**: Business logic and UI state management
- **Repository**: Data management layer

## Project Structure

```
app/src/main/java/com/example/diaryapp/
├── data/
│   ├── local/          # Room database, DAO, and entities
│   └── repository/     # Repository pattern implementation
├── ui/
│   ├── screens/        # Compose UI screens
│   ├── theme/          # Material Design theme
│   └── MainActivity.kt
├── navigation/
│   └── AppNavigation.kt # Navigation between screens
├── viewmodel/
│   └── DiaryViewModel.kt # ViewModel with Factory
```

## Libraries Used

- Jetpack Compose: Modern UI toolkit
- Room: Local database
- Navigation: Compose navigation
- ViewModel: UI-related data holder
- Coroutines: Asynchronous operations
- Flow: Reactive streams

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Build and run the app