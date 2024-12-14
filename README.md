# Camera in Android

This README provides guidance on implementing a camera feature in an Android application using Java or Kotlin.

## Features
- Capture photos using the device's camera.
- Save captured images to storage.
- Display a preview of the camera feed.
- Handle permissions for accessing the camera and storage.

## Prerequisites
1. Android Studio installed on your system.
2. Basic understanding of Android development in Java or Kotlin.
3. A physical device or an emulator with camera support.

## Permissions
To use the camera in your Android app, add the following permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

Additionally, declare the `Camera` feature:

```xml
<uses-feature android:name="android.hardware.camera" android:required="false" />
<uses-feature android:name="android.hardware.camera.autofocus" android:required="false" />
```

## Setting Up CameraX (Preferred Library)
Using CameraX simplifies the implementation of camera features. Add the following dependencies to your `build.gradle` file:

```groovy
dependencies {
    implementation "androidx.camera:camera-core:1.5.0"
    implementation "androidx.camera:camera-view:1.5.0"
    implementation "androidx.camera:camera-lifecycle:1.5.0"
    implementation "androidx.camera:camera-extensions:1.5.0"
}
```

## Code Implementation
### 1. Camera Permission Handling
Use the `ActivityCompat.requestPermissions` method to request permissions at runtime.

```java
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
}
```

### 2. Set Up CameraX Preview
In your activity or fragment, initialize the CameraX preview:

#### Java:
```java
private void startCamera() {
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
            ProcessCameraProvider.getInstance(this);

    cameraProviderFuture.addListener(() -> {
        try {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

            Preview preview = new Preview.Builder().build();
            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();

            preview.setSurfaceProvider(previewView.getSurfaceProvider());
            cameraProvider.bindToLifecycle(this, cameraSelector, preview);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }, ContextCompat.getMainExecutor(this));
}
```

#### Kotlin:
```kotlin
private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }, ContextCompat.getMainExecutor(this))
}
```

### 3. Capture Image
#### Java:
```java
private void capturePhoto() {
    ImageCapture imageCapture = new ImageCapture.Builder().build();

    File photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg");

    ImageCapture.OutputFileOptions options = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

    imageCapture.takePicture(options, ContextCompat.getMainExecutor(this),
            new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    Toast.makeText(MainActivity.this, "Photo Saved!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    exception.printStackTrace();
                }
            });
}
```

#### Kotlin:
```kotlin
private fun capturePhoto() {
    val imageCapture = ImageCapture.Builder().build()

    val photoFile = File(externalMediaDirs.first(), "photo.jpg")

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Toast.makeText(this@MainActivity, "Photo Saved!", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
            }
        })
}
```

## Screenshots
You can include relevant screenshots or UI diagrams here to help users understand the feature.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Contribution
Feel free to fork this repository, create new branches, and submit pull requests. All contributions are welcome!
