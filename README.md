# Slide (Not Glide 😄) - Android Image Loading Library

This project implements a custom image downloading and caching library for Android, along with an example app that demonstrates its usage.

⚠️This is a project created in ~3h which means its missing tests, and also might be not that clean I'd like it to be. Please consider this when reviewing.

## Project Structure

### ImageLoader Library (`imageloader` module)
A custom image loading library that provides:
- Image downloading from URLs
- Memory and disk caching with 4-hour TTL
- Manual cache invalidation
- Support for both Kotlin and Java

### Example App (`app` module)
A sample application that:
- Fetches a JSON list of images from the provided API
- Displays images in a RecyclerView using the custom image loader
- Shows image ID and title
- Includes a button to manually invalidate the cache

## Features

### Image Loading Library
- **URL-based image loading**: Load images from any HTTP/HTTPS URL
- **Dual caching system**: Memory cache (LRU) + disk cache
- **TTL support**: Cached images are valid for 4 hours
- **Manual cache invalidation**: Clear cache on demand
- **Placeholder support**: Show placeholder while loading
- **Thread-safe**: All operations are thread-safe
- **Java/Kotlin compatibility**: Works with both languages

### Example App Features
- Loads image list from: `https://acharyaprashant.org/api/v2/content/misc/media-coverages?limit=100`
- RecyclerView with custom adapter
- Shows loading indicator
- Cache invalidation button
- Error handling with toast messages

## Usage

### Kotlin && Java
```kotlin
Slide.load(
    url = image.url,
    imageView = imageView,
    loadingPlaceholderResId = R.drawable.placeholder_loading,
    failurePlaceholderResId = R.drawable.placeholder_failure
)

// Invalidate cache
Slide.invalidateCache()
```

## Library Architecture

### Core Components

1. **ImageLoadingClient**: Class that encapsulates the image loading logic and takes care of concurrency.
2. **MemorySource**: Handles memory caching with TTL
2. **DiskSource**: Handles disk caching with TTL
3. **NetworkSource**: Downloads images

### Caching Strategy

- **Memory Cache**: LRU cache with 10MB limit
- **Disk Cache**: Files stored in app's cache directory
- **TTL**: 4 hours expiration time
- **Cache Keys**: MD5 hash of image URL and custom tags

## Dependencies

The library has minimal dependencies:
- Android SDK (API 24+)
- No external libraries (no Glide, Picasso, etc.)

## Requirements Met

✅ Custom image downloading and caching library  
✅ No external libraries like Glide  
✅ Fetches JSON from provided URL  
✅ Example app using the library  
✅ 4-hour cache validity  
✅ Manual cache invalidation  
✅ Shows image and ID in list  
✅ Android View system (not Jetpack Compose)  
✅ Java and Kotlin support  

## Building the Project

1. Clone the repository
2. Open in Android Studio
3. Sync gradle files
4. Run the app

## API Usage Example

The app demonstrates loading images from:
```
https://zipoapps-storage-test.nyc3.digitaloceanspaces.com/image_list.json
```

Each image item contains:
- `id`: Unique identifier
- `title`: Image file name extracted from the image url
- `imageUrl`: Image url

## Cache Management

The cache automatically:
- Stores images for 4 hours
- Cleans expired entries
- Handles storage errors gracefully
- Provides manual invalidation
